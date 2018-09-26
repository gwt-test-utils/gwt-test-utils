package com.googlecode.gwt.test.internal.rewrite;

import com.google.gwt.core.client.GwtScriptOnly;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.*;
import com.google.gwt.dev.javac.CompilationState;
import com.google.gwt.dev.jjs.InternalCompilerException;
import com.google.gwt.dev.shell.JsValueGlue;
import com.google.gwt.dev.shell.rewrite.HostedModeClassRewriter;
import com.google.gwt.dev.shell.rewrite.HostedModeClassRewriter.InstanceMethodOracle;
import com.google.gwt.dev.shell.rewrite.HostedModeClassRewriter.SingleJsoImplData;
import com.google.gwt.dev.util.Name;
import com.google.gwt.dev.util.collect.Lists;
import com.google.gwt.dev.util.log.speedtracer.DevModeEventType;
import com.google.gwt.dev.util.log.speedtracer.SpeedTracerLogger;
import com.google.gwt.dev.util.log.speedtracer.SpeedTracerLogger.Event;
import com.googlecode.gwt.test.GwtTreeLogger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Method;

import java.util.*;

/**
 * This class performs any and all byte code rewriting needed to make Overlay types work with
 * gwt-test-utils.
 * <ol>
 * <li>Rewrites all JSO types into an interface type (which retains the original name) and an
 * implementation type (which has a $ appended).</li>
 * <li>All JSO interface types are empty and mirror the original type hierarchy.</li>
 * <li>All JSO impl types contain the guts of the original type, except that all instance methods
 * are reimplemented as statics.</li>
 * <li>Calls sites to JSO types rewritten to dispatch to impl types. Any virtual calls are also made
 * static. Static field references to JSO types reference static fields in the the impl class.</li>
 * <li>JavaScriptObject$ implements all the interface types and is the only instantiable type.</li>
 * </ol>
 * <strong>For internal use only.</strong>
 *
 * @see RewriteRefsToJsoClasses
 * @see WriteJsoImpl
 */
public class OverlayTypesRewriter {

    /**
     * Implements {@link InstanceMethodOracle} on behalf of the {@link HostedModeClassRewriter}.
     * Implemented using {@link TypeOracle}.
     */
    private class MyInstanceMethodOracle implements InstanceMethodOracle {

        private final Map<String, Set<JClassType>> signatureToDeclaringClasses = new HashMap<>();

        public MyInstanceMethodOracle(Set<JClassType> jsoTypes, JClassType javaLangObject) {
            // Record that the JSO implements its own methods
            for (JClassType type : jsoTypes) {
                for (JMethod method : type.getMethods()) {
                    if (!method.isStatic()) {
                        assert !method.isAbstract() : "Abstract method in JSO type " + method;
                        add(type, method);
                    }
                }
            }

         /*
          * Record the implementing types for methods defined in SingleJsoImpl interfaces. We have
          * to make this pass because of possible variance in the return types between the abstract
          * method declaration in the interface and the concrete method.
          */
            for (String intfName : jsoData.getSingleJsoIntfTypes()) {
                // We only store the name in the data block to keep it lightweight
                JClassType intf = typeOracle.findType(Name.InternalName.toSourceName(intfName));
                JClassType jso = typeOracle.getSingleJsoImpl(intf);
                for (JMethod method : intf.getMethods()) {
                    JClassType implementingJso = findImplementingTypeForMethod(jso, method);
                    assert implementingJso != null : "Jso should contain method: "
                            + method.getJsniSignature();
                    add(implementingJso, method);
                }
            }

            // Object clobbers everything.
            for (JMethod method : javaLangObject.getMethods()) {
                if (!method.isStatic()) {
                    String signature = createSignature(method);
                    Set<JClassType> declaringClasses = new HashSet<>();
                    signatureToDeclaringClasses.put(signature, declaringClasses);
                    declaringClasses.add(javaLangObject);
                }
            }
        }

        public String findOriginalDeclaringClass(String desc, String signature) {
            // Lookup the method.
            Set<JClassType> declaringClasses = signatureToDeclaringClasses.get(signature);
            assert declaringClasses != null : "No classes for " + signature;
            if (declaringClasses.size() == 1) {
                // Shortcut: if there's only one answer, it must be right.
                return createDescriptor(declaringClasses.iterator().next());
            }
            // Must check for assignability.
            String sourceName = desc.replace('/', '.');
            sourceName = sourceName.replace('$', '.');
            JClassType declaredType = typeOracle.findType(sourceName);

            // Check if I declare this directly.
            if (declaringClasses.contains(declaredType)) {
                return desc;
            }

            // Check to see what type I am assignable to.
            for (JClassType possibleSupertype : declaringClasses) {
                if (declaredType.isAssignableTo(possibleSupertype)) {
                    return createDescriptor(possibleSupertype);
                }
            }
            throw new IllegalArgumentException("Could not resolve signature '" + signature
                    + "' from class '" + desc + "'");
        }

        /**
         * Record that a given JSO type contains the concrete implementation of a (possibly abstract)
         * method.
         */
        private void add(JClassType type, JMethod method) {
            String signature = createSignature(method);
            Set<JClassType> declaringClasses = signatureToDeclaringClasses.get(signature);
            if (declaringClasses == null) {
                declaringClasses = new HashSet<>();
                signatureToDeclaringClasses.put(signature, declaringClasses);
            }
            declaringClasses.add(type);
        }

        private String createDescriptor(JClassType type) {
            String jniSignature = type.getJNISignature();
            return jniSignature.substring(1, jniSignature.length() - 1);
        }

        private String createSignature(JMethod method) {
            StringBuffer sb = new StringBuffer(method.getName());
            sb.append('(');
            for (JParameter param : method.getParameters()) {
                sb.append(param.getType().getJNISignature());
            }
            sb.append(')');
            sb.append(method.getReturnType().getJNISignature());
            String signature = sb.toString();
            return signature;
        }
    }

    /**
     * Cook up the data we need to support JSO subtypes that implement interfaces with methods. This
     * includes the set of SingleJsoImpl interfaces actually implemented by a JSO type, the mangled
     * method names, and the names of the Methods that should actually implement the virtual
     * functions.
     * <p>
     * Given the current implementation of JSO$ and incremental execution of rebinds, it's not
     * possible for Generators to produce additional JavaScriptObject subtypes, so this data can
     * remain static.
     */
    private class MySingleJsoImplData implements SingleJsoImplData {
        private final SortedSet<String> mangledNames = new TreeSet<>();
        private final Map<String, List<Method>> mangledNamesToDeclarations = new HashMap<>();
        private final Map<String, List<Method>> mangledNamesToImplementations = new HashMap<>();
        private final Set<String> unmodifiableIntfNames = Collections.unmodifiableSet(singleJsoImplTypes);
        private final SortedSet<String> unmodifiableNames = Collections.unmodifiableSortedSet(mangledNames);

        public MySingleJsoImplData(TypeOracle typeOracle) {
            // Loop over all interfaces with JSO implementations
            typeLoop:
            for (JClassType type : typeOracle.getSingleJsoImplInterfaces()) {
                assert type.isInterface() == type : "Expecting interfaces only";

            /*
             * By preemptively adding all possible mangled names by which a method could be called,
             * we greatly simplify the logic necessary to rewrite the call-site.
             *
             * interface A {void m();}
             *
             * interface B extends A {void z();}
             *
             * becomes
             *
             * c_g_p_A_m() -> JsoA$.m$()
             *
             * c_g_p_B_m() -> JsoA$.m$()
             *
             * c_g_p_B_z() -> JsoB$.z$()
             */
                for (JMethod intfMethod : type.getOverridableMethods()) {
                    assert intfMethod.isAbstract() : "Expecting only abstract methods";

               /*
                * It is necessary to locate the implementing type on a per-method basis. Consider
                * the case of
                *
                * @SingleJsoImpl interface C extends A, B {}
                *
                * Methods inherited from interfaces A and B must be dispatched to their respective
                * JSO implementations.
                */
                    JClassType implementingType = typeOracle.getSingleJsoImpl(intfMethod.getEnclosingType());

                    if (implementingType == null
                            || implementingType.isAnnotationPresent(GwtScriptOnly.class)) {
                  /*
                   * This means that there is no concrete implementation of the interface by a JSO.
                   * Any implementation that might be created by a Generator won't be a JSO subtype,
                   * so we'll just ignore it as an actionable type. Were Generators ever able to
                   * create new JSO subtypes, we'd have to speculatively rewrite the callsite.
                   */
                        continue typeLoop;
                    }

               /*
                * Record the type as being actionable.
                */
                    singleJsoImplTypes.add(canonicalizeClassName(getBinaryName(type)));

               /*
                * The mangled name adds the current interface like
                *
                * com_foo_Bar_methodName
                */
                    String mangledName = getBinaryName(type).replace('.', '_') + "_"
                            + intfMethod.getName();
                    mangledNames.add(mangledName);

               /*
                * Handle virtual overrides by finding the method that we would normally invoke and
                * using its declaring class as the dispatch target.
                */
                    JMethod implementingMethod;
                    while ((implementingMethod = findOverloadUsingErasure(implementingType, intfMethod)) == null) {
                        implementingType = implementingType.getSuperclass();
                    }
                    // implementingmethod and implementingType cannot be null here

               /*
                * Create a pseudo-method declaration for the interface method. This should look
                * something like
                *
                * ReturnType method$ (ParamType, ParamType)
                *
                * This must be kept in sync with the WriteJsoImpl class.
                */
                    {
                        String decl = getBinaryOrPrimitiveName(intfMethod.getReturnType().getErasedType())
                                + " " + intfMethod.getName() + "(";
                        for (JParameter param : intfMethod.getParameters()) {
                            decl += ",";
                            decl += getBinaryOrPrimitiveName(param.getType().getErasedType());
                        }
                        decl += ")";

                        Method declaration = Method.getMethod(decl);
                        addToMap(mangledNamesToDeclarations, mangledName, declaration);
                    }

               /*
                * Cook up the a pseudo-method declaration for the concrete type. This should look
                * something like
                *
                * ReturnType method$ (JsoType, ParamType, ParamType)
                *
                * This must be kept in sync with the WriteJsoImpl class.
                */
                    {
                        String returnName = getBinaryOrPrimitiveName(implementingMethod.getReturnType().getErasedType());
                        String jsoName = getBinaryOrPrimitiveName(implementingType);

                        String decl = returnName + " " + intfMethod.getName() + "$ (" + jsoName;
                        for (JParameter param : implementingMethod.getParameters()) {
                            decl += ",";
                            decl += getBinaryOrPrimitiveName(param.getType().getErasedType());
                        }
                        decl += ")";

                        Method toImplement = Method.getMethod(decl);
                        addToMap(mangledNamesToImplementations, mangledName, toImplement);
                    }
                }
            }

            TreeLogger logger = GwtTreeLogger.get();
            if (logger.isLoggable(TreeLogger.SPAM)) {
                TreeLogger dumpLogger = logger.branch(TreeLogger.SPAM, "SingleJsoImpl method mappings");
                for (Map.Entry<String, List<Method>> entry : mangledNamesToImplementations.entrySet()) {
                    dumpLogger.log(TreeLogger.SPAM, entry.getKey() + " -> " + entry.getValue());
                }
            }
        }

        public List<Method> getDeclarations(String mangledName) {
            List<Method> toReturn = mangledNamesToDeclarations.get(mangledName);
            return toReturn == null ? null : Collections.unmodifiableList(toReturn);
        }

        public List<Method> getImplementations(String mangledName) {
            List<Method> toReturn = mangledNamesToImplementations.get(mangledName);
            return toReturn == null ? toReturn : Collections.unmodifiableList(toReturn);
        }

        public SortedSet<String> getMangledNames() {
            return unmodifiableNames;
        }

        public Set<String> getSingleJsoIntfTypes() {
            return unmodifiableIntfNames;
        }

        /**
         * Assumes that the usual case is a 1:1 mapping.
         */
        private <K, V> void addToMap(Map<K, List<V>> map, K key, V value) {
            List<V> list = map.get(key);
            if (list == null) {
                map.put(key, Lists.create(value));
            } else {
                List<V> maybeOther = Lists.add(list, value);
                if (maybeOther != list) {
                    map.put(key, maybeOther);
                }
            }
        }

        /**
         * Looks for a concrete implementation of <code>intfMethod</code> in
         * <code>implementingType</code>.
         */
        private JMethod findOverloadUsingErasure(JClassType implementingType, JMethod intfMethod) {

            int numParams = intfMethod.getParameters().length;
            JType[] erasedTypes = new JType[numParams];
            for (int i = 0; i < numParams; i++) {
                erasedTypes[i] = intfMethod.getParameters()[i].getType().getErasedType();
            }

            outer:
            for (JMethod method : implementingType.getOverloads(intfMethod.getName())) {
                JParameter[] params = method.getParameters();
                if (params.length != numParams) {
                    continue;
                }
                for (int i = 0; i < numParams; i++) {
                    if (params[i].getType().getErasedType() != erasedTypes[i]) {
                        continue outer;
                    }
                }
                return method;
            }
            return null;
        }
    }

    static final String JAVASCRIPTOBJECT_DESC = JsValueGlue.JSO_CLASS.replace('.', '/');

    static final String JAVASCRIPTOBJECT_IMPL_DESC = JsValueGlue.JSO_IMPL_CLASS.replace('.', '/');

    static String addSyntheticThisParam(String owner, String methodDescriptor) {
        return "(L" + owner + ";" + methodDescriptor.substring(1);
    }

    private static JClassType findImplementingTypeForMethod(JClassType type, JMethod method) {
        JType[] methodParamTypes = method.getErasedParameterTypes();
        while (type != null) {
            for (JMethod candidate : type.getMethods()) {
                if (hasMatchingErasedSignature(method, methodParamTypes, candidate)) {
                    return type;
                }
            }
            type = type.getSuperclass();
        }
        return null;
    }

    private static boolean hasMatchingErasedSignature(JMethod a, JType[] aParamTypes, JMethod b) {
        if (!a.getName().equals(b.getName())) {
            return false;
        }

        JType[] bParamTypes = b.getErasedParameterTypes();
        if (aParamTypes.length != bParamTypes.length) {
            return false;
        }

        for (int i = 0; i < aParamTypes.length; ++i) {
            if (aParamTypes[i] != bParamTypes[i]) {
                return false;
            }
        }

        return true;
    }

    private static String toDescriptor(String jsoSubtype) {
        return jsoSubtype.replace('.', '/');
    }

    private final CompilationState compilationState;

    private final SingleJsoImplData jsoData;
    /**
     * An unmodifiable set of descriptors containing the implementation form of
     * <code>JavaScriptObject</code> and all subclasses.
     */
    private final Set<String> jsoImplDescs;

    /**
     * An unmodifiable set of descriptors containing the interface form of
     * <code>JavaScriptObject</code> and all subclasses.
     */
    private final Set<String> jsoIntfDescs;

    /**
     * Records the superclass of every JSO for generating empty JSO interfaces.
     */
    private final Map<String, List<String>> jsoSuperDescs;

    /**
     * Maps methods to the class in which they are declared.
     */
    private final InstanceMethodOracle mapper;

    private final Set<String> singleJsoImplTypes = new HashSet<>();

    private final TypeOracle typeOracle;

    /**
     * @param compilationState the name of the GWT module under test
     * @param jsoType          the type of JavaScriptObject
     */
    public OverlayTypesRewriter(CompilationState compilationState, JClassType jsoType) {

        // Create a set of binary names.
        Set<JClassType> jsoTypes = new HashSet<>();
        JClassType[] jsoSubtypes = jsoType.getSubtypes();
        Collections.addAll(jsoTypes, jsoSubtypes);
        jsoTypes.add(jsoType);

        Set<String> jsoTypeNames = new HashSet<>();
        Map<String, List<String>> jsoSuperTypes = new HashMap<>();
        for (JClassType type : jsoTypes) {
            List<String> types = new ArrayList<>();
            types.add(getBinaryName(type.getSuperclass()));
            for (JClassType impl : type.getImplementedInterfaces()) {
                types.add(getBinaryName(impl));
            }

            String binaryName = getBinaryName(type);
            jsoTypeNames.add(binaryName);
            jsoSuperTypes.put(binaryName, types);
        }

        Set<String> buildJsoIntfDescs = new HashSet<>();
        Set<String> buildJsoImplDescs = new HashSet<>();
        Map<String, List<String>> buildJsoSuperDescs = new HashMap<>();
        for (String jsoSubtype : jsoTypeNames) {
            String desc = toDescriptor(jsoSubtype);

            buildJsoIntfDescs.add(desc);
            buildJsoImplDescs.add(desc + "$");

            List<String> superTypes = jsoSuperTypes.get(jsoSubtype);
            assert superTypes != null;
            assert superTypes.size() > 0;
            for (ListIterator<String> i = superTypes.listIterator(); i.hasNext(); ) {
                i.set(toDescriptor(i.next()));
            }
            buildJsoSuperDescs.put(desc, Collections.unmodifiableList(superTypes));
        }

        // FIXME: RegExp extends JavaScriptObject at runtime, don't know why...
        String notJsoDesc = toDescriptor("com.google.gwt.regexp.shared.RegExp");
        buildJsoIntfDescs.remove(notJsoDesc);
        buildJsoImplDescs.remove(notJsoDesc);
        buildJsoSuperDescs.remove(notJsoDesc);
        // FIXME: MatchResult extends JavaScriptObject at runtime, don't know
        // why...
        notJsoDesc = toDescriptor("com.google.gwt.regexp.shared.MatchResult");
        buildJsoIntfDescs.remove(notJsoDesc);
        buildJsoImplDescs.remove(notJsoDesc);
        buildJsoSuperDescs.remove(notJsoDesc);
        // FIXME: SplitResult extends JavaScriptObject at runtime, don't know
        // why...
        notJsoDesc = toDescriptor("com.google.gwt.regexp.shared.SplitResult");
        buildJsoIntfDescs.remove(notJsoDesc);
        buildJsoImplDescs.remove(notJsoDesc);
        buildJsoSuperDescs.remove(notJsoDesc);

        this.compilationState = compilationState;
        this.typeOracle = compilationState.getTypeOracle();
        this.jsoIntfDescs = Collections.unmodifiableSet(buildJsoIntfDescs);
        this.jsoImplDescs = Collections.unmodifiableSet(buildJsoImplDescs);
        this.jsoSuperDescs = Collections.unmodifiableMap(buildJsoSuperDescs);
        this.jsoData = new MySingleJsoImplData(typeOracle);
        this.mapper = new MyInstanceMethodOracle(jsoTypes, typeOracle.getJavaLangObject());
    }

    /**
     * Convert a binary class name into a resource-like name.
     */
    public String canonicalizeClassName(String className) {
        String lookupClassName = className.replace('.', '/');
        // A JSO impl class ends with $, strip it
        if (isJsoImpl(className)) {
            lookupClassName = lookupClassName.substring(0, lookupClassName.length() - 1);
        }
        return lookupClassName;
    }

    public CompilationState getCompilationState() {
        return compilationState;
    }

    /**
     * Returns <code>true</code> if the class is the implementation class for a JSO subtype.
     */
    public boolean isJsoImpl(String className) {
        return jsoImplDescs.contains(toDescriptor(className));
    }

    /**
     * Returns <code>true</code> if the class is the interface class for a JSO subtype.
     */
    public boolean isJsoIntf(String className) {
        return jsoIntfDescs.contains(toDescriptor(className));
    }

    /**
     * Performs rewriting transformations on a class.
     *
     * @param className  the name of the class
     * @param classBytes the bytes of the class
     */
    public byte[] rewrite(String className, byte[] classBytes) {
        Event classBytesRewriteEvent = SpeedTracerLogger.start(DevModeEventType.CLASS_BYTES_REWRITE,
                "Class Name", className);
        String desc = toDescriptor(className);
        assert !jsoIntfDescs.contains(desc);

        // The ASM model is to chain a bunch of visitors together.
        ClassWriter writer = new ClassWriter(0);
        ClassVisitor v = writer;

        // v = new CheckClassAdapter(v);
        // v = new TraceClassVisitor(v, new PrintWriter(System.out));

        v = new UseMirroredClasses(v, className);

        v = new RewriteSingleJsoImplDispatches(v, typeOracle, jsoData);

        v = new RewriteRefsToJsoClasses(v, jsoIntfDescs, mapper);

        if (jsoImplDescs.contains(desc)) {
            v = WriteJsoImpl.create(v, desc, jsoIntfDescs, mapper, jsoData);
        }

        if (Double.parseDouble(System.getProperty("java.class.version")) < Opcodes.V1_6) {
            v = new ForceClassVersion15(v);
        }

        new ClassReader(classBytes).accept(v, 0);
        classBytesRewriteEvent.end();
        return writer.toByteArray();
    }

    public byte[] writeJsoIntf(String className) {
        String desc = toDescriptor(className);
        assert jsoIntfDescs.contains(desc);
        assert jsoSuperDescs.containsKey(desc);
        List<String> superDescs = jsoSuperDescs.get(desc);
        assert superDescs != null;
        assert superDescs.size() > 0;

        // The ASM model is to chain a bunch of visitors together.
        ClassWriter writer = new ClassWriter(0);
        ClassVisitor v = writer;

        // v = new CheckClassAdapter(v);
        // v = new TraceClassVisitor(v, new PrintWriter(System.out));

        String[] interfaces;
        // TODO(bov): something better than linear?
        if (superDescs.contains("java/lang/Object")) {
            interfaces = null;
        } else {
            interfaces = superDescs.toArray(new String[superDescs.size()]);
        }
        v.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC | Opcodes.ACC_INTERFACE, desc, null,
                "java/lang/Object", interfaces);
        v.visitEnd();
        return writer.toByteArray();
    }

    private String getBinaryName(JClassType type) {
        String name = type.getPackage().getName() + '.';
        name += type.getName().replace('.', '$');
        return name;
    }

    private String getBinaryOrPrimitiveName(JType type) {
        JArrayType asArray = type.isArray();
        JClassType asClass = type.isClassOrInterface();
        JPrimitiveType asPrimitive = type.isPrimitive();
        if (asClass != null) {
            return getBinaryName(asClass);
        } else if (asPrimitive != null) {
            return asPrimitive.getQualifiedSourceName();
        } else if (asArray != null) {
            JType componentType = asArray.getComponentType();
            return getBinaryOrPrimitiveName(componentType) + "[]";
        } else {
            throw new InternalCompilerException("Cannot create binary name for "
                    + type.getQualifiedSourceName());
        }
    }

}

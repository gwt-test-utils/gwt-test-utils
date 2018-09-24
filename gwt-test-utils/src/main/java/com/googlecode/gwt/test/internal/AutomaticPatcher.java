package com.googlecode.gwt.test.internal;

import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import com.googlecode.gwt.test.patchers.InitMethod;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.patchers.PatchMethod.ParamType;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.Descriptor;
import javassist.bytecode.annotation.AnnotationImpl;
import javassist.bytecode.annotation.StringMemberValue;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * <p>
 * The default class for implementing {@link Patcher} methods. It relies on convenient annotations
 * {@link InitMethod} which marks a method to be a bytecode initialization callback and {link
 * PatchMethod} which marks a method to be a substitution for another one following a simple
 * convention.
 * </p>
 * <p>
 * <strong>For internal use only.</strong>
 * </p>
 *
 * @author Bertrand Paquet
 * @author Gael Lazzari
 */
class AutomaticPatcher implements Patcher {

    private final CtMethod initMethod;
    private final Set<CtMethod> patchMethods;
    private final Set<CtMethod> processedMethods;

    AutomaticPatcher(Set<CtClass> patchClasses) {
        for (CtClass patchClass : patchClasses) {
            patchClass.setModifiers(Modifier.PUBLIC);
        }

        this.initMethod = getInitMethod(patchClasses);
        this.patchMethods = getPatchMethods(patchClasses);
        this.processedMethods = new HashSet<>();
    }

    public void finalizeClass(CtClass c) throws Exception {
        // FIXME : re-activate this code when gwt-bootstrap will work with bootstrap 2.1.1

        // for (CtMethod m : patchMethods) {
        // if (!processedMethods.contains(m)) {
        // throw new GwtTestPatchException("@" + PatchMethod.class.getSimpleName()
        // + " not used : " + m.getLongName());
        // }
        // }
    }

    public String getNewBody(CtMethod m) throws Exception {
        CtMethod patchMethod = findPatchMethod(m);
        if (patchMethod == null) {
            return null;
        }

        processedMethods.add(patchMethod);

        // construction of the redirection code
        StringBuilder buffer = new StringBuilder();
        buffer.append("{");
        buffer.append("return ");
        buffer.append(patchMethod.getDeclaringClass().getName() + "." + patchMethod.getName());
        buffer.append("(");
        boolean append = false;

        if (!Modifier.isStatic(m.getModifiers())) {
            buffer.append("this");
            append = true;
        }
        for (int i = 0; i < m.getParameterTypes().length; i++) {
            if (append) {
                buffer.append(", ");
            }
            int j = i + 1;
            buffer.append("$" + j);
            append = true;
        }
        buffer.append(");");
        buffer.append("}");

        return buffer.toString();
    }

    public void initClass(CtClass c) throws Exception {
        if (initMethod == null) {
            return;
        }

        Class<?> clazz = GwtReflectionUtils.getClass(initMethod.getDeclaringClass().getName());
        Method compiledMethod = clazz.getDeclaredMethod(initMethod.getName(), CtClass.class);
        // TODO : this should not be mandatory since AutomaticPatcher set every
        // initMethod to public when loaded by GwtClassLoader
        GwtReflectionUtils.makeAccessible(compiledMethod);
        try {
            compiledMethod.invoke(null, c);
        } catch (InvocationTargetException e) {
            if (Exception.class.isInstance(e)) {
                throw (Exception) e.getCause();
            } else {
                throw e;
            }
        }
    }

    private String[] extractPatchMethodParameterTypes(CtMethod patchMethod, boolean isStatic)
            throws NotFoundException, ClassNotFoundException {

        CtClass[] patchMethodParamTypes = patchMethod.getParameterTypes();
        int length = isStatic ? patchMethodParamTypes.length : patchMethodParamTypes.length - 1;
        String[] result = new String[length];

        Object[][] parameterAnntations = patchMethod.getParameterAnnotations();

        for (int i = isStatic ? 0 : 1; i < patchMethodParamTypes.length; i++) {
            Object[] annotations = parameterAnntations[i];

            String paramType = null;
            if (annotations.length > 0) {
                paramType = tryExtractFromParamTypeAnnotation(annotations);
            }

            if (paramType == null) {
                // default
                paramType = patchMethodParamTypes[i].getName();
            }

            result[isStatic ? i : i - 1] = paramType;

        }

        return result;
    }

    private CtMethod findPatchMethod(CtMethod m) throws Exception {
        for (CtMethod patchMethod : patchMethods) {
            PatchMethod annotation = (PatchMethod) patchMethod.getAnnotation(PatchMethod.class);
            String methodName = (annotation.value().length() > 0) ? annotation.value()
                    : patchMethod.getName();

            if (m.getName().equals(methodName) && hasCompatibleSignature(m, patchMethod)) {
                return patchMethod;
            }
        }

        return null;
    }

    private CtMethod getInitMethod(Set<CtClass> patchClasses) {
        List<CtMethod> initMethods = new ArrayList<>();

        for (CtClass patchClass : patchClasses) {
            for (CtMethod ctMethod : patchClass.getDeclaredMethods()) {
                if (ctMethod.hasAnnotation(InitMethod.class)) {
                    if (!Modifier.isStatic(ctMethod.getModifiers())) {
                        throw new GwtTestPatchException("@" + InitMethod.class.getSimpleName()
                                + " has to be static : '" + ctMethod.getLongName() + "'");
                    }
                    try {
                        if (ctMethod.getParameterTypes().length != 1
                                || ctMethod.getParameterTypes()[0] != GwtClassPool.getCtClass(CtClass.class)) {
                            throw new GwtTestPatchException("@" + InitMethod.class.getName()
                                    + " method must have one and only one parameter of type '"
                                    + CtClass.class.getName() + "'");
                        }
                    } catch (NotFoundException e) {
                        // should never happen
                        throw new GwtTestPatchException(e);
                    }
                    initMethods.add(ctMethod);
                }
            }
        }

        CtMethod initMethod = getMethodToUse(initMethods, InitMethod.class);
        if (initMethod != null) {
            initMethod.setModifiers(Modifier.PUBLIC + Modifier.STATIC);
        }
        return initMethod;

    }

    @SuppressWarnings("unchecked")
    private <T extends Annotation> T getMethodAnnotation(CtMethod ctMethod, Class<T> annotation) {
        try {
            return (T) ctMethod.getAnnotation(annotation);
        } catch (ClassNotFoundException e) {
            // should never happen
            throw new GwtTestPatchException(e);
        }
    }

    private <T extends Annotation> CtMethod getMethodToUse(List<CtMethod> methods,
                                                           Class<T> annotationClass) {
        switch (methods.size()) {
            case 0:
                return null;
            case 1:
                return methods.get(0);
            default:
                return getOverrideMethod(methods, annotationClass);
        }
    }

    private <T extends Annotation> CtMethod getOverrideMethod(List<CtMethod> methods,
                                                              Class<T> annotationClass) {
        CtMethod overrideInitMethod = null;

        for (CtMethod method : methods) {
            T annotation = getMethodAnnotation(method, annotationClass);
            if (isOverride(annotation)) {
                if (overrideInitMethod != null) {
                    throw new GwtTestPatchException("There are more than one @"
                            + annotationClass.getSimpleName()
                            + " with 'override=true' for the same target : '" + method.getLongName()
                            + "' and '" + overrideInitMethod.getLongName() + "'");
                } else {
                    overrideInitMethod = method;
                }
            }
        }

        if (overrideInitMethod == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(methods.size()).append(" @");
            sb.append(annotationClass.getSimpleName());
            sb.append(" methods detected for the same target, but no one is set to override the other. You must use 'override=true' on the one which should be applied : ");

            for (CtMethod method : methods) {
                sb.append("'").append(method.getLongName()).append("'");
                sb.append(" or ");
            }
            throw new GwtTestPatchException(sb.substring(0, sb.length() - 4));
        }

        return overrideInitMethod;
    }

    private Set<CtMethod> getPatchMethods(Set<CtClass> patchClasses) {
        Set<CtMethod> result = new HashSet<>();

        // add all @PatchMethod found in a temporary map
        Map<String, List<CtMethod>> temp = new HashMap<>();
        for (CtClass patchClass : patchClasses) {
            for (CtMethod ctMethod : patchClass.getDeclaredMethods()) {
                if (ctMethod.hasAnnotation(PatchMethod.class)) {
                    if (!Modifier.isStatic(ctMethod.getModifiers())) {
                        throw new GwtTestPatchException("@" + PatchMethod.class.getName()
                                + " has to be static : '" + ctMethod.getLongName() + "'");
                    }
                    String nameAndSignature = ctMethod.getName()
                            + Descriptor.toString(ctMethod.getSignature());
                    List<CtMethod> correspondingMethods = temp.get(nameAndSignature);

                    if (correspondingMethods == null) {
                        correspondingMethods = new ArrayList<>();
                        temp.put(nameAndSignature, correspondingMethods);
                    }

                    correspondingMethods.add(ctMethod);
                }
            }
        }

        // for each @PatchMethod with the same signature, filter to get one with
        // override=true
        for (Map.Entry<String, List<CtMethod>> entry : temp.entrySet()) {
            CtMethod methodToUse = getMethodToUse(entry.getValue(), PatchMethod.class);
            methodToUse.setModifiers(Modifier.PUBLIC + Modifier.STATIC);
            result.add(methodToUse);
        }

        return result;

    }

    private boolean hasCompatibleSignature(CtMethod methodFound, CtMethod patchMethod)
            throws Exception {

        boolean isStatic = Modifier.isStatic(methodFound.getModifiers());

        if (!isStatic
                && (patchMethod.getParameterTypes().length == 0 || !methodFound.getDeclaringClass().subtypeOf(
                patchMethod.getParameterTypes()[0]))) {

            return false;
        }

        CtClass[] orgMethodParamTypes = methodFound.getParameterTypes();
        String[] patchMethodParamTypes = extractPatchMethodParameterTypes(patchMethod, isStatic);

        if (orgMethodParamTypes.length != patchMethodParamTypes.length) {
            return false;
        }

        for (int i = 0; i < orgMethodParamTypes.length; i++) {
            if (!orgMethodParamTypes[i].getName().equals(patchMethodParamTypes[i])) {
                return false;
            }
        }

        return true;
    }

    private boolean isOverride(Annotation annotation) {
        if (InitMethod.class.isInstance(annotation)) {
            return ((InitMethod) annotation).override();
        } else {
            return ((PatchMethod) annotation).override();
        }
    }

    private String tryExtractFromParamTypeAnnotation(Object[] annotations) {
        for (Object o : annotations) {

            AnnotationImpl annotation = (AnnotationImpl) Proxy.getInvocationHandler(o);
            if (annotation.getTypeName().equals(ParamType.class.getName())) {
                String value = ((StringMemberValue) annotation.getAnnotation().getMemberValue("value")).getValue();

                return value.equals("") ? null : value;
            }
        }

        return null;
    }
}

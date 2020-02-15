package com.googlecode.gwt.test.gin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.inject.rebind.reflect.ReflectUtil;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.*;
import com.google.inject.Module;
import com.google.inject.spi.*;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import com.googlecode.gwt.test.internal.GwtClassPool;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.bytecode.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Additional Guice module class which must be added when replacing a GIN Injector with a Guice
 * Injector, in order to add all required bindings to call GWT's deferred binding fallback which
 * happens in GIN.
 *
 * @author Alex Dobjanschi
 * @author Gael Lazzari
 */
class DeferredBindingModule extends AbstractModule {

    private static class DeferredBindingProvider implements Provider<Object> {

        private final Class<?> clazzToInstanciate;

        public DeferredBindingProvider(Class<? extends Ginjector> ginInjectorClass, Key<?> key) {
            Class<?> rawType = key.getTypeLiteral().getRawType();
            if (rawType.getName().endsWith("Async")) {
                try {
                    this.clazzToInstanciate = GwtReflectionUtils.getClass(rawType.getName().substring(0,
                            rawType.getName().length() - 5));
                } catch (ClassNotFoundException e) {
                    throw new GwtTestPatchException(
                            "Error while trying to create a Guice provider for injector '"
                                    + ginInjectorClass.getName() + "'", e);
                }
            } else {
                this.clazzToInstanciate = rawType;
            }
        }

        public Object get() {
            // call GWT deferred binding, which is patch by gwt-test-utils to call GwtCreateHandlerManager
            return GWT.create(clazzToInstanciate);
        }

    }

    private static final Map<Class<?>, DeferredBindingModule> DEFERRED_BINDING_MODULES_CACHE = new HashMap<>();

    private static final Map<String, Class<?>> GENERATED = new HashMap<>();
    private static final Map<Class<?>, Boolean> HAS_INJECTION_ANNOTATION_CACHE = new HashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(DeferredBindingModule.class);

    static final DeferredBindingModule getDeferredBindingModule(
            Class<? extends Ginjector> ginInjectorClass, Collection<Module> modules) {
        DeferredBindingModule deferredBindingModule = DEFERRED_BINDING_MODULES_CACHE.get(ginInjectorClass);
        if (deferredBindingModule == null) {
            deferredBindingModule = new DeferredBindingModule(ginInjectorClass,
                    modules.toArray(new Module[modules.size()]));
            DEFERRED_BINDING_MODULES_CACHE.put(ginInjectorClass, deferredBindingModule);
        }

        return deferredBindingModule;
    }

    private final Set<Key<?>> bindedClasses;

    private final Set<Key<?>> classesToInstanciate;

    private final Class<? extends Ginjector> ginInjectorClass;

    private DeferredBindingModule(Class<? extends Ginjector> ginInjectorClass, Module[] modules) {

        this.ginInjectorClass = ginInjectorClass;
        List<Element> elements = Elements.getElements(modules);
        this.classesToInstanciate = collectClassesFromInjector(ginInjectorClass);
        this.classesToInstanciate.addAll(collectDependencies(elements));
        this.bindedClasses = collectBindedClasses(elements);
    }

    @Override
    protected void configure() {
        Set<Key<?>> copy = new HashSet<>(bindedClasses);
        addDeferredBindings(classesToInstanciate, copy);

    }

    @SuppressWarnings("unchecked")
    private void addDeferredBinding(final Key<?> toInstanciate, Set<Key<?>> bindedClasses) {
        bindedClasses.add(toInstanciate);

        if (isProviderKey(toInstanciate)) {
            Key<Object> providedKey = (Key<Object>) ReflectUtil.getProvidedKey(toInstanciate);
            if (!bindedClasses.contains(providedKey)) {
                bindedClasses.add(providedKey);
                Set<Key<?>> collected = new HashSet<>();
                collectDependencies(providedKey, collected);
                addDeferredBindings(collected, bindedClasses);
            }
        } else if (isAsyncProviderKey(toInstanciate)) {
            Class<?> asyncProviderClass = getAsyncProvider(toInstanciate);
            bind((Key<Object>) toInstanciate).to(asyncProviderClass);

            Key<Object> providedKey = (Key<Object>) ReflectUtil.getProvidedKey(toInstanciate);
            if (!bindedClasses.contains(providedKey)) {
                bindedClasses.add(providedKey);
                Set<Key<?>> collected = new HashSet<>();
                collectDependencies(providedKey, collected);
                addDeferredBindings(collected, bindedClasses);
            }

        } else if (hasAnyGuiceAnnotation(toInstanciate.getTypeLiteral().getRawType())) {
            // bind to itself, to tell guice there are some injection to proceed although the binding
            // is not declared in the module
            bind(toInstanciate);
        } else {
            // by default use GWT deferred binding to create leaf instances to be injected
            bind((Key<Object>) toInstanciate).toProvider(
                    new DeferredBindingProvider(ginInjectorClass, toInstanciate));
        }

    }

    private void addDeferredBindings(Set<Key<?>> classesToInstanciate, Set<Key<?>> bindedClasses) {
        for (final Key<?> toInstanciate : classesToInstanciate) {
            if (!bindedClasses.contains(toInstanciate)) {
                addDeferredBinding(toInstanciate, bindedClasses);
            }
        }
    }

    private Set<Key<?>> collectBindedClasses(List<Element> elements) {
        final Set<Key<?>> bindedClasses = new HashSet<>();

        for (Element e : elements) {
            e.acceptVisitor(new DefaultElementVisitor<Void>() {
                @Override
                public <T> Void visit(Binding<T> binding) {
                    bindedClasses.add(binding.getKey());
                    return null;
                }
            });
        }

        return bindedClasses;
    }

    private Set<Key<?>> collectClassesFromInjector(Class<?> injectorClass) {

        Set<Key<?>> classesToInstanciate = new HashSet<>();

        for (Method m : injectorClass.getMethods()) {
            if (m.getGenericParameterTypes().length > 0) {
                // This method has non-zero argument list. We cannot do anything
                // about it, so inform developer and continue
                LOGGER.warn("skipping method '" + m.toGenericString()
                        + "' because it has non-zero argument list");
                continue;
            }

            Class<?> literal = m.getReturnType();

            collectDependencies(Key.get(literal), classesToInstanciate);
        }

        return classesToInstanciate;
    }

    private void collectDependencies(Key<?> current, Set<Key<?>> collected) {
        if (collected.contains(current)) {
            return;
        }

        collected.add(current);

        Set<Key<?>> dependencies = getDependencies(current);

        for (Key<?> dependency : dependencies) {
            collectDependencies(dependency, collected);
        }
    }

    private Set<Key<?>> collectDependencies(List<Element> elements) {
        final Set<Key<?>> dependencies = new HashSet<>();
        for (Element e : elements) {
            e.acceptVisitor(new DefaultElementVisitor<Void>() {
                @Override
                public <T> Void visit(Binding<T> binding) {
                    LOGGER.debug("visiting binding " + binding.toString());

                    if (binding instanceof HasDependencies) {
                        HasDependencies deps = (HasDependencies) binding;
                        for (Dependency<?> d : deps.getDependencies()) {
                            collectDependencies(d.getKey(), dependencies);
                        }
                    } else {
                        collectDependencies(binding.getKey(), dependencies);
                        dependencies.addAll(getDependencies(binding.getKey()));
                    }

                    return null;
                }
            });
        }

        return dependencies;
    }

    @SuppressWarnings("unchecked")
    private Class<?> generatedAsyncProvider(String className, Key<?> providedKey) {

        CtClass providedCtClass = GwtClassPool.getCtClass(providedKey.getTypeLiteral().getRawType());

        CtClass c = GwtClassPool.get().makeClass(className);
        c.addInterface(GwtClassPool.getCtClass(AsyncProvider.class));

        try {
            ClassFile classFile = c.getClassFile();
            classFile.setVersionToJava5();
            ConstPool constantPool = classFile.getConstPool();

            CtField provider = CtField.make("private " + Provider.class.getName() + " provider;", c);
            c.addField(provider);
            FieldInfo fieldInfo = provider.getFieldInfo();

            // Make it generic
            SignatureAttribute signatureAttribute = new SignatureAttribute(fieldInfo.getConstPool(),
                    "Lcom/google/inject/Provider<" + Descriptor.of(providedCtClass) + ">;");
            fieldInfo.addAttribute(signatureAttribute);

            AnnotationsAttribute attr = new AnnotationsAttribute(constantPool,
                    AnnotationsAttribute.visibleTag);
            javassist.bytecode.annotation.Annotation a = new javassist.bytecode.annotation.Annotation(
                    Inject.class.getName(), constantPool);
            attr.setAnnotation(a);
            provider.getFieldInfo().addAttribute(attr);

            CtMethod get = CtMethod.make("public void get(" + AsyncCallback.class.getName()
                    + " callback) { callback.onSuccess(provider.get()); }", c);
            c.addMethod(get);

            return c.toClass();
        } catch (CannotCompileException e) {
            throw new GwtTestGinException("Error while creating AsyncProvider subclass [" + className
                    + "]", e);
        }

    }

    private Class<?> getAsyncProvider(Key<?> key) {

        Key<?> providedKey = ReflectUtil.getProvidedKey(key);
        String className = providedKey.getTypeLiteral().getRawType().getName() + "AsyncProvider";

        Class<?> clazz = GENERATED.get(className);

        if (clazz != null) {
            return clazz;
        }

        clazz = generatedAsyncProvider(className, providedKey);
        GENERATED.put(className, clazz);

        return clazz;
    }

    private Set<Key<?>> getDependencies(InjectionPoint point) {
        Set<Key<?>> dependencies = new HashSet<>();
        for (Dependency<?> d1 : point.getDependencies()) {
            dependencies.add(d1.getKey());
        }

        return dependencies;
    }

    private Set<Key<?>> getDependencies(Key<?> clazz) {

        Set<Key<?>> dependencies = new HashSet<>();

        if (clazz.getTypeLiteral().getRawType().isInterface()) {
            dependencies.add(clazz);
            return dependencies;
        }

        try {
            dependencies.addAll(getDependencies(InjectionPoint.forConstructorOf(clazz.getTypeLiteral())));
        } catch (ConfigurationException e) {
            // nothing to do
        }

        for (InjectionPoint point : InjectionPoint.forInstanceMethodsAndFields(clazz.getTypeLiteral())) {
            dependencies.addAll(getDependencies(point));
        }

        for (InjectionPoint point : InjectionPoint.forStaticMethodsAndFields(clazz.getTypeLiteral())) {
            dependencies.addAll(getDependencies(point));
        }

        return dependencies;

    }

    private boolean hasAnyGuiceAnnotation(Class<?> toInstanciate) {
        Boolean hasAnyGuiceAnnotation = HAS_INJECTION_ANNOTATION_CACHE.get(toInstanciate);
        if (hasAnyGuiceAnnotation != null) {
            return hasAnyGuiceAnnotation;
        }

        if (GwtReflectionUtils.getAnnotation(toInstanciate, Singleton.class) != null) {
            hasAnyGuiceAnnotation = true;
        } else if (GwtReflectionUtils.getAnnotation(toInstanciate, ProvidedBy.class) != null) {
            hasAnyGuiceAnnotation = true;
        } else if (hasInjectAnnotatedConstructor(toInstanciate)) {
            hasAnyGuiceAnnotation = true;
        } else {
            hasAnyGuiceAnnotation = GwtReflectionUtils.getAnnotatedField(toInstanciate, Inject.class).size() > 0;
        }

        HAS_INJECTION_ANNOTATION_CACHE.put(toInstanciate, hasAnyGuiceAnnotation);

        return hasAnyGuiceAnnotation;
    }

    private boolean hasInjectAnnotatedConstructor(Class<?> toInstanciate) {
        for (Constructor<?> cons : toInstanciate.getDeclaredConstructors()) {
            if (cons.getAnnotation(Inject.class) != null) {
                return true;
            }
        }
        return false;
    }

    private boolean isAsyncProviderKey(Key<?> key) {
        Type keyType = key.getTypeLiteral().getType();
        return keyType instanceof ParameterizedType
                && ((ParameterizedType) keyType).getRawType() == AsyncProvider.class;
    }

    private boolean isProviderKey(Key<?> key) {
        Type keyType = key.getTypeLiteral().getType();
        return keyType instanceof ParameterizedType
                && (((ParameterizedType) keyType).getRawType() == Provider.class || ((ParameterizedType) keyType).getRawType() == com.google.inject.Provider.class);
    }
}

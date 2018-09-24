package com.googlecode.gwt.test.utils;

import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import com.googlecode.gwt.test.internal.GwtClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.AnnotationImpl;
import javassist.bytecode.annotation.StringMemberValue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class over the <code>javassist</code> API.
 *
 * @author Gael Lazzari
 */
public class JavassistUtils {

    public static CtConstructor findConstructor(CtClass ctClass, Class<?>... argsClasses) {

        Set<CtConstructor> set = new HashSet<>();

        findConstructors(ctClass, set, argsClasses);

        switch (set.size()) {
            case 0:
                return null;
            case 1:
                return set.iterator().next();
            default:
                throw new GwtTestPatchException("Multiple constructors (" + set.size() + ") in class "
                        + ctClass.getName() + ", you have to set parameter types discriminators");
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Annotation> javassist.bytecode.annotation.Annotation getAnnotation(
            CtClass ctClass, Class<T> annotationClass) throws ClassNotFoundException {

        T proxiedAnnot = (T) ctClass.getAnnotation(annotationClass);

        if (proxiedAnnot == null) {
            return null;
        } else if (!Proxy.isProxyClass(proxiedAnnot.getClass())) {
            return null;
        }

        AnnotationImpl impl = (AnnotationImpl) Proxy.getInvocationHandler(proxiedAnnot);

        return impl.getAnnotation();
    }

    /**
     * Retrieve the String value of an annotation which is not available at runtime.
     *
     * @param clazz      The annotated class
     * @param annotation The annotation which is not visible at runtime
     * @param name       The name of the String property of the annotation to retrieve
     * @return The String value of the annotation or null if the annotation or its property is not
     * present
     */
    public static String getInvisibleAnnotationStringValue(Class<?> clazz,
                                                           Class<? extends Annotation> annotation, String name) {
        CtClass ctClass = GwtClassPool.getCtClass(clazz);
        ctClass.defrost();

        AnnotationsAttribute attr = (AnnotationsAttribute) ctClass.getClassFile().getAttribute(
                AnnotationsAttribute.visibleTag);
        if (attr == null) {
            attr = (AnnotationsAttribute) ctClass.getClassFile().getAttribute(
                    AnnotationsAttribute.invisibleTag);
        }
        if (attr == null) {
            return null;
        }

        javassist.bytecode.annotation.Annotation an = attr.getAnnotation(annotation.getName());

        ctClass.freeze();

        return an != null ? ((StringMemberValue) an.getMemberValue(name)).getValue() : null;
    }

    /**
     * Retrieve the String value of an annotation which is not available at runtime.
     *
     * @param method     The annotated method
     * @param annotation The annotation which is not visible at runtime
     * @param name       The name of the String property of the annotation to retrieve
     * @return The String value of the annotation or null if the annotation or its property is not
     * present
     */
    public static String getInvisibleAnnotationStringValue(Method method,
                                                           Class<? extends Annotation> annotation, String name) {
        CtClass ctClass = GwtClassPool.getCtClass(method.getDeclaringClass());
        ctClass.defrost();

        AnnotationsAttribute attr = (AnnotationsAttribute) ctClass.getClassFile().getMethod(
                method.getName()).getAttribute(AnnotationsAttribute.visibleTag);
        if (attr == null) {
            attr = (AnnotationsAttribute) ctClass.getClassFile().getMethod(method.getName()).getAttribute(
                    AnnotationsAttribute.invisibleTag);
        }
        if (attr == null) {
            return null;
        }
        javassist.bytecode.annotation.Annotation an = attr.getAnnotation(annotation.getName());

        ctClass.freeze();

        return an != null ? ((StringMemberValue) an.getMemberValue(name)).getValue() : null;
    }

    private static void findConstructors(CtClass ctClass, Set<CtConstructor> set,
                                         Class<?>... argsClasses) {
        try {

            if (ctClass == null) {
                return;
            }

            CtConstructor[] constructors = ctClass.getDeclaredConstructors();

            if (constructors.length == 0) {
                findConstructors(ctClass.getSuperclass(), set, argsClasses);
            } else if (constructors.length == 1 && argsClasses.length == 0) {
                set.add(constructors[0]);
            } else {
                for (CtConstructor c : constructors) {

                    if (c.getParameterTypes().length != argsClasses.length) {
                        continue;
                    }

                    boolean sameArgs = true;
                    for (int i = 0; i < argsClasses.length; i++) {
                        String requestedClassName = argsClasses[i].getName();
                        String currentClassName = c.getParameterTypes()[i].getName();

                        if (!requestedClassName.equals(currentClassName)) {
                            sameArgs = false;
                        }
                    }

                    if (sameArgs) {
                        set.add(c);
                    }
                }
            }
        } catch (NotFoundException e) {
            // should never happen
            throw new GwtTestPatchException("Error while trying find a constructor in class '"
                    + ctClass.getName() + "'", e);
        }
    }

    private JavassistUtils() {

    }

}

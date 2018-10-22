package com.googlecode.gwt.test.utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.web.bindery.event.shared.UmbrellaException;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.exceptions.ReflectionException;
import com.googlecode.gwt.test.internal.utils.DoubleMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * Class which provides reflection utilities.
 *
 * @author Bertrand Paquet
 * @author Gael Lazzari
 */
@SuppressWarnings("unchecked")
public class GwtReflectionUtils {

    /**
     * Callback interface invoked on each field in the hierarchy.
     */
    public interface FieldCallback {

        /**
         * Perform an operation using the given field.
         *
         * @param field the field to operate on
         */
        void doWith(Field field) throws IllegalArgumentException, IllegalAccessException;
    }

    /**
     * Callback optionally used to filter fields to be operated on by a field callback.
     */
    public interface FieldFilter {

        /**
         * Determine whether the given field matches.
         *
         * @param field the field to check
         */
        boolean matches(Field field);
    }

    /**
     * Action to take on each method.
     */
    public interface MethodCallback {

        /**
         * Perform an operation using the given method.
         *
         * @param method the method to operate on
         */
        void doWith(Method method) throws IllegalArgumentException, IllegalAccessException;
    }

    /**
     * Callback optionally used to method fields to be operated on by a method callback.
     */
    public interface MethodFilter {

        /**
         * Determine whether the given method matches.
         *
         * @param method the method to check
         */
        boolean matches(Method method);
    }

    private static final String ASSERTIONS_FIELD_NAME = "$assertionsDisabled";

    private static DoubleMap<Class<?>, Class<?>, Map<Field, ?>> cacheAnnotatedField = new DoubleMap<>();
    private static DoubleMap<Class<?>, Class<?>, Map<Method, ?>> cacheAnnotatedMethod = new DoubleMap<>();

    private static DoubleMap<Class<?>, Class<?>, Object> cacheAnnotation = new DoubleMap<Class<?>, Class<?>, Object>();

    private static Map<Class<?>, Set<Field>> cacheField = new HashMap<>();

    private static DoubleMap<Class<?>, String, Method> cacheMethod = new DoubleMap<>();

    private static DoubleMap<Class<?>, String, Field> cacheUniqueField = new DoubleMap<>();

    public static <T> T callPrivateMethod(JavaScriptObject target, String methodName,
                                          String overlayOriginalType, Object... args) {
        if (overlayOriginalType == null) {
            throw new IllegalArgumentException("The specified overlay type should not be null");
        }

        overlayOriginalType = overlayOriginalType.trim();
        if (overlayOriginalType.equals("")) {
            throw new IllegalArgumentException("The specified overlay type should not be blank");
        }

        if (overlayOriginalType.endsWith("$")) {
            throw new IllegalArgumentException(
                    "The specified overlay type should not be a rewritten type : "
                            + overlayOriginalType);
        }

        Class<?> rewrittenOverlayType = null;
        try {
            rewrittenOverlayType = Class.forName(overlayOriginalType + "$");
        } catch (ClassNotFoundException e) {
            throw new ReflectionException("Error while calling overlay rewritten method "
                    + overlayOriginalType + "$." + methodName + "$(..) :", e);
        }

        Object[] newArgs = new Object[args.length + 1];
        newArgs[0] = target;

        System.arraycopy(args, 0, newArgs, 1, args.length);

        return callStaticMethod(rewrittenOverlayType, methodName + "$", newArgs);
    }

    public static <T> T callPrivateMethod(Object target, Method method, Object... args) {
        try {
            method.setAccessible(true);
            Object res = method.invoke(target, args);
            return (T) res;
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof GwtTestException) {
                throw (GwtTestException) e.getCause();
            } else if (e.getCause() instanceof AssertionError) {
                throw (AssertionError) e.getCause();
            } else if (e.getCause() instanceof UmbrellaException) {
                throw new ReflectionException("Error while calling method '" + method.toString() + "'",
                        e.getCause().getCause());
            }
            throw new ReflectionException("Error while calling method '" + method.toString() + "'",
                    e.getCause());
        } catch (Exception e) {
            throw new ReflectionException("Unable to call method '"
                    + target.getClass().getSimpleName() + "." + method.getName() + "(..)'", e);
        }
    }

    public static <T> T callPrivateMethod(Object target, String methodName, Object... args) {
        if (target instanceof JavaScriptObject) {
            throw new UnsupportedOperationException(
                    "Cannot call instance method on Overlay types without specifying its base type");
        }

        Method method = findMethod(target.getClass(), methodName, args);
        if (method == null) {
            throw new ReflectionException("Cannot find method '" + target.getClass().getName() + "."
                    + methodName + "(..)'");
        }
        return (T) callPrivateMethod(target, method, args);

    }

    public static <T> T callStaticMethod(Class<?> clazz, String methodName, Object... args) {

        Method m = findMethod(clazz, methodName, args);
        if (m == null) {
            throw new ReflectionException("Cannot find method '" + clazz.getName() + "." + methodName
                    + "(..)'");
        }
        try {
            m.setAccessible(true);
            Object res = m.invoke(null, args);
            return (T) res;
        } catch (Exception e) {
            throw new ReflectionException("Unable to call static method '" + m.toString() + "'", e);
        }
    }

    /**
     * Invoke the given callback on all fields in the target class, going up the class hierarchy to
     * get all declared fields.
     *
     * @param clazz the target class to analyze
     * @param fc    the callback to invoke for each field
     */
    public static void doWithFields(Class<?> clazz, FieldCallback fc)
            throws IllegalArgumentException {
        doWithFields(clazz, fc, null);
    }

    /**
     * Invoke the given callback on all fields in the target class, going up the class hierarchy to
     * get all declared fields.
     *
     * @param clazz the target class to analyze
     * @param fc    the callback to invoke for each field
     * @param ff    the filter that determines the fields to apply the callback to
     */
    public static void doWithFields(Class<?> clazz, FieldCallback fc, FieldFilter ff)
            throws IllegalArgumentException {

        // Keep backing up the inheritance hierarchy.
        Class<?> targetClass = clazz;
        do {
            Field[] fields = targetClass.getDeclaredFields();
            for (Field field : fields) {
                // Skip static and final fields.
                if (ff != null && !ff.matches(field)) {
                    continue;
                }
                try {
                    fc.doWith(field);
                } catch (IllegalAccessException ex) {
                    throw new IllegalStateException("Shouldn't be illegal to access field '"
                            + field.getName() + "': " + ex);
                }
            }
            targetClass = targetClass.getSuperclass();
        } while (targetClass != null && targetClass != Object.class);
    }

    /**
     * Perform the given callback operation on all matching methods of the given class and
     * superclasses.
     * <p>
     * The same named method occurring on subclass and superclass will appear twice, unless excluded
     * by a {@link MethodFilter}.
     *
     * @param clazz class to start looking at
     * @param mc    the callback to invoke for each method
     * @see #doWithMethods(Class, MethodCallback, MethodFilter)
     */
    public static void doWithMethods(Class<?> clazz, MethodCallback mc)
            throws IllegalArgumentException {
        doWithMethods(clazz, mc, null);
    }

    /**
     * Perform the given callback operation on all matching methods of the given class and
     * superclasses (or given interface and super-interfaces).
     * <p>
     * The same named method occurring on subclass and superclass will appear twice, unless excluded
     * by the specified {@link MethodFilter}.
     *
     * @param clazz class to start looking at
     * @param mc    the callback to invoke for each method
     * @param mf    the filter that determines the methods to apply the callback to
     */
    public static void doWithMethods(Class<?> clazz, MethodCallback mc, MethodFilter mf)
            throws IllegalArgumentException {

        // Keep backing up the inheritance hierarchy.
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (mf != null && !mf.matches(method)) {
                continue;
            }
            try {
                mc.doWith(method);
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException("Shouldn't be illegal to access method '"
                        + method.getName() + "': " + ex);
            }
        }
        if (clazz.getSuperclass() != null) {
            doWithMethods(clazz.getSuperclass(), mc, mf);
        } else if (clazz.isInterface()) {
            for (Class<?> superIfc : clazz.getInterfaces()) {
                doWithMethods(superIfc, mc, mf);
            }
        }
    }

    public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
        Class<?> searchType = clazz;
        while (!Object.class.equals(searchType) && searchType != null) {
            Method[] methods = searchType.isInterface() ? searchType.getMethods()
                    : searchType.getDeclaredMethods();
            for (Method method : methods) {
                if (name.equals(method.getName())
                        && paramTypes.length == method.getParameterTypes().length) {
                    boolean compatibleParams = true;
                    for (int j = 0; j < paramTypes.length; j++) {

                        if (paramTypes[j] == null) {
                            // null class is a wildcard
                            continue;
                        }

                        Class<?> methodParamType = getCheckedClass(method.getParameterTypes()[j]);
                        Class<?> searchParamType = getCheckedClass(paramTypes[j]);

                        if (!methodParamType.isAssignableFrom(searchParamType)) {
                            compatibleParams = false;
                        }
                    }

                    if (compatibleParams) {
                        return method;

                    }
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    /**
     * Get all declared methods on the leaf class and all superclasses. Leaf class methods are
     * included first.
     */
    public static Method[] getAllDeclaredMethods(Class<?> leafClass) throws IllegalArgumentException {
        final List<Method> methods = new ArrayList<>(32);
        doWithMethods(leafClass, methods::add);
        return methods.toArray(new Method[methods.size()]);
    }

    public static <T extends Annotation> Map<Field, T> getAnnotatedField(Class<?> clazz,
                                                                         Class<T> annotationClass) {
        Map<Field, T> l = (Map<Field, T>) cacheAnnotatedField.get(clazz, annotationClass);
        if (l != null) {
            return l;
        }
        l = new HashMap<>();
        recurseGetAnnotatedField(l, clazz, annotationClass);
        cacheAnnotatedField.put(clazz, annotationClass, l);
        return l;
    }

    public static <T extends Annotation> Map<Method, T> getAnnotatedMethod(Class<?> target,
                                                                           Class<T> annotationClass) {
        Map<Method, T> map = (Map<Method, T>) cacheAnnotatedMethod.get(target, annotationClass);
        if (map != null) {
            return map;
        }
        map = new HashMap<>();
        recurseGetAnnotatedMethod(map, target, annotationClass);
        cacheAnnotatedMethod.put(target, annotationClass, map);
        return map;
    }

    public static <T> T getAnnotation(Class<?> clazz, Class<T> annotationClass) {

        Object o = cacheAnnotation.get(clazz, annotationClass);
        if (o != null) {
            return (T) o;
        }

        T result = null;
        for (Annotation a : clazz.getDeclaredAnnotations()) {
            if (a.annotationType() == annotationClass) {
                result = (T) a;
            }
        }
        if (result == null && clazz.getSuperclass() != null) {
            result = getAnnotation(clazz.getSuperclass(), annotationClass);
        }

        cacheAnnotation.put(clazz, annotationClass, result);

        return result;
    }

    /**
     * Get the class corresponding to the String passed as param. This method is better than
     * {@link Class#forName(String)} because it can handle inner class when necessary.
     *
     * @param type The class name
     * @return The corresponding class object
     * @throws ClassNotFoundException If the class does not exist
     */
    public static Class<?> getClass(String type) throws ClassNotFoundException {
        try {
            return Class.forName(type);
        } catch (ClassNotFoundException e1) {
            // it can be an inner class
            int lastIndex = type.lastIndexOf('.');
            String innerTypeName = type.substring(0, lastIndex) + "$" + type.substring(lastIndex + 1);
            try {
                return Class.forName(innerTypeName);
            } catch (ClassNotFoundException e2) {
                throw e1;
            }
        }
    }

    /**
     * Get all fields from a class and its superclasses, being carefull not to add multiple
     * '$assertionsDisabled' fields which are added when a class contains java assertions.
     *
     * @param clazz The class to introspect.
     * @return The set of field of the class
     */
    public static Set<Field> getFields(Class<?> clazz) {
        Set<Field> set = cacheField.get(clazz);
        if (set != null) {
            return set;
        }

        // get all field, being carefull not to add multiple $assertionsDisabled
        // fields
        set = getFields(clazz, false);

        cacheField.put(clazz, set);

        return set;
    }

    public static Method getMethod(Class<?> clazz, String methodName) {
        Method res = cacheMethod.get(clazz, methodName);
        if (res != null) {
            return res;
        }
        for (Method m : clazz.getDeclaredMethods()) {
            if (methodName.equalsIgnoreCase(m.getName())) {
                m.setAccessible(true);
                cacheMethod.put(clazz, methodName, m);
                return m;
            }
        }
        for (Method m : clazz.getMethods()) {
            if (methodName.equalsIgnoreCase(m.getName())) {
                m.setAccessible(true);
                cacheMethod.put(clazz, methodName, m);
                return m;
            }
        }
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            res = getMethod(superClazz, methodName);
        }
        cacheMethod.put(clazz, methodName, res);
        return res;
    }

    public static Method getMethod(Class<?> clazz, String methodName, int paramLenght) {
        Method method = getMethod(clazz, methodName);
        if (method != null && method.getParameterTypes().length == paramLenght) {
            return method;
        } else if (method != null) {
            for (Method m : clazz.getDeclaredMethods()) {
                if (methodName.equalsIgnoreCase(m.getName()) && m.getParameterTypes().length == paramLenght) {
                    m.setAccessible(true);
                    return m;
                }
            }
        }
        return null;
    }

    public static <T> T getPrivateFieldValue(Object target, Field field) {
        try {
            makeAccessible(field);
            return (T) field.get(target);
        } catch (Exception e) {
            throw new ReflectionException("Unable to get field '" + target.getClass().getSimpleName()
                    + "." + field.getName() + "'", e);
        }
    }

    public static <T> T getPrivateFieldValue(Object target, String fieldName) {
        Field field = getUniqueFieldByName(target.getClass(), fieldName);
        return (T) getPrivateFieldValue(target, field);
    }

    public static <T> T getStaticFieldValue(Class<?> clazz, String fieldName) {
        Field field = getUniqueFieldByName(clazz, fieldName);
        try {
            return (T) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ReflectionException(e.getMessage() + " Unable to get static field, class "
                    + fieldName + ", fieldClass " + clazz);
        }
    }

    /**
     * Convenience method to instantiate a class using its no-arg constructor. As this method doesn't
     * try to load classes by name, it should avoid class-loading issues.
     * <p>
     * Note that this method tries to set the constructor accessible if given a non-accessible (that
     * is, non-public) constructor.
     *
     * @param <T>   the type of object to instanciate
     * @param clazz class to instantiate
     * @return the new instance
     */
    public static <T> T instantiateClass(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class must not be null");
        }
        if (clazz.isInterface()) {
            throw new ReflectionException("Error during instanciation of '" + clazz.getName()
                    + "'. Specified class is an interface");
        }
        try {
            return instantiateClass(clazz.getDeclaredConstructor());
        } catch (NoSuchMethodException ex) {
            throw new ReflectionException("Error during instanciation of '" + clazz.getName()
                    + "'. No default constructor found", ex);
        }
    }

    public static <T> T instantiateClass(String className) {
        try {
            Class<T> clazz = (Class<T>) Class.forName(className);
            return instantiateClass(clazz);
        } catch (ClassNotFoundException e) {
            throw new GwtTestException("Error while trying to get class " + className, e);
        }
    }

    /**
     * Convenience method to instantiate a class using the given constructor. As this method doesn't
     * try to load classes by name, it should avoid class-loading issues.
     * <p>
     * Note that this method tries to set the constructor accessible if given a non-accessible (that
     * is, non-public) constructor.
     *
     * @param <T>  The object type
     * @param ctor the constructor to instantiate
     * @param args the constructor arguments to apply
     * @return the new instance
     */
    public static <T> T instantiateClass(Constructor<T> ctor, Object... args) {
        if (ctor == null) {
            throw new IllegalArgumentException("Constructor must not be null");
        }

        try {
            makeAccessible(ctor);
            return ctor.newInstance(args);
        } catch (InstantiationException ex) {
            throw new ReflectionException("Error during instanciation of '"
                    + ctor.getDeclaringClass().getName() + "'. Is it an abstract class?", ex);
        } catch (IllegalAccessException ex) {
            throw new ReflectionException("Error during instanciation of '"
                    + ctor.getDeclaringClass().getName()
                    + "'. Has the class definition changed? Is the constructor accessible?", ex);
        } catch (IllegalArgumentException ex) {
            throw new ReflectionException("Error during instanciation of '"
                    + ctor.getDeclaringClass().getName() + "'. Illegal arguments for constructor", ex);
        } catch (InvocationTargetException ex) {
            if (ex.getTargetException() instanceof GwtTestException) {
                throw (GwtTestException) ex.getTargetException();
            } else if (ex.getTargetException().getCause() instanceof GwtTestException) {
                throw (GwtTestException) ex.getTargetException().getCause();
            } else {
                throw new ReflectionException("Error during instanciation of '"
                        + ctor.getDeclaringClass().getName() + "'. Constructor threw exception",
                        ex.getTargetException());
            }
        }

    }

    /**
     * Make the given constructor accessible, explicitly setting it accessible if necessary. The
     * <code>setAccessible(true)</code> method is only called when actually necessary, to avoid
     * unnecessary conflicts with a JVM SecurityManager (if active).
     *
     * @param ctor the constructor to make accessible
     * @see java.lang.reflect.Constructor#setAccessible
     */
    public static void makeAccessible(Constructor<?> ctor) {
        if (!Modifier.isPublic(ctor.getModifiers())
                || !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) {
            ctor.setAccessible(true);
        }
    }

    /**
     * Make the given field accessible, explicitly setting it accessible if necessary. The
     * <code>setAccessible(true)</code> method is only called when actually necessary, to avoid
     * unnecessary conflicts with a JVM SecurityManager (if active).
     *
     * @param field the field to make accessible
     * @see java.lang.reflect.Field#setAccessible
     */
    public static void makeAccessible(Field field) {
        if (!Modifier.isPublic(field.getModifiers())
                || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

    /**
     * Make the given method accessible, explicitly setting it accessible if necessary. The
     * <code>setAccessible(true)</code> method is only called when actually necessary, to avoid
     * unnecessary conflicts with a JVM SecurityManager (if active).
     *
     * @param method the method to make accessible
     * @see java.lang.reflect.Method#setAccessible
     */
    public static void makeAccessible(Method method) {
        if (!Modifier.isPublic(method.getModifiers())
                || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
            method.setAccessible(true);
        }
    }

    public static void setPrivateFieldValue(Object target, String fieldName, Object value) {
        Field field = getUniqueFieldByName(target.getClass(), fieldName);
        try {
            field.set(target, value);
        } catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    public static void setStaticField(Class<?> clazz, String fieldName, Object value) {
        Field field = getUniqueFieldByName(clazz, fieldName);
        try {
            field.set(null, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ReflectionException(e);
        }
    }

    private static Method findMethod(Class<?> clazz, String methodName, Object... args) {
        Class<?>[] l = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            l[i] = args[i] != null ? args[i].getClass() : null;
        }
        return findMethod(clazz, methodName, l);
    }

    private static Class<?> getCheckedClass(Class<?> potentialPrimitiveType) {
        if (!potentialPrimitiveType.isPrimitive()) {
            return potentialPrimitiveType;
        }

        if (potentialPrimitiveType == Byte.TYPE) {
            return Byte.class;
        } else if (potentialPrimitiveType == Short.TYPE) {
            return Short.class;
        } else if (potentialPrimitiveType == Integer.TYPE) {
            return Integer.class;
        } else if (potentialPrimitiveType == Long.TYPE) {
            return Long.class;
        } else if (potentialPrimitiveType == Float.TYPE) {
            return Float.class;
        } else if (potentialPrimitiveType == Double.TYPE) {
            return Double.class;
        } else if (potentialPrimitiveType == Boolean.TYPE) {
            return Boolean.class;
        } else {
            return Character.class;
        }
    }

    /**
     * Get all fields from a class, being carefull not to add multiple '$assertionsDisabled' fields
     * which are added when a class contains java assertions.
     *
     * @param clazz             The class to introspect.
     * @param hasAssertionField A token to remember if an '$assertionDisabled' field had already been
     *                          added to the class.
     * @return The set of field of the class
     */
    private static Set<Field> getFields(Class<?> clazz, boolean hasAssertionField) {
        Set<Field> set = new HashSet<>();

        for (Field f : clazz.getDeclaredFields()) {
            if (ASSERTIONS_FIELD_NAME.equals(f.getName())) {

                // do not add this assertion specific field if it has already been
                // added
                // by a child class
                if (!hasAssertionField) {
                    f.setAccessible(true);
                    set.add(f);
                    hasAssertionField = true;
                }
            } else {
                f.setAccessible(true);
                set.add(f);
            }
        }

        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            set.addAll(getFields(superClazz, hasAssertionField));
        }

        return set;
    }

    private static Field getUniqueFieldByName(Class<?> clazz, String fieldName) {
        Field f = cacheUniqueField.get(clazz, fieldName);
        if (f != null) {
            return f;
        }
        Set<Field> fieldSet = getFields(clazz);

        Set<Field> result = new HashSet<>();

        for (Field field : fieldSet) {
            if (field.getName().equals(fieldName)) {
                result.add(field);
            }
        }

        if (result.size() == 0) {
            throw new ReflectionException("Unable to find field, class '" + clazz + "', fieldName '"
                    + fieldName + "'");
        }
        if (result.size() > 1) {
            throw new ReflectionException("Unable to choose field, '" + clazz + "', fieldName '"
                    + fieldName + "'");
        }
        Field field = result.iterator().next();
        cacheUniqueField.put(clazz, fieldName, field);
        return field;
    }

    private static <T extends Annotation> void recurseGetAnnotatedField(Map<Field, T> map,
                                                                        Class<?> target, Class<?> annotationClass) {
        for (Field f : target.getDeclaredFields()) {
            for (Annotation a : f.getDeclaredAnnotations()) {
                if (a.annotationType() == annotationClass) {
                    map.put(f, (T) a);
                }
            }
        }
        if (target.getSuperclass() != null) {
            recurseGetAnnotatedField(map, target.getSuperclass(), annotationClass);
        }
    }

    private static <T extends Annotation> void recurseGetAnnotatedMethod(Map<Method, T> map,
                                                                         Class<?> target, Class<?> annotationClass) {
        for (Method m : target.getDeclaredMethods()) {
            for (Annotation a : m.getDeclaredAnnotations()) {
                if (a.annotationType() == annotationClass) {
                    map.put(m, (T) a);
                }
            }
        }
        if (target.getSuperclass() != null) {
            recurseGetAnnotatedMethod(map, target.getSuperclass(), annotationClass);
        }
    }

    private GwtReflectionUtils() {
    }

}

package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.UIObject;
import com.googlecode.gwt.test.exceptions.GwtTestUiBinderException;
import com.googlecode.gwt.test.internal.GwtParanamer;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class responsible for Object instantiations. It handles provided {@link UiField},
 * {@link UiFactory} fields and {@link UiConstructor} fields.
 *
 * @author Gael Lazzari
 */
@SuppressWarnings("unchecked")
class UiBinderInstanciator {
    public final static Map<Class<?>, Class<?>> primitiveMap = new HashMap<>();

    static {
        primitiveMap.put(boolean.class, Boolean.class);
        primitiveMap.put(byte.class, Byte.class);
        primitiveMap.put(short.class, Short.class);
        primitiveMap.put(char.class, Character.class);
        primitiveMap.put(int.class, Integer.class);
        primitiveMap.put(long.class, Long.class);
        primitiveMap.put(float.class, Float.class);
        primitiveMap.put(double.class, Double.class);
    }

    static <U> U getInstance(Class<U> clazz, Map<String, Object> attributes, Object owner) {

        String uiFieldValue = (String) attributes.get("ui:field");
        U instance = getProvidedUiField(clazz, owner, uiFieldValue);

        if (instance == null) {
            instance = getObjectFromUiFactory(clazz, owner);
        }

        if (instance == null) {
            instance = getObjectFromUiConstructor(clazz, attributes);
        }

        if (instance == null && !UIObject.class.isAssignableFrom(clazz)
                && !IsWidget.class.isAssignableFrom(clazz)) {
            instance = GWT.<U>create(clazz);
        }

        return instance;
    }

    private static boolean allArgsAreDeclaredInUiFile(String[] argNames,
                                                      Map<String, Object> attributes) {
        for (String argName : argNames) {
            if (!attributes.containsKey(argName)) {
                return false;
            }
        }
        return true;
    }

    private static Object convertArgs(Class<?> paramType, Object object)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (paramType == String.class) {
            return object;
        }

        if (paramType.isPrimitive()) {
            paramType = primitiveMap.get(paramType);
        }

        Method valueOfMethod = null;
        try {
            valueOfMethod = paramType.getDeclaredMethod("valueOf", String.class);
            if (valueOfMethod.getReturnType() != paramType) {
                // This is not valueOf() I want.
                return object;
            }
        } catch (Exception e) {
            // No valueOf() method
            return object;
        }

        return valueOfMethod.invoke(null, object);
    }

    private static List<Object> extractArgs(String[] argNames, Map<String, Object> attributes) {

        List<Object> args = new ArrayList<>();
        for (String argName : argNames) {
            Object arg = attributes.get(argName);
            if (arg == null) {
                // the widget .ui.xml declaration does not match with this
                // @UiConstructor
                return null;
            }

            args.add(arg);
        }

        return args;
    }

    private static <U> U getObjectFromUiConstructor(Class<U> clazz, Map<String, Object> attributes) {

        for (Constructor<?> cons : clazz.getDeclaredConstructors()) {
            if (cons.getAnnotation(UiConstructor.class) == null) {
                continue;
            }

            Constructor<U> uiConstructor = (Constructor<U>) cons;

            String[] argNames = GwtParanamer.get().lookupParameterNames(uiConstructor);

            if (allArgsAreDeclaredInUiFile(argNames, attributes)) {
                List<Object> constructorArgs = extractArgs(argNames, attributes);

                if (constructorArgs != null) {
                    Class<?>[] paramTypes = uiConstructor.getParameterTypes();
                    for (int i = 0; i < argNames.length; i++) {
                        String argName = argNames[i];
                        try {
                            Object convertedArg = convertArgs(paramTypes[i], constructorArgs.get(i));
                            constructorArgs.set(i, convertedArg);

                            // remove the attribute from the map to populate the widget with since it's
                            // used in the constructor
                            attributes.remove(argName);
                        } catch (Exception e) {
                            throw new GwtTestUiBinderException("Error while converting argument "
                                    + argNames[i] + " to " + paramTypes[i], e);
                        }
                    }
                }

                return instanciate(uiConstructor, constructorArgs);
            }

        }

        return null;
    }

    private static <U> U getObjectFromUiFactory(Class<U> clazz, Object owner) {
        Map<Method, UiFactory> map = GwtReflectionUtils.getAnnotatedMethod(owner.getClass(),
                UiFactory.class);

        List<Method> compatibleFactories = new ArrayList<>();
        for (Method factoryMethod : map.keySet()) {
            if (clazz.isAssignableFrom(factoryMethod.getReturnType())) {
                compatibleFactories.add(factoryMethod);
            }
        }

        switch (compatibleFactories.size()) {
            case 0:
                return null;
            case 1:
                return (U) GwtReflectionUtils.callPrivateMethod(owner, compatibleFactories.get(0));
            default:
                throw new GwtTestUiBinderException("Duplicate factory in class '"
                        + owner.getClass().getName() + " for type '" + clazz.getName() + "'");
        }
    }

    private static <U> U getProvidedUiField(Class<U> clazz, Object owner, String uiFieldValue) {
        Map<Field, UiField> map = GwtReflectionUtils.getAnnotatedField(owner.getClass(),
                UiField.class);

        for (Map.Entry<Field, UiField> entry : map.entrySet()) {
            if (!entry.getValue().provided()) {
                // not a provided uiField
            } else if (entry.getKey().getName().equals(uiFieldValue)
                    || (uiFieldValue == null && entry.getKey().getType() == clazz)) {
                Object providedObject = GwtReflectionUtils.getPrivateFieldValue(owner, entry.getKey());
                if (providedObject == null) {
                    throw new GwtTestUiBinderException(
                            "The UiField(provided=true) '"
                                    + entry.getKey().getDeclaringClass().getSimpleName()
                                    + "."
                                    + entry.getKey().getName()
                                    + "' has not been initialized before calling 'UiBinder.createAndBind(..)' method");
                }

                return (U) providedObject;
            }
        }

        return null;
    }

    private static <U> U instanciate(Constructor<U> cons, List<Object> args) {
        try {
            return GwtReflectionUtils.instantiateClass(cons, args.toArray());
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Error while calling @UiConstructor 'new ").append(
                    cons.getDeclaringClass().getSimpleName()).append("(");

            for (Object arg : args) {
                sb.append("\"").append(arg.toString()).append("\"").append(", ");
            }

            if (args.size() > 0) {
                sb.delete(sb.length() - 2, sb.length() - 1);
            }

            sb.append(");'");

            throw new GwtTestUiBinderException(sb.toString(), e);
        }
    }

}

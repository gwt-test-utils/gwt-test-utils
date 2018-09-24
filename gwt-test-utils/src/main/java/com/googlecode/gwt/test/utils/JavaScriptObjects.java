package com.googlecode.gwt.test.utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.googlecode.gwt.test.internal.utils.JsoProperties;
import com.googlecode.gwt.test.internal.utils.JsoUtils;
import com.googlecode.gwt.test.internal.utils.PropertyContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Utilities for Overlay types support in gwt-test-utils.
 *
 * @author Gael Lazzari
 */
public class JavaScriptObjects {

    public static void clearProperties(JavaScriptObject jso) {
        getJsoProperties(jso).clear();
    }

    public static Set<Map.Entry<String, Object>> entrySet(JavaScriptObject jso) {
        return getJsoProperties(jso).entrySet();
    }

    public static boolean getBoolean(JavaScriptObject jso, String propName) {
        return getJsoProperties(jso, propName).getBoolean(propName);
    }

    public static byte getByte(JavaScriptObject jso, String propName) {
        return getJsoProperties(jso, propName).getByte(propName);
    }

    public static char getChar(JavaScriptObject jso, String propName) {
        return getJsoProperties(jso, propName).getChar(propName);
    }

    public static double getDouble(JavaScriptObject jso, String propName) {
        return getJsoProperties(jso, propName).getDouble(propName);
    }

    public static float getFloat(JavaScriptObject jso, String propName) {
        return getJsoProperties(jso, propName).getFloat(propName);
    }

    public static int getInteger(JavaScriptObject jso, String propName) {
        return getJsoProperties(jso, propName).getInteger(propName);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getObject(JavaScriptObject jso, String propName) {
        return (T) getJsoProperties(jso, propName).getObject(propName);
    }

    public static short getShort(JavaScriptObject jso, String propName) {
        return getJsoProperties(jso, propName).getShort(propName);
    }

    public static String getString(JavaScriptObject jso, String propName) {
        return getJsoProperties(jso, propName).getString(propName);
    }

    public static boolean hasProperty(JavaScriptObject jso, String propName) {
        return getJsoProperties(jso).contains(propName);
    }

    public static void remove(JavaScriptObject jso, String propName) {
        getJsoProperties(jso, propName).remove(propName);
    }

    public static void setProperty(JavaScriptObject jso, String propName, boolean value) {
        getJsoProperties(jso, propName).put(propName, Boolean.valueOf(value));
    }

    public static void setProperty(JavaScriptObject jso, String propName, double value) {
        getJsoProperties(jso, propName).put(propName, Double.valueOf(value));
    }

    public static void setProperty(JavaScriptObject jso, String propName, float value) {
        getJsoProperties(jso, propName).put(propName, Float.valueOf(value));
    }

    public static void setProperty(JavaScriptObject jso, String propName, int value) {
        getJsoProperties(jso, propName).put(propName, Integer.valueOf(value));
    }

    public static void setProperty(JavaScriptObject jso, String propName, long value) {
        getJsoProperties(jso, propName).put(propName, Long.valueOf(value));
    }

    public static void setProperty(JavaScriptObject jso, String propName, Object value) {

        if (JsoProperties.ID.equals(propName)) {
            JsoUtils.onSetId(jso, value.toString(), getString(jso, JsoProperties.ID));
        }
        getJsoProperties(jso, propName).put(propName, value);
    }

    public static void setProperty(JavaScriptObject jso, String propName, short value) {
        getJsoProperties(jso, propName).put(propName, Short.valueOf(value));
    }

    private static PropertyContainer getJsoProperties(JavaScriptObject o) {
        PropertyContainer pc = GwtReflectionUtils.getPrivateFieldValue(o,
                JsoProperties.JSO_PROPERTIES);

        if (pc == null) {
            pc = PropertyContainer.newInstance(new HashMap<>());
            GwtReflectionUtils.setPrivateFieldValue(o, JsoProperties.JSO_PROPERTIES, pc);
        }

        return pc;
    }

    private static PropertyContainer getJsoProperties(JavaScriptObject o, String propertyName) {
        if (JsoUtils.isStandardDOMProperty(propertyName)) {
            // case for standard dom properties, like "id", "name", "title"...
            return JsoUtils.getDomProperties(o.<Element>cast());
        }

        return getJsoProperties(o);

    }

    private JavaScriptObjects() {
    }
}

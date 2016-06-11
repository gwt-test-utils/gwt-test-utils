package com.googlecode.gwt.test.internal.utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.googlecode.gwt.test.utils.JavaScriptObjects;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Some {@link Style} utility methods. <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
public class GwtStyleUtils {

    public static final String STYLE_OBJECT_FIELD = "STYLE_OBJECT";

    public static final String STYLE_PROPERTIES = "STYLE_PROPERTIES";
    // map initialized with default style values
    private static final Map<String, String> DEFAULT_STYLE_VALUES = new HashMap<String, String>() {

        private static final long serialVersionUID = 1L;

        {
            put("whiteSpace", "nowrap");
        }
    };

    private static final Pattern STYLE_PATTERN = Pattern.compile("(.+):(.+)");

    public static void cloneStyle(Style newStyle, Style oldStyle) {
        Map<String, String> oldProperties = getStyleProperties(oldStyle);
        Map<String, String> newProperties = getStyleProperties(newStyle);
        newProperties.clear();
        newProperties.putAll(oldProperties);
    }

    public static String getProperty(Style style, String propertyName) {
        String value = getStyleProperties(style).get(propertyName);

        if (value == null) {
            String defaultValue = DEFAULT_STYLE_VALUES.get(propertyName);
            value = defaultValue != null ? defaultValue : "";
        }

        return value;
    }

    public static Style getStyle(Element element) {
        assert Element.is(element) : "not an Element";

        Style style = JavaScriptObjects.getObject(element, STYLE_OBJECT_FIELD);
        if (style == null) {
            style = newStyle();
            JavaScriptObjects.setProperty(element, STYLE_OBJECT_FIELD, style);
        }

        return style;

    }

    public static LinkedHashMap<String, String> getStyleProperties(String style) {
        LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();

        if (style == null || style.trim().length() == 0) {
            return result;
        }

        String[] styles = style.split("\\s*;\\s*");
        for (String property : styles) {
            Matcher m = STYLE_PATTERN.matcher(property);
            if (m.matches()) {
                result.put(m.group(1).trim(), m.group(2).trim());
            }
        }

        return result;
    }

    public static LinkedHashMap<String, String> getStyleProperties(Style style) {
        LinkedHashMap<String, String> properties = JavaScriptObjects.getObject(style,
                STYLE_PROPERTIES);

        assert properties != null : "not a Style";

        return properties;
    }

    public static boolean isStyle(JavaScriptObject jso) {
        return JavaScriptObjects.hasProperty(jso, STYLE_PROPERTIES);
    }

    public static void overrideStyle(Style target, String newValue) {
        for (Map.Entry<String, String> entry : getStyleProperties(newValue).entrySet()) {
            target.setProperty(GwtStringUtils.camelize(entry.getKey()), entry.getValue());
        }
    }

    public static void setProperty(Style style, String propertyName, String propertyValue) {
        // treat case when propertyValue = "250.0px" => "250px" instead
        propertyValue = GwtStringUtils.treatDoubleValue(propertyValue);

        Map<String, String> styleProperties = GwtStyleUtils.getStyleProperties(style);

        // ensure the style will be added at the end of the LinkedHashMap
        styleProperties.remove(propertyName);
        styleProperties.put(propertyName, propertyValue);
    }

    public static String toString(Style style) {
        LinkedHashMap<String, String> styleProperties = GwtStyleUtils.getStyleProperties(style);
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : styleProperties.entrySet()) {
            String cssPropertyValue = entry.getValue().trim();

            if (!"".equals(cssPropertyValue)) {
                String cssProperyName = GwtStringUtils.hyphenize(entry.getKey());
                sb.append(cssProperyName).append(": ").append(cssPropertyValue).append("; ");
            }
        }

        return sb.toString();

    }

    private static Style newStyle() {
        Style style = JavaScriptObject.createObject().cast();

        JavaScriptObjects.setProperty(style, STYLE_PROPERTIES, new LinkedHashMap<String, String>());

        return style;
    }

    private GwtStyleUtils() {

    }

}

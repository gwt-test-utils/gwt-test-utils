package com.googlecode.gwt.test.internal.i18n;

import com.google.gwt.i18n.client.Constants.*;
import com.google.gwt.i18n.client.LocalizableResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.googlecode.gwt.test.exceptions.GwtTestI18NException;
import com.googlecode.gwt.test.internal.utils.GwtPropertiesHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

class ConstantsInvocationHandler extends LocalizableResourceInvocationHandler {

    ConstantsInvocationHandler(Class<? extends LocalizableResource> proxiedClass) {
        super(proxiedClass);
    }

    @Override
    protected Object extractDefaultValue(Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();
        if (returnType == String.class) {
            DefaultStringValue a = getCheckedAnnotation(method, DefaultStringValue.class);
            return treatLine(a.value());
        } else if (returnType == SafeHtml.class) {
            DefaultStringValue a = getCheckedAnnotation(method, DefaultStringValue.class);
            return SafeHtmlUtils.fromTrustedString(treatLine(a.value()));
        } else if (returnType.isArray() && returnType.getComponentType() == String.class) {
            DefaultStringArrayValue a = getCheckedAnnotation(method, DefaultStringArrayValue.class);
            String[] v = a.value();
            for (int i = 0; i < v.length; i++) {
                v[i] = treatLine(v[i]);
            }
            return v;
        } else if (returnType == Map.class) {
            DefaultStringMapValue a = getCheckedAnnotation(method, DefaultStringMapValue.class);
            String[] v = a.value();
            if (v.length % 2 != 0) {
                throw new GwtTestI18NException("@" + DefaultStringMapValue.class.getSimpleName()
                        + " value incorrect for method " + method.getDeclaringClass().getName() + "."
                        + method.getName() + "() : the array value (key/value pair) should be even");
            }
            Map<String, String> result = new HashMap<>();
            for (int i = 0; i < v.length; i++) {
                result.put(v[i], treatLine(v[++i]));
            }
            return result;

        } else if (returnType == Integer.class || returnType == Integer.TYPE) {
            DefaultIntValue a = getCheckedAnnotation(method, DefaultIntValue.class);
            return a.value();
        } else if (returnType == Double.class || returnType == Double.TYPE) {
            DefaultDoubleValue a = getCheckedAnnotation(method, DefaultDoubleValue.class);
            return a.value();
        } else if (returnType == Float.class || returnType == Float.TYPE) {
            DefaultFloatValue a = getCheckedAnnotation(method, DefaultFloatValue.class);
            return a.value();
        } else if (returnType == Boolean.class || returnType == Boolean.TYPE) {
            DefaultBooleanValue a = getCheckedAnnotation(method, DefaultBooleanValue.class);
            return a.value();
        }

        throw new GwtTestI18NException("The return type (" + returnType.getSimpleName()
                + ") of i18n '" + getProxiedClass().getSimpleName() + "." + method.getName()
                + "()' is not managed");

    }

    @Override
    protected Object extractFromProperties(Properties properties, Method method, Object[] args,
                                           Locale locale) throws Throwable {
        String line = properties.getProperty(getKey(method));

        if (line == null) {
            return null;
        }

        Class<?> returnType = method.getReturnType();

        if (returnType == String.class) {
            return line;
        } else if (returnType == SafeHtml.class) {
            return SafeHtmlUtils.fromTrustedString(line);
        } else if (returnType.isArray() && returnType.getComponentType() == String.class) {
            return line.split("\\s*,\\s*");
        } else if (returnType == Map.class) {
            Map<String, Object> result = new HashMap<>();
            String[] v = line.split("\\s*,\\s*");

            for (String key : v) {
                result.put(key, properties.get(key));
            }
            return result;

        } else if (returnType == Integer.class || returnType == Integer.TYPE) {
            return Integer.parseInt(line);
        } else if (returnType == Double.class || returnType == Double.TYPE) {
            return Double.parseDouble(line);
        } else if (returnType == Float.class || returnType == Float.TYPE) {
            return Float.parseFloat(line);
        } else if (returnType == Boolean.class || returnType == Boolean.TYPE) {
            return Boolean.parseBoolean(line);
        }

        throw new GwtTestI18NException("The return type (" + returnType.getSimpleName()
                + ") of i18n '" + method.getDeclaringClass().getSimpleName() + "."
                + method.getName() + "()' is not managed");

    }

    private <T extends Annotation> T getCheckedAnnotation(Method method, Class<T> defaultAnnotation) {
        T v = method.getAnnotation(defaultAnnotation);
        if (v == null) {
            throw new GwtTestI18NException("No matching property \"" + method.getName()
                    + "\" for Constants class [" + getProxiedClass().getCanonicalName()
                    + "]. Please check the corresponding properties files or use @"
                    + defaultAnnotation.getSimpleName());
        }

        return v;
    }

    private String treatLine(String line) {
        return GwtPropertiesHelper.get().treatString(line);
    }
}

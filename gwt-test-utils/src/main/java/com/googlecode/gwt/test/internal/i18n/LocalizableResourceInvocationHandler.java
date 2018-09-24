package com.googlecode.gwt.test.internal.i18n;

import com.google.gwt.i18n.client.LocalizableResource;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.LocalizableResource.Key;
import com.googlecode.gwt.test.exceptions.GwtTestI18NException;
import com.googlecode.gwt.test.internal.GwtConfig;
import com.googlecode.gwt.test.internal.utils.GwtPropertiesHelper;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Properties;

abstract class LocalizableResourceInvocationHandler implements InvocationHandler {

    private final Class<? extends LocalizableResource> proxiedClass;

    LocalizableResourceInvocationHandler(Class<? extends LocalizableResource> proxiedClass) {
        this.proxiedClass = proxiedClass;
    }

    Class<? extends LocalizableResource> getProxiedClass() {
        return proxiedClass;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // try in the locale specific .properties of the class
        Object result = extractLocaleSpecificValue(proxiedClass, method, args);
        if (result != null) {
            return result;
        }

        // try in the locale specific .properties of the parent class
        result = recurseExtractFromParentLocaleSpecificResource(proxiedClass.getInterfaces(), method,
                args);
        if (result != null) {
            return result;
        }

        // try to get the value from a .properties without locale
        result = extractFromDefaultProperties(proxiedClass, method, args);
        Properties prop = GwtPropertiesHelper.get().getProperties(
                getPropertiesFilePrefix(proxiedClass));
        if (prop != null) {
            result = extractFromProperties(prop, method, args, null);
            if (result != null) {
                return result;
            }
        }

        // try in .properties of the parent class (without locale)
        result = recurseExtractFromParentResource(proxiedClass.getInterfaces(), method, args);
        if (result != null) {
            return result;
        }

        result = extractDefaultValue(method, args);

        if (result != null) {
            return result;
        }

        throw new GwtTestI18NException(
                "Unable to find a Locale specific resource file to bind with i18n interface '"
                        + proxiedClass.getName()
                        + "' and there is no @DefaultXXXValue annotation on '" + method.getName()
                        + "' called method");
    }

    protected abstract Object extractDefaultValue(Method method, Object[] args) throws Throwable;

    protected abstract Object extractFromProperties(Properties localizedProperties, Method method,
                                                    Object[] args, Locale locale) throws Throwable;

    protected String getKey(Method method) {
        Key key = method.getAnnotation(Key.class);
        return key != null ? key.value() : method.getName();
    }

    protected Locale getLocale() {
        if (GwtConfig.get().getModuleRunner().getLocale() != null) {
            return GwtConfig.get().getModuleRunner().getLocale();
        }

        DefaultLocale annotation = GwtReflectionUtils.getAnnotation(proxiedClass, DefaultLocale.class);
        if (annotation != null) {
            String[] localeCodes = annotation.value().split("_");
            switch (localeCodes.length) {
                case 1:
                    return new Locale(localeCodes[0]);
                case 2:
                    return new Locale(localeCodes[0], localeCodes[1]);
                default:
                    throw new GwtTestI18NException("Cannot parse Locale value in annoted class ["
                            + proxiedClass.getSimpleName() + "] : @"
                            + DefaultLocale.class.getSimpleName() + "(" + annotation.value() + ")");
            }
        } else {
            return null;
        }
    }

    private Object extractFromDefaultProperties(Class<?> clazz, Method method, Object[] args)
            throws Throwable {
        // try to get the value from a .properties without locale
        Properties prop = GwtPropertiesHelper.get().getProperties(getPropertiesFilePrefix(clazz));
        if (prop != null) {
            return extractFromProperties(prop, method, args, getLocale());
        } else {
            return null;
        }
    }

    private Object extractLocaleSpecificValue(Class<?> localizableResourceClass, Method method,
                                              Object[] args) throws Throwable {
        Object result = null;
        Locale locale = getLocale();
        Properties prop = GwtPropertiesHelper.get().getLocalizedProperties(
                getPropertiesFilePrefix(localizableResourceClass), locale);

        if (prop != null) {
            result = extractFromProperties(prop, method, args, locale);
        }

        return result;
    }

    private String getPropertiesFilePrefix(Class<?> localizableResourceClass) {
        return localizableResourceClass.getCanonicalName().replaceAll("\\.", "/");
    }

    private Object recurseExtractFromParentLocaleSpecificResource(Class<?>[] interfaces,
                                                                  Method method, Object[] args) throws Throwable {
        Object result;
        for (Class<?> inter : interfaces) {
            if (LocalizableResource.class.isAssignableFrom(inter)) {
                result = extractLocaleSpecificValue(inter, method, args);

                if (result != null) {
                    return result;
                } else {
                    return recurseExtractFromParentLocaleSpecificResource(inter.getInterfaces(), method,
                            args);
                }
            }
        }

        return null;
    }

    private Object recurseExtractFromParentResource(Class<?>[] interfaces, Method method,
                                                    Object[] args) throws Throwable {
        Object result;
        for (Class<?> inter : interfaces) {
            if (LocalizableResource.class.isAssignableFrom(inter)) {
                result = extractFromDefaultProperties(inter, method, args);

                if (result != null) {
                    return result;
                } else {
                    return recurseExtractFromParentResource(inter.getInterfaces(), method, args);
                }
            }
        }

        return null;
    }

}

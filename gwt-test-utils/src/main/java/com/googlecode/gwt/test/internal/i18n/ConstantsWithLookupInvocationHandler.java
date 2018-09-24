package com.googlecode.gwt.test.internal.i18n;

import com.google.gwt.i18n.client.LocalizableResource;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;

class ConstantsWithLookupInvocationHandler extends ConstantsInvocationHandler {

    ConstantsWithLookupInvocationHandler(Class<? extends LocalizableResource> proxiedClass) {
        super(proxiedClass);
    }

    @Override
    protected Object extractDefaultValue(Method method, Object[] args) throws Throwable {
        return super.extractDefaultValue(getAskedMethod(method, args), args);
    }

    @Override
    protected Object extractFromProperties(Properties localizedProperties, Method method,
                                           Object[] args, Locale locale) throws Throwable {
        return super.extractFromProperties(localizedProperties, getAskedMethod(method, args), args,
                locale);
    }

    private Method getAskedMethod(Method method, Object[] args) {
        if (args != null && args.length == 1 && args[0].getClass() == String.class) {
            String methodName = (String) args[0];
            try {
                return getProxiedClass().getMethod(methodName);
            } catch (Exception e) {
                throw new MissingResourceException("Cannot find constant '" + methodName
                        + "'; expecting a method name from " + getProxiedClass().getName(),
                        getProxiedClass().getName(), methodName);
            }
        } else {
            return method;
        }
    }

}

package com.googlecode.gwt.test.internal.handlers;

import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

class DefaultGwtCreateHandler implements GwtCreateHandler {

    @SuppressWarnings("unchecked")
    public Object create(Class<?> classLiteral) {
        if (classLiteral.isAnnotation() || classLiteral.isArray() || classLiteral.isEnum()
                || classLiteral.isInterface() || Modifier.isAbstract(classLiteral.getModifiers())) {
            return null;
        }

        try {
            // try to call public default constructor
            return classLiteral.newInstance();
        } catch (Exception e) {
            // search for a not-public default constructor to invoke
            try {
                Constructor<Object> cons = (Constructor<Object>) classLiteral.getDeclaredConstructor();
                return GwtReflectionUtils.instantiateClass(cons);
            } catch (NoSuchMethodException e2) {
                return null;
            }
        }
    }

}

package com.googlecode.gwt.test.gin;

import com.google.inject.Injector;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * InvocationHandler where calls to an proxified Gin {@link Injector} are redirected.
 *
 * @author Alex Dobjanschi
 * @author Gael Lazzari
 */
class GinInjectorInvocationHandler implements InvocationHandler {

    // the wrapped Guice injector
    private final Injector injector;

    GinInjectorInvocationHandler(Injector injector) {
        this.injector = injector;
    }

    public Object invoke(Object proxy, Method method, Object[] args) {
        // Make sure method called has zero arguments (otherwise we're in
        // big trouble).
        assert args == null || args.length == 0 : "Cannot execute GinInjector methods with non-zero argument list";

        // Try to return a new instance based on the classLiteral
        // returnType.
        Class<?> returnLiteral = method.getReturnType();
        return injector.getInstance(returnLiteral);
    }
}
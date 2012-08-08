package com.googlecode.gwt.test.gin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.google.inject.Injector;

/**
 * InvocationHandler where calls to an proxified Gin {@link Injector} are redirected.
 * 
 * @author Alex Dobjanschi
 * @author Gael Lazzari
 * 
 */
class GinInjectorInvocationHandler implements InvocationHandler {

   // the wrapped Guice injector
   private final Injector injector;

   public GinInjectorInvocationHandler(Injector injector) {
      this.injector = injector;
   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      // Make sure method called has zero arguments (otherwise we're in
      // big trouble).
      assert args == null || args.length == 0 : "Cannot execute GinInjector methods with non-zero argument list";

      // Try to return a new instance based on the classLiteral
      // returnType.
      Class<?> returnLiteral = method.getReturnType();
      return injector.getInstance(returnLiteral);
   }
}
package com.googlecode.gwt.test.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.gwt.user.server.rpc.AbstractRemoteServiceServlet;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.exceptions.GwtTestRpcException;
import com.googlecode.gwt.test.internal.AsyncCallbackRecorder;
import com.googlecode.gwt.test.internal.patchers.AbstractRemoteServiceServletPatcher;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

class GwtRpcInvocationHandler implements InvocationHandler {

   private static final Logger logger = LoggerFactory.getLogger(GwtRpcInvocationHandler.class);

   private final GwtRpcExceptionHandler exceptionHandler;
   private final Map<Method, Method> methodTable;
   private final GwtRpcSerializerHandler serializerHander;
   private final Object target;

   public GwtRpcInvocationHandler(Class<?> asyncClazz, Object target,
            GwtRpcExceptionHandler exceptionHandler, GwtRpcSerializerHandler serializerHandler) {

      this.target = target;
      this.exceptionHandler = exceptionHandler;
      this.serializerHander = serializerHandler;

      this.methodTable = new HashMap<Method, Method>();
      for (Method m : asyncClazz.getMethods()) {
         for (Method m2 : target.getClass().getMethods()) {
            if (m.getName().equals(m2.getName())
                     && m.getParameterTypes().length == m2.getParameterTypes().length + 1) {
               methodTable.put(m, m2);
               GwtReflectionUtils.makeAccessible(m2);
            }
         }
      }
   }

   @SuppressWarnings("unchecked")
   public Object invoke(Object proxy, Method method, Object[] args) {
      Object[] subArgs = new Object[args.length - 1];
      for (int i = 0; i < args.length - 1; i++) {
         subArgs[i] = args[i];
      }

      final AsyncCallback<Object> callback = (AsyncCallback<Object>) args[args.length - 1];
      final Method m = methodTable.get(method);

      Command asyncCallbackCommand;

      if (m == null) {
         logger.error("Method not found " + method);

         // error 500 async call
         asyncCallbackCommand = new Command() {
            public void execute() {
               callback.onFailure(new StatusCodeException(500, "No method found"));
            }
         };
      } else {

         try {
            logger.debug("Invoking " + m + " on " + target.getClass().getName());
            // Serialize objects
            Object[] serializedArgs = new Object[subArgs.length];
            for (int i = 0; i < subArgs.length; i++) {
               try {
                  serializedArgs[i] = serializerHander.serializeUnserialize(subArgs[i]);
               } catch (Exception e) {
                  throw new GwtTestRpcException("Error while serializing argument " + i
                           + " of type " + subArgs[i].getClass().getName() + " in method "
                           + method.getDeclaringClass().getSimpleName() + "." + method.getName()
                           + "(..)", e);
               }
            }

            if (target instanceof AbstractRemoteServiceServlet) {
               AbstractRemoteServiceServletPatcher.currentCalledMethod = m;
            }

            Object resultObject = m.invoke(target, serializedArgs);

            Object returnObject;
            try {
               returnObject = serializerHander.serializeUnserialize(resultObject);
            } catch (Exception e) {
               throw new GwtTestRpcException("Error while serializing object of type "
                        + resultObject.getClass().getName()
                        + " which was returned from RPC Service "
                        + method.getDeclaringClass().getSimpleName() + "." + method.getName()
                        + "(..)", e);
            }

            final Object o = returnObject;

            // success async call
            asyncCallbackCommand = new Command() {
               public void execute() {
                  logger.debug("Result of " + m.getName() + " : " + o);
                  callback.onSuccess(o);
               }
            };

         } catch (final InvocationTargetException e) {
            if (GwtTestException.class.isInstance(e.getCause())) {
               throw (GwtTestException) e.getCause();
            }
            asyncCallbackCommand = new Command() {
               public void execute() {
                  logger.info("Exception when invoking service throw to handler " + e.getMessage());
                  exceptionHandler.handle(e.getCause(), callback);
               }
            };
         } catch (final IllegalAccessException e) {
            asyncCallbackCommand = new Command() {
               public void execute() {
                  logger.error("GWT RPC exception : " + e.toString(), e);
                  callback.onFailure(new StatusCodeException(500, e.toString()));
               }
            };
         } catch (final IllegalArgumentException e) {
            asyncCallbackCommand = new Command() {
               public void execute() {
                  logger.error("GWT RPC exception : " + e.toString(), e);
                  callback.onFailure(new StatusCodeException(500, e.toString()));
               }
            };
         }

      }
      // delegate the execution to the recorder
      AsyncCallbackRecorder.get().handleAsyncCallback(asyncCallbackCommand);

      // async callback always return void
      return null;
   }
}

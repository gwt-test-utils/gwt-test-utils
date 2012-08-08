package com.googlecode.gwt.test.internal.i18n;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Properties;

import com.google.gwt.i18n.client.LocalizableResource;
import com.googlecode.gwt.test.exceptions.GwtTestI18NException;

class ConstantsWithLookupInvocationHandler extends ConstantsInvocationHandler {

   public ConstantsWithLookupInvocationHandler(Class<? extends LocalizableResource> proxiedClass) {
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
            throw new GwtTestI18NException("Cannot find constant '" + methodName
                     + "'; expecting a method name from " + getProxiedClass().getName());
         }
      } else {
         return method;
      }
   }

}

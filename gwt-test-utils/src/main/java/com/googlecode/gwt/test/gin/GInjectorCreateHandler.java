package com.googlecode.gwt.test.gin;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.GinModule;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.inject.rebind.adapter.GinModuleAdapter;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.googlecode.gwt.test.GwtCreateHandler;

/**
 * Handle {@link GWT#create(Class)} for {@link Ginjector}.
 * 
 * @author Alex Dobjanschi
 * @author Gael Lazzari
 * 
 */
public class GInjectorCreateHandler implements GwtCreateHandler {

   private static final Logger LOGGER = LoggerFactory.getLogger(GInjectorCreateHandler.class);

   // map used as cache to store bindings between gin and guice proxy injectors
   private Map<Class<? extends Ginjector>, Object> injectors;

   public Object create(Class<?> classLiteral) throws Exception {
      // Make sure this is a Ginjector
      if (!Ginjector.class.isAssignableFrom(classLiteral)) {
         return null;
      }

      @SuppressWarnings("unchecked")
      Class<? extends Ginjector> ginInjectorClass = (Class<? extends Ginjector>) classLiteral;

      if (injectors == null) {
         injectors = new HashMap<Class<? extends Ginjector>, Object>();
      }

      Object guiceInjectorProxy = injectors.get(classLiteral);

      if (guiceInjectorProxy != null) {
         LOGGER.debug("Proxy for class '" + ginInjectorClass.getName()
                  + "'has been found in cache. It is returned");
         return guiceInjectorProxy;
      }

      Class<? extends GinModule>[] ginModules = readGinModules(ginInjectorClass);

      // create a set of Guice Module bases on the GinModules
      Set<Module> guiceModules = readGuiceModules(ginModules);

      // Use Guice SPI to solve deferred binding dependencies
      DeferredBindingModule deferredBindingModule = DeferredBindingModule.getDeferredBindingModule(
               ginInjectorClass, guiceModules);
      guiceModules.add(deferredBindingModule);

      // Instantiate an injector, based on the modules read above + the
      // deferredBindingModule
      Injector injector = Guice.createInjector(guiceModules);

      LOGGER.debug("creating new Proxy for class '" + ginInjectorClass.getName() + "'");

      guiceInjectorProxy = Proxy.newProxyInstance(this.getClass().getClassLoader(),
               new Class[]{ginInjectorClass}, new GinInjectorInvocationHandler(injector));

      injectors.put(ginInjectorClass, guiceInjectorProxy);

      return guiceInjectorProxy;
   }

   private Class<? extends GinModule>[] readGinModules(Class<? extends Ginjector> classLiteral) {
      LOGGER.debug("inspecting classLiteral " + classLiteral);
      GinModules annotation = classLiteral.getAnnotation(GinModules.class);
      if (annotation == null) {
         // Throw an exception if we don't find this specific annotation.
         throw new IllegalArgumentException(classLiteral.getName()
                  + " doesn't have any @GinModules annotation present");
      }

      Class<? extends GinModule>[] ginModules = annotation.value();

      if (ginModules == null || ginModules.length == 0) {
         // there are no GinModules present in the Ginjector.
         throw new IllegalArgumentException(classLiteral.getName()
                  + " doesn't have any GinModules. " + "Runtime should not work.");
      }

      LOGGER.debug("discovered modules " + annotation);
      return ginModules;
   }

   private Set<Module> readGuiceModules(Class<? extends GinModule>[] classLiterals)
            throws Exception {

      Set<Module> modules = new HashSet<Module>();
      for (Class<? extends GinModule> literal : classLiterals) {
         LOGGER.debug("wrapping GinModule literal " + literal);
         modules.add(new GinModuleAdapter(literal.newInstance()));
      }

      return modules;

   }
}

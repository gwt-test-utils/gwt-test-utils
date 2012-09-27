package com.googlecode.gwt.test.gin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.ProvidedBy;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.spi.DefaultElementVisitor;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.Element;
import com.google.inject.spi.Elements;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.InjectionPoint;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

/**
 * Additional Guice module class which must be added when replacing a GIN Injector with a Guice
 * Injector, in order to add all required bindings to call GWT's deferred binding fallback which
 * happens in GIN.
 * 
 * @author Alex Dobjanschi
 * @author Gael Lazzari
 * 
 */
class DeferredBindingModule extends AbstractModule {

   private static class DeferredBindingProvider implements Provider<Object> {

      private final Class<?> clazzToInstanciate;

      public DeferredBindingProvider(Class<? extends Ginjector> ginInjectorClass,
               Class<?> clazzToInstanciate) {
         if (clazzToInstanciate.getName().endsWith("Async")) {
            try {
               this.clazzToInstanciate = GwtReflectionUtils.getClass(clazzToInstanciate.getName().substring(
                        0, clazzToInstanciate.getName().length() - 5));
            } catch (ClassNotFoundException e) {
               throw new GwtTestPatchException(
                        "Error while trying to create a Guice provider for injector '"
                                 + ginInjectorClass.getName() + "'", e);
            }
         } else {
            this.clazzToInstanciate = clazzToInstanciate;
         }
      }

      public Object get() {
         // call GWT deferred binding, which is patch by gwt-test-utils to call
         // GwtCreateHandlerManager
         return GWT.create(clazzToInstanciate);
      }

   }

   private static final Map<Class<?>, DeferredBindingModule> DEFERRED_BINDING_MODULES_CACHE = new HashMap<Class<?>, DeferredBindingModule>();
   private static final Map<Class<?>, Boolean> HAS_INJECTION_ANNOTATION_CACHE = new HashMap<Class<?>, Boolean>();

   private static final Logger LOGGER = LoggerFactory.getLogger(DeferredBindingModule.class);

   static final DeferredBindingModule getDeferredBindingModule(
            Class<? extends Ginjector> ginInjectorClass, Collection<Module> modules) {
      DeferredBindingModule deferredBindingModule = DEFERRED_BINDING_MODULES_CACHE.get(ginInjectorClass);
      if (deferredBindingModule == null) {
         deferredBindingModule = new DeferredBindingModule(ginInjectorClass,
                  modules.toArray(new Module[modules.size()]));
         DEFERRED_BINDING_MODULES_CACHE.put(ginInjectorClass, deferredBindingModule);
      }

      return deferredBindingModule;
   }

   private final Set<Class<?>> bindedClasses;

   private final Set<Class<?>> classesToInstanciate;

   private final Class<? extends Ginjector> ginInjectorClass;

   private DeferredBindingModule(Class<? extends Ginjector> ginInjectorClass, Module[] modules) {

      this.ginInjectorClass = ginInjectorClass;
      List<Element> elements = Elements.getElements(modules);
      this.classesToInstanciate = collectClassesToInstanciate(ginInjectorClass);
      this.classesToInstanciate.addAll(collectDependencies(elements));
      this.bindedClasses = collectBindedClasses(elements);
   }

   @SuppressWarnings({"unchecked"})
   @Override
   protected void configure() {

      for (Class<?> toInstanciate : classesToInstanciate) {
         if (!bindedClasses.contains(toInstanciate)) {

            if (toInstanciate.equals(Provider.class)) {
               continue;
               // Skip the Provider class. However, there's a second
               // issue here, when you need to inject a Provider of
               // parameterized type not already bound (something that
               // falls back to GWT.create): Animal(Provider<Messages>
               // provider).
               // I'm not sure there's a way to fix this (other that
               // don't use Provider for generated code).
            }

            if (hasAnyGuiceAnnotation(toInstanciate)) {
               // bind to itself, to tell guice there are some injection to
               // proceed
               // although the binding is not declared in the module
               bind(toInstanciate);
            } else {
               // by default use GWT deferred binding to create leaf instances
               // to be
               // injected
               bind((Class<Object>) toInstanciate).toProvider(
                        new DeferredBindingProvider(ginInjectorClass, toInstanciate));
            }
         }
      }
   }

   private Set<Class<?>> collectBindedClasses(List<Element> elements) {
      final Set<Class<?>> bindedClasses = new HashSet<Class<?>>();

      for (Element e : elements) {
         e.acceptVisitor(new DefaultElementVisitor<Void>() {
            @Override
            public <T> Void visit(Binding<T> binding) {
               bindedClasses.add(binding.getKey().getTypeLiteral().getRawType());
               return null;
            }
         });
      }

      return bindedClasses;
   }

   private Set<Class<?>> collectClassesToInstanciate(Class<?> classLiteral) {

      Set<Class<?>> classesToInstanciate = new HashSet<Class<?>>();

      for (Method m : classLiteral.getMethods()) {
         if (m.getGenericParameterTypes().length > 0) {
            // This method has non-zero argument list. We cannot do anything
            // about it, so inform developer and continue
            LOGGER.warn("skipping method '" + m.toGenericString()
                     + "' because it has non-zero argument list");
            continue;
         }

         Class<?> literal = m.getReturnType();

         classesToInstanciate.add(literal);

      }

      return classesToInstanciate;
   }

   private Set<Class<?>> collectDependencies(List<Element> elements) {
      final Set<Class<?>> dependencies = new HashSet<Class<?>>();
      for (Element e : elements) {
         e.acceptVisitor(new DefaultElementVisitor<Void>() {
            @Override
            public <T> Void visit(Binding<T> binding) {
               LOGGER.debug("visiting binding " + binding.toString());

               if (binding instanceof HasDependencies) {
                  HasDependencies deps = (HasDependencies) binding;
                  for (Dependency<?> d : deps.getDependencies()) {
                     if (!d.getKey().getTypeLiteral().getRawType().isInterface()) {
                        InjectionPoint point = InjectionPoint.forConstructorOf(d.getKey().getTypeLiteral());
                        dependencies.addAll(getDependencies(point));
                     } else {
                        dependencies.add(d.getKey().getTypeLiteral().getRawType());
                     }
                  }
               } else {
                  // At least try to fix the dependecies for untargeted bindings
                  InjectionPoint point = InjectionPoint.forConstructorOf(binding.getKey().getTypeLiteral());
                  dependencies.addAll(getDependencies(point));

               }

               return null;
            }
         });
      }

      return dependencies;
   }

   private Set<Class<?>> getDependencies(InjectionPoint point) {
      Set<Class<?>> dependencies = new HashSet<Class<?>>();
      for (Dependency<?> d1 : point.getDependencies()) {
         Class<?> classLiteral = d1.getKey().getTypeLiteral().getRawType();
         dependencies.add(classLiteral);
      }

      return dependencies;
   }

   private boolean hasAnyGuiceAnnotation(Class<?> toInstanciate) {
      Boolean hasAnyGuiceAnnotation = HAS_INJECTION_ANNOTATION_CACHE.get(toInstanciate);
      if (hasAnyGuiceAnnotation != null) {
         return hasAnyGuiceAnnotation;
      }

      if (GwtReflectionUtils.getAnnotation(toInstanciate, Singleton.class) != null) {
         hasAnyGuiceAnnotation = true;
      } else if (GwtReflectionUtils.getAnnotation(toInstanciate, ProvidedBy.class) != null) {
         hasAnyGuiceAnnotation = true;
      } else if (hasInjectAnnotatedConstructor(toInstanciate)) {
         hasAnyGuiceAnnotation = true;
      } else {
         hasAnyGuiceAnnotation = GwtReflectionUtils.getAnnotatedField(toInstanciate, Inject.class).size() > 0;
      }

      HAS_INJECTION_ANNOTATION_CACHE.put(toInstanciate, hasAnyGuiceAnnotation);

      return hasAnyGuiceAnnotation;
   }

   private boolean hasInjectAnnotatedConstructor(Class<?> toInstanciate) {
      for (Constructor<?> cons : toInstanciate.getDeclaredConstructors()) {
         if (cons.getAnnotation(Inject.class) != null) {
            return true;
         }
      }
      return false;
   }
}

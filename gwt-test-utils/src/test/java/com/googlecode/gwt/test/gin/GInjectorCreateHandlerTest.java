package com.googlecode.gwt.test.gin;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.rpc.RemoteServiceCreateHandler;

public class GInjectorCreateHandlerTest extends GwtTestTest {

   @GinModules({Gin1Module.class})
   static interface Gin1Injector extends Ginjector {
      Virtual virtual();
   }

   // This module will only contain a single binding
   static final class Gin1Module extends AbstractGinModule {
      @Override
      protected void configure() {
         bind(Virtual.class).to(Impl.class).in(Singleton.class);
      }
   }

   @GinModules({Gin2Module.class})
   static interface Gin2Injector extends Ginjector {
      SomeServiceAsync service();

      Virtual virtual();

      VirtualMore virtualMore();
   }

   static final class Gin2Module extends AbstractGinModule {
      @Override
      protected void configure() {
         bind(Virtual.class).to(Impl2.class);
         bind(VirtualMore.class).to(ImplMore.class);
      }
   }

   @GinModules({Gin3Module.class})
   static interface Gin3Injector extends Ginjector {
      ImplMore implMore();
   }

   static final class Gin3Module extends AbstractGinModule {
      @Override
      protected void configure() {
         bind(Virtual.class).to(Impl2.class);
      }
   }

   @GinModules(Gin4Module.class)
   interface Gin4Injector extends Ginjector {
      Virtual virtual();
   }

   static class Gin4Module extends AbstractGinModule {
      @Override
      protected void configure() {
         bind(Virtual.class).to(ImplementationWithProviders.class);
         bind(VirtualMore.class).to(ImplMore.class);
      }
   }

   @GinModules(Gin5Module.class)
   interface Gin5Injector extends Ginjector {
      Impl singletonImpl();

      Virtual singletonVirtual();

   }

   static class Gin5Module extends AbstractGinModule {

      @Override
      protected void configure() {
         bind(Impl.class).in(Singleton.class);

         bind(Virtual.class).to(Impl.class);
      }

   }

   @GinModules(Gin6Module.class)
   interface Gin6Injector extends Ginjector {
      Virtual singletonImpl();

      Impl3 wrapper();

   }

   static class Gin6Module extends AbstractGinModule {

      @Override
      protected void configure() {
         bind(Virtual.class).to(Impl.class).in(Singleton.class);

      }

      @Provides
      Impl3 provideImpl3(Virtual toWrap) {
         return new Impl3(toWrap);
      }

   }

   @GinModules(Gin7Module.class)
   interface Gin7Injector extends Ginjector {
      Impl2 eagerSingleton();
   }

   static class Gin7Module extends AbstractGinModule {

      @Override
      protected void configure() {
         bind(Impl2.class).asEagerSingleton();
      }
   }

   static class Impl implements Virtual {
   }

   static class Impl2 implements Virtual {
      TestMessages messages;

      @Inject
      public Impl2(TestMessages messages) {
         this.messages = messages;
      }
   }

   static class Impl3 implements Virtual {

      protected final Virtual wrapped;

      public Impl3(Virtual impl) {
         this.wrapped = impl;
      }
   }

   static class ImplementationWithProviders implements Virtual {
      @Inject
      public ImplementationWithProviders(Provider<VirtualMore> provider) {
      }
   }

   static class ImplMore implements VirtualMore {
      Virtual core;

      @Inject
      public ImplMore(Virtual core) {
         super();
         this.core = core;
      }

   }

   // These bindings test the ability of Ginjector GwtCreateHandler
   // to fallback to GWT.create for unbound ones (like RemoteService,
   // Constants, Messages, etc).
   @RemoteServiceRelativePath("service")
   static interface Service extends RemoteService {
      String name();
   }

   static interface ServiceAsync {
      void name(AsyncCallback<String> callback);
   }

   static class ServiceImpl implements Service {

      public String name() {
         return "Service Implementation";
      }

   }

   @RemoteServiceRelativePath("someService")
   static interface SomeService extends RemoteService {
   }

   static interface SomeServiceAsync {
   }

   static class SomeServiceImpl implements SomeService {

   }

   static interface TestMessages extends Messages {
      @DefaultMessage("this is junit")
      String myName();
   }

   // Simple bindings
   static interface Virtual {
   }
   static interface VirtualMore {
   }

   @Before
   public void beforeGinjectorCreateHandler() {
      addGwtCreateHandler(new GInjectorCreateHandler());
      addGwtCreateHandler(new RemoteServiceCreateHandler() {

         @Override
         protected Object findService(Class<?> remoteServiceClass, String remoteServiceRelativePath) {

            if (Service.class.equals(remoteServiceClass)) {
               return new ServiceImpl();
            } else if (SomeService.class.equals(remoteServiceClass)) {
               return new SomeServiceImpl();
            }

            return null;
         }
      });
   }

   @Test
   public void shouldBindAndServe() {
      // Arrange
      Gin1Injector injector1 = GWT.create(Gin1Injector.class);

      // Act
      Virtual v = injector1.virtual();

      // Assert
      assertEquals(Impl.class, v.getClass());
      assertSame(v, injector1.virtual());
   }

   @Test
   public void shouldFallbackToGwtCreate() {
      // Arrange
      Gin2Injector injector2 = GWT.create(Gin2Injector.class);

      // Act
      Virtual virtual = injector2.virtual();
      SomeServiceAsync service = injector2.service();

      // Assert
      assertEquals(Impl2.class, virtual.getClass());
      Assert.assertNotSame(virtual, injector2.virtual());
      assertNotNull(service);
   }

   @Test
   public void shouldInstanciateSingletonOnce() {
      // Arrange
      Gin5Injector injector5 = GWT.create(Gin5Injector.class);

      // Act
      Impl impl = injector5.singletonImpl();
      Virtual virtual = injector5.singletonVirtual();

      // Assert
      assertSame(impl, virtual);
   }

   @Test
   public void shouldInstanciateUsingProvidesMethod() {
      // Arrange
      Gin6Injector injector6 = GWT.create(Gin6Injector.class);

      // Act
      Impl3 wrapper = injector6.wrapper();

      // Assert
      assertEquals(injector6.singletonImpl(), wrapper.wrapped);
   }

   @Test
   public void shouldInstantiateComplexObjectGraphs() {
      // Arrange
      Gin2Injector injector2 = GWT.create(Gin2Injector.class);

      // Act
      VirtualMore more = injector2.virtualMore();

      // Assert
      assertEquals(ImplMore.class, more.getClass());
   }

   @Test
   public void shouldInstantiateConcreteComplexObjectGraphs() {
      // Arrange
      Gin3Injector injector3 = GWT.create(Gin3Injector.class);

      // Act
      ImplMore more = injector3.implMore();

      // Assert
      assertEquals(Impl2.class, more.core.getClass());
      assertNotNull(((Impl2) more.core).messages);
   }

   @Test
   public void shouldInstantiateEeagerSingleton() {
      // Arrange
      Gin7Injector injector7 = GWT.create(Gin7Injector.class);

      // Act
      Impl2 impl2 = injector7.eagerSingleton();

      // Assert
      assertThat(impl2).isNotNull();
      assertNotNull(impl2.messages);
   }

   /**
    * This is the use case that needs to hold. <code><pre>
    * class Animal {
    * 
    * @Inject Animal (Provider<Sound> soundProvider) { } } </pre></code>
    */
   @SuppressWarnings("unused")
   @Test
   public void shouldInstantiateObjectGraphsContainingProviders() {
      Gin4Injector injector4 = GWT.create(Gin4Injector.class);
      Virtual virtual = injector4.virtual();
   }
}
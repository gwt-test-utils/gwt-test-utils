package com.googlecode.gwt.test.gin;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.Assisted;

public class Injectors {

    public static interface AssistedInjectFactory {

        ClassWithAssistedInjection newClassWithAssistedInjection(String assitedString);

    }

    public static class ClassWithAssistedInjection {

        final String assistedString;

        @Inject
        Virtual virtual;

        @Inject
        public ClassWithAssistedInjection(@Assisted
                                                  String assistedString) {
            this.assistedString = assistedString;
        }
    }

    public static class ClassWithAsyncProvider {

        private final AsyncProvider<Impl2> provider;

        @Inject
        public ClassWithAsyncProvider(AsyncProvider<Impl2> provider) {
            this.provider = provider;
        }

        public void onSuccess(AsyncCallback<Impl2> callback) {
            provider.get(callback);
        }

    }

    @GinModules({Gin1Module.class})
    public interface Gin1Injector extends Ginjector {
        Virtual virtual();
    }

    // This module will only contain a single binding
    public static final class Gin1Module extends AbstractGinModule {
        @Override
        protected void configure() {
            bind(Virtual.class).to(Impl.class).in(Singleton.class);
        }
    }

    @GinModules({Gin2Module.class})
    public interface Gin2Injector extends Ginjector {
        SomeServiceAsync service();

        Virtual virtual();

        VirtualMore virtualMore();
    }

    public static final class Gin2Module extends AbstractGinModule {
        @Override
        protected void configure() {
            bind(Virtual.class).to(Impl2.class);
            bind(VirtualMore.class).to(ImplMore.class);
        }
    }

    @GinModules({Gin3Module.class})
    public interface Gin3Injector extends Ginjector {
        ImplMore implMore();
    }

    public static final class Gin3Module extends AbstractGinModule {
        @Override
        protected void configure() {
            bind(Virtual.class).to(Impl2.class);
        }
    }

    @GinModules(Gin4Module.class)
    public interface Gin4Injector extends Ginjector {
        Virtual virtual();
    }

    public static class Gin4Module extends AbstractGinModule {
        @Override
        protected void configure() {
            bind(Virtual.class).to(ImplementationWithProviders.class);
            bind(VirtualMore.class).to(ImplMore.class);
        }
    }

    @GinModules(Gin5Module.class)
    public interface Gin5Injector extends Ginjector {
        Impl singletonImpl();

        Virtual singletonVirtual();

    }

    public static class Gin5Module extends AbstractGinModule {

        @Override
        protected void configure() {
            bind(Impl.class).in(Singleton.class);

            bind(Virtual.class).to(Impl.class);
        }

    }

    @GinModules(Gin6Module.class)
    public interface Gin6Injector extends Ginjector {
        Virtual singletonImpl();

        Impl3 wrapper();

    }

    public static class Gin6Module extends AbstractGinModule {

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
    public interface Gin7Injector extends Ginjector {
        Impl2 eagerSingleton();
    }

    public static class Gin7Module extends AbstractGinModule {

        @Override
        protected void configure() {
            bind(Impl2.class).asEagerSingleton();
        }
    }

    @GinModules({Gin8Module.class})
    public interface Gin8Injector extends Ginjector {
        ClassWithAsyncProvider classWithAsyncProvider();
    }

    // This module will only contain a single binding
    public static final class Gin8Module extends AbstractGinModule {
        @Override
        protected void configure() {
            bind(VirtualMore.class).to(ImplMore.class);
            bind(Virtual.class).to(Impl.class);
        }
    }

    @GinModules({Gin9Module.class})
    public interface Gin9Injector extends Ginjector {
        AssistedInjectFactory assistedInjectFactory();
    }

    public static class Gin9Module extends AbstractGinModule {

        @Override
        protected void configure() {
            bind(Virtual.class).to(Impl2.class);
            install(new GinFactoryModuleBuilder().build(AssistedInjectFactory.class));
        }

    }

    public static class Impl implements Virtual {
    }

    public static class Impl2 implements Virtual {
        TestMessages messages;

        @Inject
        public Impl2(TestMessages messages) {
            this.messages = messages;
        }
    }

    public static class Impl3 implements Virtual {

        protected final Virtual wrapped;

        public Impl3(Virtual impl) {
            this.wrapped = impl;
        }
    }

    public static class ImplementationWithProviders implements Virtual {

        Provider<VirtualMore> provider;

        @Inject
        public ImplementationWithProviders(Provider<VirtualMore> provider) {
            this.provider = provider;
        }
    }

    public static class ImplMore implements VirtualMore {
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
    public interface Service extends RemoteService {
        String name();
    }

    public interface ServiceAsync {
        void name(AsyncCallback<String> callback);
    }

    public static class ServiceImpl implements Service {

        public String name() {
            return "Service Implementation";
        }

    }

    @RemoteServiceRelativePath("someService")
    public interface SomeService extends RemoteService {
    }

    public interface SomeServiceAsync {
    }

    public static class SomeServiceImpl implements SomeService {

    }

    public interface TestMessages extends Messages {
        @DefaultMessage("this is junit")
        String myName();
    }

    // Simple bindings
    public interface Virtual {
    }

    public interface VirtualMore {
    }

}

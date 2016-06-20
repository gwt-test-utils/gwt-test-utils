package com.googlecode.gwt.test.gin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.gin.Injectors.*;
import com.googlecode.gwt.test.rpc.RemoteServiceCreateHandler;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

public class GInjectorCreateHandlerTest extends GwtTestTest {

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
        // Given
        Gin1Injector injector1 = GWT.create(Gin1Injector.class);

        // When
        Virtual v = injector1.virtual();

        // Then
        assertThat(v.getClass()).isEqualTo(Impl.class);
        assertThat(injector1.virtual()).isSameAs(v);
    }

    @Test
    public void shouldFallbackToGwtCreate() {
        // Given
        Gin2Injector injector2 = GWT.create(Gin2Injector.class);

        // When
        Virtual virtual = injector2.virtual();
        SomeServiceAsync service = injector2.service();

        // Then
        assertThat(virtual.getClass()).isEqualTo(Impl2.class);
        assertThat(injector2.virtual()).isNotSameAs(virtual);
        assertThat(service).isNotNull();
    }

    @Test
    public void shouldInstanciateAsyncProvider() {
        Gin8Injector injector8 = GWT.create(Gin8Injector.class);

        // When
        injector8.classWithAsyncProvider().onSuccess(new AsyncCallback<Impl2>() {

            public void onFailure(Throwable caught) {
                fail("should not fail", caught);

            }

            public void onSuccess(Impl2 result) {
                assertThat(result.messages.myName()).isEqualTo("this is junit");
            }
        });
    }

    // FIXME : we have to make Assisted injection work..
    @Ignore
    @Test
    public void shouldInstanciateClassWithAssistedInjection() {
        // Given
        Gin9Injector injector9 = GWT.create(Gin9Injector.class);

        // When
        ClassWithAssistedInjection o = injector9.assistedInjectFactory().newClassWithAssistedInjection(
                "my assisted string");

        // Then
        assertThat(o.assistedString).isEqualTo("my assisted string");
        assertThat(o.virtual).isInstanceOf(Impl2.class);

    }

    @Test
    public void shouldInstanciateSingletonOnce() {
        // Given
        Gin5Injector injector5 = GWT.create(Gin5Injector.class);

        // When
        Impl impl = injector5.singletonImpl();
        Virtual virtual = injector5.singletonVirtual();

        // Then
        assertThat(virtual).isSameAs(impl);
    }

    @Test
    public void shouldInstanciateUsingProvidesMethod() {
        // Given
        Gin6Injector injector6 = GWT.create(Gin6Injector.class);

        // When
        Impl3 wrapper = injector6.wrapper();

        // Then
        assertThat(wrapper.wrapped).isEqualTo(injector6.singletonImpl());
    }

    @Test
    public void shouldInstantiateComplexObjectGraphs() {
        // Given
        Gin2Injector injector2 = GWT.create(Gin2Injector.class);

        // When
        VirtualMore more = injector2.virtualMore();

        // Then
        assertThat(more.getClass()).isEqualTo(ImplMore.class);
    }

    @Test
    public void shouldInstantiateConcreteComplexObjectGraphs() {
        // Given
        Gin3Injector injector3 = GWT.create(Gin3Injector.class);

        // When
        ImplMore more = injector3.implMore();

        // Then
        assertThat(more.core.getClass()).isEqualTo(Impl2.class);
        assertThat(((Impl2) more.core).messages).isNotNull();
    }

    @Test
    public void shouldInstantiateEeagerSingleton() {
        // Given
        Gin7Injector injector7 = GWT.create(Gin7Injector.class);

        // When
        Impl2 impl2 = injector7.eagerSingleton();

        // Then
        assertThat(impl2).isNotNull();
        assertThat(impl2.messages).isNotNull();
    }

    /**
     * This is the use case that needs to hold. <code><pre>
     * class Animal {
     * <p>
     * @Inject Animal (Provider<Sound> soundProvider) { } } </pre></code>
     */
    @SuppressWarnings("unused")
    @Test
    public void shouldInstantiateObjectGraphsContainingProviders() {
        Gin4Injector injector4 = GWT.create(Gin4Injector.class);
        Virtual virtual = injector4.virtual();
    }
}

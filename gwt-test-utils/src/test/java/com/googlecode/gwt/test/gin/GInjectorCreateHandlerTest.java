package com.googlecode.gwt.test.gin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.gin.Injectors.*;
import com.googlecode.gwt.test.rpc.RemoteServiceCreateHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Fail.fail;
import static org.junit.Assert.*;

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
    public void shouldInstanciateAsyncProvider() {
        Gin8Injector injector8 = GWT.create(Gin8Injector.class);

        // Act
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
        // Arrange
        Gin9Injector injector9 = GWT.create(Gin9Injector.class);

        // Act
        ClassWithAssistedInjection o = injector9.assistedInjectFactory().newClassWithAssistedInjection(
                "my assisted string");

        // Assert
        assertThat(o.assistedString).isEqualTo("my assisted string");
        assertThat(o.virtual).isInstanceOf(Impl2.class);

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
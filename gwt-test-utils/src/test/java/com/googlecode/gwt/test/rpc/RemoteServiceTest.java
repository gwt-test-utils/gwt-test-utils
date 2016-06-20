package com.googlecode.gwt.test.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.server.rpc.AbstractRemoteServiceServlet;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.client.MyObject;
import com.googlecode.gwt.test.exceptions.GwtTestRpcException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class RemoteServiceTest extends GwtTestTest {

    private boolean failure;
    private boolean success;

    @Test
    public void accessToHttpRequest() {
        // Given
        MyServiceAsync myService = GWT.create(MyService.class);
        setServletMockProvider(new ServletMockProviderAdapter() {

            @Override
            public HttpServletRequest getMockedRequest(AbstractRemoteServiceServlet rpcService,
                                                       Method rpcMethod) {

                MockHttpServletRequest mock = new MockHttpServletRequest();
                mock.addHeader("myHeader", "mocked header's value");

                return mock;
            }

        });

        // When
        myService.getHttpRequestHeader("myHeader", new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                fail("onFailure should not be called");
            }

            public void onSuccess(String result) {
                success = true;
                assertThat(result).isEqualTo("mocked header's value");

            }
        });

        // Then
        assertThat(success).isFalse();
        getBrowserSimulator().fireLoopEnd();
        assertThat(success).isTrue();
    }

    @Test
    public void accessToHttpRequest_ThrowsExceptionWhenNoMockConfigured() {
        // Given
        MyServiceAsync myService = GWT.create(MyService.class);

        // When
        try {
            myService.getHttpRequestHeader("myHeader", new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                    fail("onFailure should not be called");
                }

                public void onSuccess(String result) {
                    fail("onSucess should not be called");
                }
            });

            getBrowserSimulator().fireLoopEnd();

            fail("getHttpRequestHeader should have thrown a GwtTestRpcException");
        } catch (GwtTestRpcException e) {
            assertThat(e).hasMessage("Illegal call to com.googlecode.gwt.test.rpc.MyServiceImpl.getThreadLocalRequest() : You have to set a valid ServletMockProvider instance through RemoteServiceTest.setServletMockProvider(..) method");
        }
    }

    @Before
    public void beforeRemoteServiceTest() {
        failure = false;
        success = false;
    }

    @Test
    public void rpcCall_WithException() {
        // Given
        MyServiceAsync myService = GWT.create(MyService.class);

        // When
        myService.someCallWithException(new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {

                assertThat(caught.getMessage()).isEqualTo("Server side thrown exception !!");
                failure = true;
            }

            public void onSuccess(Void result) {
                fail("onSucess should not be called");
            }
        });

        // Then
        assertThat(failure).isFalse();
        getBrowserSimulator().fireLoopEnd();
        assertThat(failure).isTrue();
    }

    @Test
    public void rpcCall_WithSuccess() {
        // Given
        MyObject object = new MyObject("my field initialized during test setup");

        MyServiceAsync myService = GWT.create(MyService.class);

        // When
        myService.update(object, new AsyncCallback<MyObject>() {

            public void onFailure(Throwable caught) {
                fail("onFailure should not be called");
            }

            public void onSuccess(MyObject result) {
                // Then 2
                assertThat(result.getMyField()).isEqualTo("updated field by server side code");
                assertThat(result.getMyTransientField()).isEqualTo("transient field");
                assertThat(result.getMyChildObjects()).hasSize(1);
                assertThat(result.getMyChildObjects().get(0).getMyChildField()).isEqualTo("this is a child !");
                assertThat(result.getMyChildObjects().get(0).getMyChildTransientField()).isEqualTo("child object transient field");
                assertThat(result.getMyChildObjects().get(0).getMyField()).isEqualTo("the field inherited from the parent has been updated !");
                assertThat(result.getMyChildObjects().get(0).getMyTransientField()).isEqualTo("transient field");
                success = true;
            }
        });

        // Then 1
        assertThat(success).isFalse();
        getBrowserSimulator().fireLoopEnd();
        // Then 3
        assertThat(success).isTrue();
    }

}

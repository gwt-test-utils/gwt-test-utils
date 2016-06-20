package com.googlecode.gwt.test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@GwtModule("com.googlecode.gwt.test.GwtTestUtils")
public class GwtRpcWithEasyMockTest extends GwtTestWithEasyMock {

    @Mock
    private MyRemoteServiceAsync mockedService;

    @SuppressWarnings("unchecked")
    @Test
    public void rpcCall_KO() {
        // Given

        // mock future remote call
        mockedService.myMethod(EasyMock.eq("myParamValue"), EasyMock.isA(AsyncCallback.class));
        expectServiceAndCallbackOnFailure(new Exception());

        replay();

        // When
        MyGwtClass gwtClass = new MyGwtClass();
        gwtClass.myValue = "toto";
        assertThat(gwtClass.myValue).isEqualTo("toto");
        gwtClass.run();

        // Then
        verify();

        assertThat(gwtClass.myValue).isEqualTo("error");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void rpcCall_OK() {
        // Given

        // mock future remote call
        mockedService.myMethod(EasyMock.eq("myParamValue"), EasyMock.isA(AsyncCallback.class));
        expectServiceAndCallbackOnSuccess("returnValue");

        replay();

        // When
        MyGwtClass gwtClass = new MyGwtClass();
        gwtClass.myValue = "toto";
        assertThat(gwtClass.myValue).isEqualTo("toto");
        gwtClass.run();

        // Then
        verify();

        assertThat(gwtClass.myValue).isEqualTo("returnValue");
    }

    @Override
    protected String getHostPagePath(String moduleFullQualifiedName) {
        return "test.html";
    }

    static class MyGwtClass {

        public String myValue;

        public void run() {
            MyRemoteServiceAsync service = GWT.create(MyRemoteService.class);

            service.myMethod("myParamValue", new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                    myValue = "error";
                }

                public void onSuccess(String result) {
                    myValue = result;
                }

            });
        }
    }

}

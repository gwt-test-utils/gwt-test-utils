package com.googlecode.gwt.test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@GwtModule("com.googlecode.gwt.test.GwtTestUtils")
public class GwtRpcWithMockitoTest extends GwtTestWithMockito {

    @org.mockito.Mock
    private MyRemoteServiceAsync mockedService;

    @SuppressWarnings("unchecked")
    @Test
    public void rpcCall_KO() {
        // Given
        // mock future remote call
        doFailureCallback(new Exception()).when(mockedService).myMethod(eq("myParamValue"),
                any(AsyncCallback.class));

        // When
        MyGwtClass gwtClass = new MyGwtClass();
        gwtClass.myValue = "toto";
        assertThat(gwtClass.myValue).isEqualTo("toto");
        gwtClass.run();

        // Then
        assertThat(gwtClass.myValue).isEqualTo("error");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void rpcCall_OK() {
        // Given : mock future remote call
        doSuccessCallback("returnValue").when(mockedService).myMethod(eq("myParamValue"),
                any(AsyncCallback.class));

        // When
        MyGwtClass gwtClass = new MyGwtClass();
        gwtClass.myValue = "toto";
        assertThat(gwtClass.myValue).isEqualTo("toto");
        gwtClass.run();

        // Then
        verify(mockedService).myMethod(eq("myParamValue"), any(AsyncCallback.class));

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

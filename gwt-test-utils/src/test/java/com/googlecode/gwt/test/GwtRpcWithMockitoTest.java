package com.googlecode.gwt.test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@GwtModule("com.googlecode.gwt.test.GwtTestUtils")
public class GwtRpcWithMockitoTest extends GwtTestWithMockito {

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

    @org.mockito.Mock
    private MyRemoteServiceAsync mockedService;

    @SuppressWarnings("unchecked")
    @Test
    public void rpcCall_KO() {
        // Arrange

        // mock future remote call
        doFailureCallback(new Exception()).when(mockedService).myMethod(eq("myParamValue"),
                any(AsyncCallback.class));

        // Act
        MyGwtClass gwtClass = new MyGwtClass();
        gwtClass.myValue = "toto";
        assertEquals("toto", gwtClass.myValue);
        gwtClass.run();

        // Assert
        assertEquals("error", gwtClass.myValue);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void rpcCall_OK() {
        // Arrange : mock future remote call
        doSuccessCallback("returnValue").when(mockedService).myMethod(eq("myParamValue"),
                any(AsyncCallback.class));

        // Act
        MyGwtClass gwtClass = new MyGwtClass();
        gwtClass.myValue = "toto";
        assertEquals("toto", gwtClass.myValue);
        gwtClass.run();

        // Assert
        verify(mockedService).myMethod(eq("myParamValue"), any(AsyncCallback.class));

        assertEquals("returnValue", gwtClass.myValue);
    }

    @Override
    protected String getHostPagePath(String moduleFullQualifiedName) {
        return "test.html";
    }

}

package com.googlecode.gwt.test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@GwtModule("com.googlecode.gwt.test.GwtTestUtils")
public class GwtRpcWithEasyMockTest extends GwtTestWithEasyMock {

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

    @Mock
    private MyRemoteServiceAsync mockedService;

    @SuppressWarnings("unchecked")
    @Test
    public void rpcCall_KO() {
        // Arrange

        // mock future remote call
        mockedService.myMethod(EasyMock.eq("myParamValue"), EasyMock.isA(AsyncCallback.class));
        expectServiceAndCallbackOnFailure(new Exception());

        replay();

        // Act
        MyGwtClass gwtClass = new MyGwtClass();
        gwtClass.myValue = "toto";
        assertEquals("toto", gwtClass.myValue);
        gwtClass.run();

        // Assert
        verify();

        assertEquals("error", gwtClass.myValue);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void rpcCall_OK() {
        // Arrange

        // mock future remote call
        mockedService.myMethod(EasyMock.eq("myParamValue"), EasyMock.isA(AsyncCallback.class));
        expectServiceAndCallbackOnSuccess("returnValue");

        replay();

        // Act
        MyGwtClass gwtClass = new MyGwtClass();
        gwtClass.myValue = "toto";
        assertEquals("toto", gwtClass.myValue);
        gwtClass.run();

        // Assert
        verify();

        assertEquals("returnValue", gwtClass.myValue);
    }

    @Override
    protected String getHostPagePath(String moduleFullQualifiedName) {
        return "test.html";
    }

}

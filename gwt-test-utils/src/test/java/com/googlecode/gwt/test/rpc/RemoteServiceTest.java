package com.googlecode.gwt.test.rpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.server.rpc.AbstractRemoteServiceServlet;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.client.MyObject;
import com.googlecode.gwt.test.exceptions.GwtTestRpcException;
import com.googlecode.gwt.test.web.MockHttpServletRequest;

public class RemoteServiceTest extends GwtTestTest {

   private boolean failure;
   private boolean success;

   @Test
   public void accessToHttpRequest() {
      // Arrange
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

      // Act
      myService.getHttpRequestHeader("myHeader", new AsyncCallback<String>() {

         public void onFailure(Throwable caught) {
            fail("onFailure should not be called");
         }

         public void onSuccess(String result) {
            success = true;
            assertEquals("mocked header's value", result);

         }
      });

      // Assert
      assertTrue("The service callback should have been call in a synchronised way", success);
   }

   @Test
   public void accessToHttpRequest_ThrowsExceptionWhenNoMockConfigured() {
      // Arrange
      MyServiceAsync myService = GWT.create(MyService.class);

      // Act
      try {
         myService.getHttpRequestHeader("myHeader", new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
               fail("onFailure should not be called");
            }

            public void onSuccess(String result) {
               fail("onSucess should not be called");
            }
         });

         fail("getHttpRequestHeader should have thrown a GwtTestRpcException");
      } catch (GwtTestRpcException e) {
         assertEquals(
                  "Illegal call to com.googlecode.gwt.test.rpc.MyServiceImpl.getThreadLocalRequest() : You have to set a valid ServletMockProvider instance through RemoteServiceTest.setServletMockProvider(..) method",
                  e.getMessage());
      }
   }

   @Before
   public void beforeRemoteServiceTest() {
      failure = false;
      success = false;
   }

   @Test
   public void rpcCall_WithException() {
      // Arrange
      MyServiceAsync myService = GWT.create(MyService.class);

      // Act
      myService.someCallWithException(new AsyncCallback<Void>() {

         public void onFailure(Throwable caught) {

            assertEquals("Server side thrown exception !!", caught.getMessage());
            failure = true;
         }

         public void onSuccess(Void result) {
            fail("onSucess should not be called");
         }
      });

      // Assert
      assertTrue("The service callback should have been call in a synchronised way", failure);
   }

   @Test
   public void rpcCall_WithSuccess() {
      // Arrange
      MyObject object = new MyObject("my field initialized during test setup");

      MyServiceAsync myService = GWT.create(MyService.class);

      // Act
      myService.update(object, new AsyncCallback<MyObject>() {

         public void onFailure(Throwable caught) {
            fail("onFailure should not be called");
         }

         public void onSuccess(MyObject result) {
            // Assert 1
            assertEquals("updated field by server side code", result.getMyField());
            assertEquals("transient field", result.getMyTransientField());

            assertEquals("A single child object should have been instanciate in server code", 1,
                     result.getMyChildObjects().size());
            assertEquals("this is a child !", result.getMyChildObjects().get(0).getMyChildField());
            assertEquals("child object transient field",
                     result.getMyChildObjects().get(0).getMyChildTransientField());

            assertEquals("the field inherited from the parent has been updated !",
                     result.getMyChildObjects().get(0).getMyField());
            assertEquals("transient field", result.getMyChildObjects().get(0).getMyTransientField());
            success = true;
         }
      });

      // Assert 2
      assertTrue("The service callback should have been call in a synchronised way", success);
   }

}

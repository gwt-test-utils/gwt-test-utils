package com.googlecode.gwt.test;

import java.lang.reflect.Field;
import java.util.Set;

import org.junit.Before;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.gwt.test.exceptions.ReflectionException;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

/**
 * <p>
 * Base class for test classes which use the {@link org.mockito.Mockito Mockito} mocking framework.
 * </p>
 * 
 * Test classes can declare mock objects using fields annotated with Mockito's
 * {@link org.mockito.Mock Mock} annotation. Mock objects not declared using this annotation (e.g.
 * objects instantiated by the {@link Mockito#mock(Class)} method) should be added to the test
 * context using the addMockedObject protected method.
 * 
 * @author Eric Therond
 */
public abstract class GwtTestWithMockito extends GwtTestWithMocks {

   private static class FailureAnswer<T> implements Answer<T> {

      private final Throwable result;

      public FailureAnswer(Throwable result) {
         this.result = result;
      }

      @SuppressWarnings("unchecked")
      public T answer(InvocationOnMock invocation) {
         Object[] arguments = invocation.getArguments();
         AsyncCallback<Object> callback = (AsyncCallback<Object>) arguments[arguments.length - 1];
         callback.onFailure(result);
         return null;
      }

   }

   private static class SuccessAnswer<T> implements Answer<T> {

      private final T result;

      public SuccessAnswer(T result) {
         this.result = result;
      }

      @SuppressWarnings("unchecked")
      public T answer(InvocationOnMock invocation) {
         Object[] arguments = invocation.getArguments();
         AsyncCallback<Object> callback = (AsyncCallback<Object>) arguments[arguments.length - 1];
         callback.onSuccess(result);
         return null;
      }

   }

   @Before
   public void beforeGwtTestWithMockito() {
      MockitoAnnotations.initMocks(this);
      for (Field f : mockFields) {
         GwtReflectionUtils.makeAccessible(f);
         try {
            addMockedObject(f.getType(), f.get(this));
         } catch (Exception e) {
            throw new ReflectionException(
                     "Could not register Mockito mocks declared in test class", e);
         }
      }
   }

   /**
    * Prepares a Mockito stubber that simulates a remote service failure by calling the onFailure()
    * method of the corresponding AsyncCallback object.
    * 
    * @param exception The exception thrown by the stubbed remote service and passed to the callback
    *           onFailure() method
    * 
    * @return a Mockito stubber which will call the callback onFailure() method
    */
   protected <T> Stubber doFailureCallback(final Throwable exception) {
      return Mockito.doAnswer(new FailureAnswer<Object>(exception));
   }

   /**
    * Prepares a Mockito stubber that simulates a remote service success by calling the onSuccess()
    * method of the corresponding AsyncCallback object.
    * 
    * @param object The object returned by the stubbed remote service and passed to the callback
    *           onSuccess() method
    * 
    * @return a Mockito stubber which will call the callback onFailure() method
    */
   protected <T> Stubber doSuccessCallback(final T object) {
      return Mockito.doAnswer(new SuccessAnswer<Object>(object));
   }

   @Override
   protected Set<Field> getMockFields() {
      Set<Field> mocksFields = super.getMockFields();
      Set<Field> mockitoMockFields = GwtReflectionUtils.getAnnotatedField(this.getClass(),
               org.mockito.Mock.class).keySet();
      mocksFields.addAll(mockitoMockFields);
      return mocksFields;
   }

}

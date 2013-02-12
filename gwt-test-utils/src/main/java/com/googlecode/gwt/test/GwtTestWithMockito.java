package com.googlecode.gwt.test;

import java.lang.reflect.Field;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Before;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.configuration.AnnotationEngineHolder;

import com.googlecode.gwt.test.exceptions.ReflectionException;
import com.googlecode.gwt.test.mockito.GwtStubber;
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
 * @author Gael Lazzari
 */
public abstract class GwtTestWithMockito extends GwtTestWithMocks {

   @AfterClass
   public static void resetAnnotationEngineHolder() {
      AnnotationEngineHolder.reset();
   }

   public GwtTestWithMockito() {
      AnnotationEngineHolder.setAnnotationEngine(getCustomAnnotationEngine());
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
    * @return a customised Mockito stubber which will call the callback.onFailure() method
    */
   protected <T> GwtStubber doFailureCallback(final Throwable exception) {
      return new GwtStubberImpl().doFailureCallback(exception);
   }

   /**
    * Prepares a Mockito stubber that simulates a remote service success by calling the onSuccess()
    * method of the corresponding AsyncCallback object.
    * 
    * @param object The object returned by the stubbed remote service and passed to the callback
    *           onSuccess() method
    * 
    * @return a customised Mockito stubber which will call the callback.onSuccess() method
    */
   protected <T> GwtStubber doSuccessCallback(final T object) {
      return new GwtStubberImpl().doSuccessCallback(object);
   }

   protected AnnotationEngine getCustomAnnotationEngine() {
      return null;
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

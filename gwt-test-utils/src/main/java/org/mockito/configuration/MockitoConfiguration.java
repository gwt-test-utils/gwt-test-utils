package org.mockito.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.mockito.ReturnValues;
import org.mockito.exceptions.Reporter;
import org.mockito.stubbing.Answer;

import com.googlecode.gwt.test.Mock;

/**
 * Custom Mockito configuration which relies on a custom AnnotationEngine to be able to mock GWT
 * overlay types and to support {@link Mock} annotation as well as Mockito's ones.
 * 
 * @author Gael Lazzari
 * 
 * @see GwtInjectingAnnotationEngine
 * 
 */
@SuppressWarnings("deprecation")
public class MockitoConfiguration implements IMockitoConfiguration {

   @SuppressWarnings({"unchecked", "rawtypes"})
   static void assertNoAnnotations(Class<? extends Annotation> annotation, Field field,
            Class... undesiredAnnotations) {

      for (Class<? extends Annotation> undesiredAnnotation : undesiredAnnotations) {
         if (field.isAnnotationPresent(undesiredAnnotation)) {
            new Reporter().unsupportedCombinationOfAnnotations(undesiredAnnotation.getSimpleName(),
                     annotation.getSimpleName());
         }
      }
   }

   private final IMockitoConfiguration delegate = new DefaultMockitoConfiguration();

   public boolean cleansStackTrace() {
      return delegate.cleansStackTrace();
   }

   public AnnotationEngine getAnnotationEngine() {
      return new GwtInjectingAnnotationEngine();
   }

   public Answer<Object> getDefaultAnswer() {
      return delegate.getDefaultAnswer();
   }

   public ReturnValues getReturnValues() {
      return delegate.getReturnValues();
   }

}

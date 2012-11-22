package org.mockito.configuration;

import static org.mockito.Mockito.withSettings;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;

/**
 * Overrides Mockito's SpyAnnotationEngine to handle {@link com.googlecode.gwt.test.Mock}
 * annotation.
 * 
 * @author Gael Lazzari
 * 
 */
class GwtSpyAnnotationEngine extends SpyAnnotationEngine {

   @Override
   @SuppressWarnings("deprecation")
   // for MockitoAnnotations.Mock
   public void process(Class<?> context, Object testInstance) {
      Field[] fields = context.getDeclaredFields();
      for (Field field : fields) {
         if (field.isAnnotationPresent(Spy.class) && !field.isAnnotationPresent(InjectMocks.class)) {
            assertNoIncompatibleAnnotations(Spy.class, field, com.googlecode.gwt.test.Mock.class,
                     Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
            Object instance = null;
            try {
               FieldInitializationReport report = new FieldInitializer(testInstance, field).initialize();
               instance = report.fieldInstance();
            } catch (MockitoException e) {
               new Reporter().cannotInitializeForSpyAnnotation(field.getName(), e);
            }
            try {
               if (new MockUtil().isMock(instance)) {
                  // instance has been spied earlier
                  // for example happens when MockitoAnnotations.initMocks is called two times.
                  Mockito.reset(instance);
               } else {
                  field.setAccessible(true);
                  field.set(testInstance, Mockito.mock(
                           instance.getClass(),
                           withSettings().spiedInstance(instance).defaultAnswer(
                                    Mockito.CALLS_REAL_METHODS).name(field.getName())));
               }
            } catch (IllegalAccessException e) {
               throw new MockitoException("Problems initiating spied field " + field.getName(), e);
            }
         }
      }
   }

   // TODO duplicated elsewhere
   void assertNoIncompatibleAnnotations(Class<? extends Annotation> annotation, Field field,
            Class<? extends Annotation>... undesiredAnnotations) {
      for (Class<? extends Annotation> u : undesiredAnnotations) {
         if (field.isAnnotationPresent(u)) {
            new Reporter().unsupportedCombinationOfAnnotations(annotation.getSimpleName(),
                     annotation.getClass().getSimpleName());
         }
      }
   }

}

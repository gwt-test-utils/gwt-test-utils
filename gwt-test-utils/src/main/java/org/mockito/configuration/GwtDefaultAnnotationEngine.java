package org.mockito.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.DefaultAnnotationEngine;
import org.mockito.internal.util.reflection.GenericMaster;

import com.google.gwt.dev.shell.JsValueGlue;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import com.googlecode.gwt.test.internal.GwtFactory;

/**
 * Overrides Mockito's DefaultAnnotationEngine to create mock for Overlay types and to handle
 * {@link com.googlecode.gwt.test.Mock} annotation.
 * 
 * 
 * @author Gael Lazzari
 * 
 */
public class GwtDefaultAnnotationEngine extends DefaultAnnotationEngine {

   @Override
   @SuppressWarnings("deprecation")
   public Object createMockFor(Annotation annotation, Field field) {
      if (annotation instanceof Mock) {
         return processAnnotationOn((Mock) annotation, field);
      }
      if (annotation instanceof com.googlecode.gwt.test.Mock) {
         return processAnnotationOn((com.googlecode.gwt.test.Mock) annotation, field);
      }
      if (annotation instanceof MockitoAnnotations.Mock) {
         return processAnnotationOn((MockitoAnnotations.Mock) annotation, field);
      }
      if (annotation instanceof Captor) {
         return processAnnotationOn((Captor) annotation, field);
      }

      return null;
   }

   private Class<?> getTypeToMock(Field f) {
      if (GwtFactory.get().getOverlayRewriter().isJsoIntf(f.getType().getName())) {
         try {
            return Class.forName(JsValueGlue.JSO_IMPL_CLASS);
         } catch (ClassNotFoundException e) {
            // should never happen
            throw new GwtTestPatchException("Error while creating a mock with Mockito for "
                     + f.getType().getName(), e);
         }
      } else {
         return f.getType();
      }
   }

   private Object processAnnotationOn(Captor annotation, Field field) {
      Class<?> type = field.getType();
      if (!ArgumentCaptor.class.isAssignableFrom(type)) {
         throw new MockitoException(
                  "@Captor field must be of the type ArgumentCaptor.\n"
                           + "Field: '"
                           + field.getName()
                           + "' has wrong type\n"
                           + "For info how to use @Captor annotations see examples in javadoc for MockitoAnnotations class.");
      }
      Class<?> cls = new GenericMaster().getGenericType(field);
      return ArgumentCaptor.forClass(cls);
   }

   private Object processAnnotationOn(com.googlecode.gwt.test.Mock annotation, Field field) {
      return Mockito.mock(getTypeToMock(field), field.getName());
   }

   private Object processAnnotationOn(Mock annotation, Field field) {
      MockSettings mockSettings = Mockito.withSettings();
      if (annotation.extraInterfaces().length > 0) { // never null
         mockSettings.extraInterfaces(annotation.extraInterfaces());
      }
      if ("".equals(annotation.name())) {
         mockSettings.name(field.getName());
      } else {
         mockSettings.name(annotation.name());
      }

      // see @Mock answer default value
      mockSettings.defaultAnswer(annotation.answer().get());
      return Mockito.mock(getTypeToMock(field), mockSettings);
   }

   @SuppressWarnings("deprecation")
   private Object processAnnotationOn(org.mockito.MockitoAnnotations.Mock annotation, Field field) {
      return Mockito.mock(getTypeToMock(field), field.getName());
   }

}

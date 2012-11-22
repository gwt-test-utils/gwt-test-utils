package org.mockito.configuration;

import java.lang.reflect.Field;

import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.internal.configuration.FieldAnnotationProcessor;
import org.mockito.internal.configuration.MockAnnotationProcessor;

/**
 * Overrides {@link MockAnnotationProcessor} to manage Overlay types mocking using {@link Mock} from
 * Mockito.
 */
class MockitoMockAnnotationProcessor implements FieldAnnotationProcessor<Mock> {
   public Object process(Mock annotation, Field field) {
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
      return Mockito.mock(MockAnnotationProcessorHelper.getTypeToMock(field), mockSettings);
   }
}

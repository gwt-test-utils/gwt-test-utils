package org.mockito.configuration;

import java.lang.reflect.Field;

import org.mockito.Mockito;
import org.mockito.internal.configuration.FieldAnnotationProcessor;
import org.mockito.internal.configuration.MockAnnotationProcessor;

import com.googlecode.gwt.test.Mock;

/**
 * Overrides {@link MockAnnotationProcessor} to manage Overlay types mocking using {@link Mock} from
 * gwt-test-utils.
 */
class GwtMockAnnotationProcessor implements FieldAnnotationProcessor<Mock> {

   public Object process(Mock annotation, Field field) {
      // see @Mock answer default value
      return Mockito.mock(MockAnnotationProcessorHelper.getTypeToMock(field));
   }
}

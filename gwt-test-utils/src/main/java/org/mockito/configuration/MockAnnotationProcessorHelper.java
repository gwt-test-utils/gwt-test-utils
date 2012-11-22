package org.mockito.configuration;

import java.lang.reflect.Field;

import com.google.gwt.dev.shell.JsValueGlue;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import com.googlecode.gwt.test.internal.GwtFactory;

/**
 * Helper for MockitoAnnotationProcessor subclasses
 * 
 * @author Gael Lazzari
 * 
 */
class MockAnnotationProcessorHelper {

   /**
    * Return the exact type to mock, regarding if it's an Overlay type or not.
    * 
    * @param f The field to be mocked
    * @return The type to actually mock
    */
   static Class<?> getTypeToMock(Field f) {
      GwtFactory gwtFactory = GwtFactory.get();
      if (gwtFactory != null && gwtFactory.getOverlayRewriter() != null
               && gwtFactory.getOverlayRewriter().isJsoIntf(f.getType().getName())) {
         try {
            return Class.forName(JsValueGlue.JSO_IMPL_CLASS);
         } catch (ClassNotFoundException e) {
            // should never happen
            throw new GwtTestPatchException("Error while creating a mock with Mockito for "
                     + f.getType().getName(), e);
         }
      } else {
         // null GwtFactory means the test is a mockito test not running with gwt-test-utils, e.g.
         // @RunWith(GwtRunner.class)
         // null OverlayRewriter means JavaScriptObject class has not been found in the class set to
         // compile (should never happen in a gwt-test-utils test)
         return f.getType();
      }
   }

}

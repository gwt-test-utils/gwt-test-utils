package com.googlecode.gwt.test.mockito;

import com.google.gwt.dev.shell.JsValueGlue;
import com.googlecode.gwt.test.Mock;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import com.googlecode.gwt.test.internal.GwtFactory;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Utils for Mockito Mocks.
 *
 * @author Gael Lazzari
 */
class GwtMockitoUtils {

    static <T extends Annotation> Set<Field> getMockFields(Class<?> clazz, Class<T> mockAnnotation) {
        return new HashSet<>(GwtReflectionUtils.getAnnotatedField(clazz, mockAnnotation).keySet());
    }

    /**
     * Return the exact type to mock, regarding if it's an Overlay type or not.
     *
     * @param f The field to be mocked
     * @return The type to actually mock
     */
    static Class<?> getTypeToMock(Field f) {
        GwtFactory gwtFactory = GwtFactory.get();
        if (gwtFactory != null && gwtFactory.getOverlayRewriter() != null) {
            if (gwtFactory.getOverlayRewriter().isJsoIntf(f.getType().getName())) {
                try {
                    return Class.forName(JsValueGlue.JSO_IMPL_CLASS);
                } catch (ClassNotFoundException e) {
                    // should never happen
                    throw new GwtTestPatchException("Error while creating a mock with Mockito for "
                            + f.getType().getName(), e);
                }
            }
            else return f.getType();
        } else {
            // null GwtFactory means the test is a mockito test not running with gwt-test-utils, e.g.
            // @RunWith(GwtRunner.class)
            // null OverlayRewriter means JavaScriptObject class has not been found in the class set to
            // compile (should never happen in a gwt-test-utils test)
            return f.getType();
        }
    }
}

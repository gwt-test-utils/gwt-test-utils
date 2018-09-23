package com.googlecode.gwt.test.mockito;

import com.googlecode.gwt.test.GwtTestWithMockito;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

public class GwtMockitoAnnotations {

    public static void initMocks(GwtTestWithMockito testInstance) {
        // @Mock, @Spy... from mockito
        MockitoAnnotations.initMocks(testInstance);
        registerMocks(testInstance, org.mockito.Mock.class);

        GwtAnnotationEngine gwtAnnotationEngine = new GwtAnnotationEngine();
        // Custom @Mock annotation
        gwtAnnotationEngine.process(testInstance.getClass(), testInstance);
        registerMocks(testInstance, com.googlecode.gwt.test.Mock.class);
        // Process @InjectMocks with custom @Mock annotation
        processInjectMocks(testInstance);
    }

    private static void registerMocks(GwtTestWithMockito testInstance, Class<? extends Annotation> mockClass) {
        Set<Field> fields = GwtMockitoUtils.getMockFields(testInstance.getClass(), mockClass);
        fields.forEach(field -> {
            try {
                GwtReflectionUtils.makeAccessible(field);
                testInstance.addMockedObject(field.getType(), field.get(testInstance));
            } catch (IllegalAccessException e) {
                // should never append
                throw new GwtTestException("");
            }
        });
    }

    private static void processInjectMocks(GwtTestWithMockito testInstance) {
        GwtMockitoUtils.getMockFields(testInstance.getClass(), InjectMocks.class).forEach(injectMocksField -> {
            try {
                GwtReflectionUtils.makeAccessible(injectMocksField);
                Object injectMocks = injectMocksField.get(testInstance);
                testInstance.getAllMocksByType().forEach((key, value) ->
                        GwtReflectionUtils.getFields(injectMocks.getClass())
                                .stream()
                                .filter(field -> field.getType().isAssignableFrom(key))
                                .forEach(field -> {
                                    try {
                                        GwtReflectionUtils.makeAccessible(field);
                                        Object mock = field.get(injectMocks);
                                        if (mock == null) {
                                            field.set(injectMocks, value);
                                        }
                                    } catch (IllegalAccessException e) {
                                        // should never append
                                        throw new GwtTestException("", e);
                                    }
                                }));
            } catch (IllegalAccessException e) {
                // should never append
                throw new GwtTestException("", e);
            }
        });
    }
}

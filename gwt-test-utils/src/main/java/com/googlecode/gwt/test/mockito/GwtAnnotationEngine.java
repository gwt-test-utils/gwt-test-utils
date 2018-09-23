package com.googlecode.gwt.test.mockito;

import com.googlecode.gwt.test.Mock;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;
import org.mockito.plugins.AnnotationEngine;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.withSettings;

class GwtAnnotationEngine implements AnnotationEngine {

    @Override
    public void process(Class<?> clazz, Object testInstance) {
        getMockFields(clazz).forEach(field -> injectMock(testInstance, field));

    }

    private Set<Field> getMockFields(Class<?> clazz) {
        return new HashSet<>(GwtReflectionUtils.getAnnotatedField(clazz, Mock.class).keySet());
    }

    private void injectMock(Object testInstance, Field field) {
        GwtReflectionUtils.makeAccessible(field);

        try {
            Object mock = createMock(testInstance, field);
            field.set(testInstance, mock);
        } catch (IllegalAccessException e) {
            throw new MockitoException("Problems initiating spied field " + field.getName(), e);
        }
    }

    private Object createMock(Object testInstance, Field field) {
        try {
            Object instance = field.get(testInstance);
            if (MockUtil.isMock(instance)) {
                // instance has been spied earlier
                // for example happens when MockitoAnnotations.initMocks is called two times.
                Mockito.reset(instance);
                return instance;
            } else {
                return Mockito.mock(
                        GwtMockitoUtils.getTypeToMock(field),
                        withSettings().name(field.getName())
                );
            }
        } catch (IllegalAccessException e) {
            throw new MockitoException("Problems initiating spied field " + field.getName(), e);
        }
    }
}

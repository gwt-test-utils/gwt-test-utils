package org.mockito.configuration;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.internal.configuration.injection.scanner.MockScanner;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.FieldReader;

import java.lang.reflect.Field;
import java.util.Set;

import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

/**
 * Copied and adapted from {@link MockScanner}, to add gwt-test-utils
 * {@link com.googlecode.gwt.test.Mock} annotation.
 */
class GwtMockScanner {
    private final Class<?> clazz;
    private final Object instance;
    private final MockUtil mockUtil = new MockUtil();

    /**
     * Creates a MockScanner.
     *
     * @param instance The test instance
     * @param clazz    The class in the type hierarchy of this instance.
     */
    public GwtMockScanner(Object instance, Class<?> clazz) {
        this.instance = instance;
        this.clazz = clazz;
    }

    /**
     * Add the scanned and prepared mock instance to the given collection.
     * <p>
     * <p>
     * The preparation of mocks consists only in defining a MockName if not already set.
     * </p>
     *
     * @param mocks Set of mocks
     */
    public void addPreparedMocks(Set<Object> mocks) {
        mocks.addAll(scan());
    }

    @SuppressWarnings("deprecation")
    private boolean isAnnotatedByMockOrSpy(Field field) {
        return null != field.getAnnotation(Spy.class) || null != field.getAnnotation(Mock.class)
                || null != field.getAnnotation(com.googlecode.gwt.test.Mock.class)
                || null != field.getAnnotation(MockitoAnnotations.Mock.class);
    }

    private boolean isMockOrSpy(Object instance) {
        return mockUtil.isMock(instance) || mockUtil.isSpy(instance);
    }

    private Object preparedMock(Object instance, Field field) {
        if (isAnnotatedByMockOrSpy(field)) {
            return instance;
        } else if (isMockOrSpy(instance)) {
            mockUtil.maybeRedefineMockName(instance, field.getName());
            return instance;
        }
        return null;
    }

    /**
     * Scan and prepare mocks for the given <code>testClassInstance</code> and <code>clazz</code> in
     * the type hierarchy.
     *
     * @return A prepared set of mock
     */
    private Set<Object> scan() {
        Set<Object> mocks = newMockSafeHashSet();
        for (Field field : clazz.getDeclaredFields()) {
            // mock or spies only
            FieldReader fieldReader = new FieldReader(instance, field);

            Object mockInstance = preparedMock(fieldReader.read(), field);
            if (mockInstance != null) {
                mocks.add(mockInstance);
            }
        }
        return mocks;
    }
}

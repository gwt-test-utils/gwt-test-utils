package com.googlecode.gwt.test;

import com.googlecode.gwt.test.internal.AfterTestCallback;
import com.googlecode.gwt.test.internal.AfterTestCallbackManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class MockManager implements AfterTestCallback {

    private static final MockManager INSTANCE = new MockManager();

    public static MockManager get() {
        return INSTANCE;
    }

    private Map<Class<?>, Object> mockObjects = new HashMap<>();

    private MockManager() {
        AfterTestCallbackManager.get().registerCallback(this);
    }

    <T> T registerMock(Class<T> clazz, T mock) {
        mockObjects.put(clazz, mock);
        return mock;
    }

    void registerMocks(Map<Class<?>, Object> mocks) {
        mockObjects.putAll(mocks);
    }

    <T> T getMock(Class<T> clazz) {
        return (T) mockObjects.get(clazz);
    }

    Map<Class<?>, Object> getAllMocksByType() {
        return mockObjects;
    }

    @Override
    public void afterTest() {
        this.mockObjects.clear();
    }
}

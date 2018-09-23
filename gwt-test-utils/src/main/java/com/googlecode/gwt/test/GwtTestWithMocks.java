package com.googlecode.gwt.test;

import com.google.gwt.user.client.rpc.RemoteService;
import com.googlecode.gwt.test.internal.handlers.GwtTestGWTBridge;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Base class for test classes which make use of a mocking framework, such as
 * {@link org.easymock.EasyMock EasyMock} or {@link org.mockito.Mockito Mockito} .
 * </p>
 * <p>
 * <p>
 * This class provides methods to register mock objects into the context of a test class. This is
 * required so that application calls to {@link com.google.gwt.core.client.GWT#create(Class)
 * GWT.Create(MyClassToMock)} will return the corresponding mock object of type MyClassToMock.
 * </p>
 *
 * @author Eric Therond
 * @author Gael Lazzari
 */
public abstract class GwtTestWithMocks extends GwtTest {

    private class MockCreateHandler implements GwtCreateHandler {

        private final MockManager mockManager;

        public MockCreateHandler(MockManager mockManager) {
            this.mockManager = mockManager;
        }

        public Object create(Class<?> classLiteral) throws Exception {
            if (RemoteService.class.isAssignableFrom(classLiteral)) {
                String asyncName = classLiteral.getName() + "Async";
                classLiteral = GwtReflectionUtils.getClass(asyncName);
            }
            return mockManager.getMock(classLiteral);
        }

    }

    private final MockManager mockManager;

    public GwtTestWithMocks(MockManager mockManager) {
        this.mockManager = mockManager;
        GwtTestGWTBridge.get().setMockCreateHandler(new MockCreateHandler(mockManager));
    }

    /**
     * Adds a mock object to the list of mocks used in the context of this test class.
     *
     * @param createClass The class for which a mock object is being defined
     * @param mock        the mock instance
     */
    public <T> T addMockedObject(Class<?> createClass, Object mock) {
        return MockManager.get().registerMock((Class<T>)createClass, (T) mock);
    }

    public MockManager getMockManager() {
        return mockManager;
    }



}

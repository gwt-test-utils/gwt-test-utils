package com.googlecode.gwt.test;

import com.google.gwt.dev.shell.JsValueGlue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import com.googlecode.gwt.test.exceptions.ReflectionException;
import com.googlecode.gwt.test.internal.BrowserSimulatorImpl;
import com.googlecode.gwt.test.internal.GwtFactory;
import com.googlecode.gwt.test.internal.handlers.GwtTestGWTBridge;
import com.googlecode.gwt.test.internal.utils.ArrayUtils;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import com.googlecode.gwt.test.utils.GwtReflectionUtils.MethodCallback;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.easymock.IExpectationSetters;
import org.junit.Before;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <p>
 * Base class for test classes which make use of the {@link org.easymock.EasyMock EasyMock} mocking
 * framework.
 * </p>
 * <p>
 * <p>
 * Those classes can declare fields annotated with {@link Mock @Mock}, which will result in the
 * injection of mock objects of the corresponding type.
 * <p>
 * Mock objects not declared using this annotation (e.g. objects instantiated by calling directly
 * the {@link EasyMock#createMock(Class)} should be added to the test context using the
 * addMockedObject protected method.
 * </p>
 * <p>
 * <p>
 * Mock objects can then be manipulated using the standard EasyMock API, or with the helper methods
 * provided by GwtTestWithEasyMock.
 * </p>
 *
 * @author Bertrand Paquet
 * @author Gael Lazzari
 */
public abstract class GwtTestWithEasyMock extends GwtTestWithMocks {

    private static class FailureAnswer<T extends Throwable> implements IAnswer<T> {

        private final T result;

        public FailureAnswer(T result) {
            this.result = result;
        }

        @SuppressWarnings("unchecked")
        public T answer() throws Throwable {
            Object[] arguments = EasyMock.getCurrentArguments();
            AsyncCallback<T> callback = (AsyncCallback<T>) arguments[arguments.length - 1];
            callback.onFailure(result);
            return null;
        }

    }

    private static class SuccessAnswer<T> implements IAnswer<T> {

        private final T result;

        public SuccessAnswer(T result) {
            this.result = result;
        }

        @SuppressWarnings("unchecked")
        public T answer() throws Throwable {
            final Object[] arguments = EasyMock.getCurrentArguments();
            AsyncCallback<T> callback = (AsyncCallback<T>) arguments[arguments.length - 1];
            callback.onSuccess(result);
            return null;
        }

    }

    private List<Class<Object>> mockedClasses = new ArrayList<>();
    private Set<Field> mockFields;

    public GwtTestWithEasyMock() {
        super(MockManager.get());
        mockFields = getMockFields();
        for (Field f : mockFields) {
            mockedClasses.add((Class<Object>) f.getType());
        }
    }

    @Before
    public void beforeGwtTestWithEasyMock() {
        mockedClasses.forEach(clazz ->
                getMockManager().registerMock(clazz, createMock(clazz))
        );
        try {
            for (Field f : mockFields) {
                Object mock = getMockManager().getMock(f.getType());
                GwtReflectionUtils.makeAccessible(f);
                f.set(this, mock);
            }
        } catch (Exception e) {
            if (GwtTestException.class.isInstance(e)) {
                throw (GwtTestException) e;
            } else {
                throw new ReflectionException("Error during gwt-test-utils @Mock creation", e);
            }
        }
    }

    /**
     * Creates a mock object for a given class, where all methods are mocked except the ones given as
     * parameters.
     *
     * @param clazz       The class for which a mock object will be created
     * @param keepSetters False if setters should be mocked, true otherwise
     * @param list        List of methods that should not be mocked
     */
    protected <T> T createMockAndKeepMethods(Class<T> clazz, final boolean keepSetters,
                                             final Method... list) {
        final List<Method> l = new ArrayList<>();
        GwtReflectionUtils.doWithMethods(clazz, method -> {
            if (!ArrayUtils.contains(list, method)) {
                if (!keepSetters || !method.getName().startsWith("set")
                        || method.getReturnType() != void.class) {
                    l.add(method);
                }
            }
        });
        T o = EasyMock.createMockBuilder(clazz).addMockedMethods(l.toArray(new Method[]{})).createMock();
        addMockedObject(clazz, o);
        return o;
    }

    /**
     * Creates a mock object for a given class, where all methods are mocked except the one with
     * specifics name and parameter types.
     *
     * @param <T>         The result mock type.
     * @param clazz       The result mock type.
     * @param methodName  The name of the method to keep.
     * @param paramsTypes The parameter of the method to keep.
     * @return The resulting mock object.
     */
    protected <T> T createMockAndKeepOneMethod(Class<T> clazz, String methodName,
                                               Class<?>... paramsTypes) {
        return createMockAndKeepMethods(clazz, true,
                GwtReflectionUtils.findMethod(clazz, methodName, paramsTypes));
    }

    /**
     * Records a call to an asynchronous service and simulates a failure by calling the onFailure()
     * method of the corresponding AsyncCallback object.
     *
     * @param exception The exception thrown by the stubbed remote service and passed to the callback
     *                  onFailure() method.
     * @return the value returned by the delegated call.
     */
    @SuppressWarnings("unchecked")
    protected <T extends Throwable> IExpectationSetters<T> expectServiceAndCallbackOnFailure(
            final T exception) {
        IAnswer<T> answer = new FailureAnswer<T>(exception);
        Object o = EasyMock.expectLastCall().andAnswer(answer);
        return (IExpectationSetters<T>) o;
    }

    /**
     * Records a call to an asynchronous service and simulates a success by calling the onSuccess()
     * method of the corresponding AsyncCallback object.
     *
     * @param object The object returned by the stubbed remote service and passed to the callback
     *               onSuccess() method.
     * @return the value returned by the delegated call.
     */
    @SuppressWarnings("unchecked")
    protected <T> IExpectationSetters<T> expectServiceAndCallbackOnSuccess(final T object) {
        IAnswer<T> answer = new SuccessAnswer<T>(object);
        return (IExpectationSetters<T>) EasyMock.expectLastCall().andAnswer(answer);
    }

    /**
     * Set all declared mocks to replay state.
     */
    protected void replay() {
        getMockManager().getAllMocksByType().values().forEach(EasyMock::replay);
    }

    /**
     * Reset all declared mocks.
     */
    protected void reset() {
        getMockManager().getAllMocksByType().values().forEach(EasyMock::reset);
    }

    /**
     * Verifies that all recorded behaviors for every declared mock has actually been used.
     */
    protected void verify() {
        // trigger commands
        getBrowserSimulator().fireLoopEnd();

        getMockManager().getAllMocksByType().values().forEach(EasyMock::verify);
    }

    private <T> T createMock(Class<T> clazz) {

        if (GwtFactory.get().getOverlayRewriter().isJsoIntf(clazz.getName())) {
            try {
                return (T) EasyMock.createMock(Class.forName(JsValueGlue.JSO_IMPL_CLASS));
            } catch (ClassNotFoundException e) {
                // should never happen
                throw new GwtTestPatchException("Error while creating a mock with EasyMock for "
                        + clazz.getName(), e);
            }
        } else {
            return EasyMock.createMock(clazz);
        }
    }

    private Set<Field> getMockFields() {
        return new HashSet<>(GwtReflectionUtils.getAnnotatedField(this.getClass(), Mock.class).keySet());
    }

}

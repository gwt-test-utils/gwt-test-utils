package com.googlecode.gwt.test;

import com.googlecode.gwt.test.mockito.GwtMockitoAnnotations;
import com.googlecode.gwt.test.mockito.GwtStubber;
import org.junit.Before;
import org.mockito.Mockito;
import org.mockito.quality.Strictness;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * Base class for test classes which use the {@link org.mockito.Mockito Mockito} mocking framework.
 * </p>
 * <p>
 * Test classes can declare mock objects using fields annotated with Mockito's
 * {@link org.mockito.Mock Mock} annotation. Mock objects not declared using this annotation (e.g.
 * objects instantiated by the {@link Mockito#mock(Class)} method) should be added to the test
 * context using the addMockedObject protected method.
 *
 * @author Eric Therond
 * @author Gael Lazzari
 */
public abstract class GwtTestWithMockito extends GwtTestWithMocks {

    public GwtTestWithMockito() {
        super(MockManager.get());
    }

    @Before
    public void beforeGwtTestWithMockito() {
        GwtMockitoAnnotations.initMocks(this);
    }

    public Map<Class<?>, Object> getAllMocksByType() {
        return getMockManager().getAllMocksByType();
    }

    /**
     * Prepares a Mockito stubber that simulates a remote service failure by calling the onFailure()
     * method of the corresponding AsyncCallback object.
     *
     * @param exception The exception thrown by the stubbed remote service and passed to the callback
     *                  onFailure() method
     * @return a customised Mockito stubber which will call the callback.onFailure() method
     */
    protected <T> GwtStubber doFailureCallback(final Throwable exception) {
        return new GwtStubberImpl(Strictness.STRICT_STUBS).doFailureCallback(exception);
    }

    /**
     * Prepares a Mockito stubber that simulates a remote service success by calling the onSuccess()
     * method of the corresponding AsyncCallback object.
     *
     * @param object The object returned by the stubbed remote service and passed to the callback
     *               onSuccess() method
     * @return a customised Mockito stubber which will call the callback.onSuccess() method
     */
    protected <T> GwtStubber doSuccessCallback(final T object) {
        return new GwtStubberImpl(Strictness.STRICT_STUBS).doSuccessCallback(object);
    }

}

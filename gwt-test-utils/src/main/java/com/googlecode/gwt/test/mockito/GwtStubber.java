package com.googlecode.gwt.test.mockito;

import org.mockito.stubbing.Stubber;

/**
 * A customized Mockito Stubber to be able to invoke an async method many times with different
 * result.
 *
 * @author Gael Lazzari
 */
public interface GwtStubber extends Stubber {

    /**
     * Prepares a Mockito stubber that simulates a remote service failure by calling the onFailure()
     * method of the corresponding AsyncCallback object.
     *
     * @param exception The exception thrown by the stubbed remote service and passed to the callback
     *                  onFailure() method
     * @return a customised Mockito stubber which will call the callback.onFailure() method
     */
    <T> GwtStubber doFailureCallback(final Throwable exception);

    /**
     * Prepares a Mockito stubber that simulates a remote service success by calling the onSuccess()
     * method of the corresponding AsyncCallback object.
     *
     * @param object The object returned by the stubbed remote service and passed to the callback
     *               onSuccess() method
     * @return a customised Mockito stubber which will call the callback.onSuccess() method
     */
    <T> GwtStubber doSuccessCallback(final T object);

}

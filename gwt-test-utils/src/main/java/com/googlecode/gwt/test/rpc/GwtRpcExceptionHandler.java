package com.googlecode.gwt.test.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * <p>
 * An interface which exposes one callback method to be called when an exception occurs during a RPC
 * service invocation.
 * </p>
 * <p>
 * <p>
 * The used instance of GwtRpcExceptionHandler is specified by
 * {@link RemoteServiceCreateHandler#getExceptionHandler()}, which can be overriden.
 * </p>
 *
 * @author Bertrand Paquet
 */
public interface GwtRpcExceptionHandler {

    /**
     * The callback method which is called if the RPC service invocation throws an exception.
     *
     * @param t        The thrown exception.
     * @param callback The asynchrone callback provided by the RPC service caller.
     */
    void handle(Throwable t, AsyncCallback<?> callback);

}

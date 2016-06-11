package com.googlecode.gwt.test.exceptions;

/**
 * Exception thrown when an error occurs during a RPC call simulation.
 *
 * @author Gael Lazzari
 */
public class GwtTestRpcException extends GwtTestException {

    private static final long serialVersionUID = -7518261539641239910L;

    public GwtTestRpcException() {
    }

    public GwtTestRpcException(String message) {
        super(message);
    }

    public GwtTestRpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtTestRpcException(Throwable cause) {
        super(cause);
    }
}

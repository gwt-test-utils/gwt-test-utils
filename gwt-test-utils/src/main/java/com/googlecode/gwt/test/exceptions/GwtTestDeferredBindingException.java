package com.googlecode.gwt.test.exceptions;

/**
 * Exception thrown when an error occurs during a deferred binding call simulation (e.g.
 * GWT.create(..))
 *
 * @author Gael Lazzari
 */
public class GwtTestDeferredBindingException extends GwtTestException {

    private static final long serialVersionUID = -7518261539641239910L;

    public GwtTestDeferredBindingException() {
    }

    public GwtTestDeferredBindingException(String message) {
        super(message);
    }

    public GwtTestDeferredBindingException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtTestDeferredBindingException(Throwable cause) {
        super(cause);
    }
}

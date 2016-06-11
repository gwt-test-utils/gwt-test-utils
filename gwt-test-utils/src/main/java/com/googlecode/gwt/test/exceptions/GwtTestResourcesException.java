package com.googlecode.gwt.test.exceptions;

import com.google.gwt.resources.client.ClientBundle;

/**
 * Exception thrown when an error occurs during a GWT resources emulation.
 *
 * @author Gael Lazzari
 * @see ClientBundle
 */
public class GwtTestResourcesException extends GwtTestException {

    private static final long serialVersionUID = -7518261539641239910L;

    public GwtTestResourcesException() {
    }

    public GwtTestResourcesException(String message) {
        super(message);
    }

    public GwtTestResourcesException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtTestResourcesException(Throwable cause) {
        super(cause);
    }
}

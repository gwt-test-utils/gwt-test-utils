package com.googlecode.gwt.test.exceptions;

/**
 * Exception thrown when an error occurs during GWT DOM manipulation.
 *
 * @author Gael Lazzari
 */
public class GwtTestDomException extends GwtTestException {

    private static final long serialVersionUID = -7518261539641239910L;

    public GwtTestDomException() {
    }

    public GwtTestDomException(String message) {
        super(message);
    }

    public GwtTestDomException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtTestDomException(Throwable cause) {
        super(cause);
    }
}

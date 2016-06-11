package com.googlecode.gwt.test.exceptions;

/**
 * gwt-test-utils base exception.
 *
 * @author Gael Lazzari
 */
public class GwtTestException extends RuntimeException {

    private static final long serialVersionUID = 5806774621682061491L;

    public GwtTestException() {
    }

    public GwtTestException(String message) {
        super(message);
    }

    public GwtTestException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtTestException(Throwable cause) {
        super(cause);
    }

}

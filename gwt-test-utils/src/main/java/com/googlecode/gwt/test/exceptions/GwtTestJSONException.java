package com.googlecode.gwt.test.exceptions;

/**
 * Exception thrown when an error occurs during a GWT JSON emulation.
 *
 * @author Gael Lazzari
 */
public class GwtTestJSONException extends GwtTestException {

    private static final long serialVersionUID = -7518261539641239910L;

    public GwtTestJSONException() {
    }

    public GwtTestJSONException(String message) {
        super(message);
    }

    public GwtTestJSONException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtTestJSONException(Throwable cause) {
        super(cause);
    }
}

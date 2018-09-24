package com.googlecode.gwt.test.gin;

import com.googlecode.gwt.test.exceptions.GwtTestException;

/**
 * Exception thrown when gwt-test-utils detects an error in a google-gin configuration
 *
 * @author Gael Lazzari
 */
public class GwtTestGinException extends GwtTestException {

    /**
     *
     */
    public GwtTestGinException() {
    }

    /**
     * @param message
     */
    public GwtTestGinException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public GwtTestGinException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public GwtTestGinException(Throwable cause) {
        super(cause);
    }
}

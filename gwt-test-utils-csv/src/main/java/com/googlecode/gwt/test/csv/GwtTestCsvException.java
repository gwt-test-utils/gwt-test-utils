package com.googlecode.gwt.test.csv;

import com.googlecode.gwt.test.exceptions.GwtTestException;

/**
 * Exception thrown when an error occurs during the manipulation of the gwt-test-utils CSV API.
 *
 * @author Gael Lazzari
 */
public class GwtTestCsvException extends GwtTestException {

    private static final long serialVersionUID = -7518261539641239910L;

    public GwtTestCsvException() {
    }

    public GwtTestCsvException(String message) {
        super(message);
    }

    public GwtTestCsvException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtTestCsvException(Throwable cause) {
        super(cause);
    }
}

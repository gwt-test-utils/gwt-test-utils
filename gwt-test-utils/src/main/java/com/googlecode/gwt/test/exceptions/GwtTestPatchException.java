package com.googlecode.gwt.test.exceptions;

/**
 * Exception thrown when an error occurs during the bytecode modification of a class to patch.
 *
 * @author Gael Lazzari
 */
public class GwtTestPatchException extends GwtTestException {

    private static final long serialVersionUID = -2055077366596845492L;

    public GwtTestPatchException() {
    }

    public GwtTestPatchException(String message) {
        super(message);
    }

    public GwtTestPatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtTestPatchException(Throwable cause) {
        super(cause);
    }
}

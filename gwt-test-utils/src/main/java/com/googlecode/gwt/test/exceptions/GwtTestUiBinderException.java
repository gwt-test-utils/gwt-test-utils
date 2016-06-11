package com.googlecode.gwt.test.exceptions;

/**
 * Exception thrown when an error occurs during the UiBinder patching mechanism.
 *
 * @author Gael Lazzari
 */
public class GwtTestUiBinderException extends GwtTestPatchException {

    private static final long serialVersionUID = -5030059458747698751L;

    public GwtTestUiBinderException() {
    }

    public GwtTestUiBinderException(String message) {
        super(message);
    }

    public GwtTestUiBinderException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtTestUiBinderException(Throwable cause) {
        super(cause);
    }
}

package com.googlecode.gwt.test.exceptions;

/**
 * Exception thrown when an error occurs during a GWT i18n emulation.
 *
 * @author Gael Lazzari
 */
public class GwtTestI18NException extends GwtTestResourcesException {

    private static final long serialVersionUID = -7518261539641239910L;

    public GwtTestI18NException() {
    }

    public GwtTestI18NException(String message) {
        super(message);
    }

    public GwtTestI18NException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtTestI18NException(Throwable cause) {
        super(cause);
    }
}

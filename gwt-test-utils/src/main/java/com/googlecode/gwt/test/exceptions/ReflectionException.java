package com.googlecode.gwt.test.exceptions;

import com.googlecode.gwt.test.utils.GwtReflectionUtils;

/**
 * Exception thrown when an error occurs during the manipulation of gwt-test-utils reflection API.
 *
 * @author Gael Lazzari
 * @see GwtReflectionUtils
 */
public class ReflectionException extends GwtTestException {

    private static final long serialVersionUID = -7518261539641239910L;

    public ReflectionException() {
    }

    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionException(Throwable cause) {
        super(cause);
    }
}

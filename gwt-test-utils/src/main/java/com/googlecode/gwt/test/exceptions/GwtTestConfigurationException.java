package com.googlecode.gwt.test.exceptions;

/**
 * Exception thrown when gwt-test-utils detects an error in a test configuration.
 *
 * @author Gael Lazzari
 */
public class GwtTestConfigurationException extends GwtTestException {

    private static final long serialVersionUID = -6850907581408814501L;

    public GwtTestConfigurationException() {
    }

    public GwtTestConfigurationException(String message) {
        super(message);
    }

    public GwtTestConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtTestConfigurationException(Throwable cause) {
        super(cause);
    }

}

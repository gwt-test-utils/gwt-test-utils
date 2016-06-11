package com.googlecode.gwt.test.finder;

import com.googlecode.gwt.test.exceptions.GwtTestException;

public class GwtFinderException extends GwtTestException {

    private static final long serialVersionUID = 7293766392294842161L;

    public GwtFinderException() {
    }

    public GwtFinderException(String message) {
        super(message);
    }

    public GwtFinderException(String message, Throwable cause) {
        super(message, cause);
    }

    public GwtFinderException(Throwable cause) {
        super(cause);
    }

}

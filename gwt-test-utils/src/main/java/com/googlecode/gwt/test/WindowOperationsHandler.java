package com.googlecode.gwt.test;

import com.google.gwt.user.client.Window;

/**
 * Handler where are redirect {@link Window} operations. Its methods are expected to be overriden.
 *
 * @author Gael Lazzari
 */
public class WindowOperationsHandler {

    /**
     * Handler's method called during the invocation of {@link Window#alert(String)}. This default
     * implementation throws an UnsupportedOperationException.
     *
     * @param msg the message to be displayed.
     */
    public void alert(String msg) {
        throw new UnsupportedOperationException("Not handled call to Window.alert(msg)");
    }

    /**
     * Handler's method called during the invocation of {@link Window#confirm(String)}. This default
     * implementation throws an UnsupportedOperationException.
     *
     * @param msg the message to be displayed.
     * @return <code>true</code> if 'OK' is clicked, <code>false</code> if 'Cancel' is clicked.
     */
    public boolean confirm(String msg) {
        throw new UnsupportedOperationException("Not handled call to Window.confirm(msg)");
    }

    /**
     * Handler's method called during the invocation of {@link Window#open(String, String, String)}.
     * This implementation does nothing.
     *
     * @param url      the URL that the new window will display
     * @param name     the name of the window (e.g. "_blank")
     * @param features the features to be enabled/disabled on this window
     */
    public void open(String url, String name, String features) {
        // to be overriden
    }

    /**
     * Handler's method called during the invocation of {@link Window#print()}. This default
     * implementation does nothing.
     */
    public void print() {
        // to be overriden
    }

    /**
     * Handler's method called during the invocation of {@link Window#prompt(String, String)}. This
     * default implementation throws an UnsupportedOperationException.
     *
     * @param msg          the message to be displayed
     * @param initialValue the initial value in the dialog's text field
     * @return the value entered by the user if 'OK' was pressed, or <code>null</code> if 'Cancel'
     * was pressed
     */
    public String prompt(String msg, String initialValue) {
        throw new UnsupportedOperationException(
                "Not handled call to Window.prompt(msg, initialValue)");
    }

}

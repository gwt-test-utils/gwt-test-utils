package com.googlecode.gwt.test;

import com.google.gwt.core.client.GWT;

/**
 * Interface for an object capable of handling GWT logging, which is delegated by the patched
 * version of {@link GWT#log(String, Throwable)}.
 *
 * @author Bertrand Paquet
 */
public interface GwtLogHandler {

    /**
     * Logs a message (calls to {@link GWT#log(String, Throwable)} are delagated to this method).
     */
    void log(String message, Throwable t);

}

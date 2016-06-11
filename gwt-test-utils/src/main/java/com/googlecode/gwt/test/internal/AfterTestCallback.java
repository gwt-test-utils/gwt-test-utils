package com.googlecode.gwt.test.internal;

/**
 * Interface which provide a callback after a unit test execution. The callback will be called by a
 * {@link AfterTestCallbackManager}, which is responsible for holding callback reference and
 * handling exception which may occur during a callback execution. <strong>For internal use
 * only.</strong>
 *
 * @author Gael Lazzari
 */
public interface AfterTestCallback {

    /**
     * The callback method, executed by a {@link AfterTestCallbackManager}.
     *
     * @throws Throwable If any exception occurs during the callback. It will be handled by the
     *                   manager.
     */
    void afterTest() throws Throwable;

}

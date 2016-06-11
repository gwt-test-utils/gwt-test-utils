package com.googlecode.gwt.test.internal;

import com.google.gwt.junit.client.WithProperties;

/**
 * Testing framework independent error listener. <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
public class GwtTestDataHolder implements AfterTestCallback {

    private static final GwtTestDataHolder INSTANCE = new GwtTestDataHolder();

    public static GwtTestDataHolder get() {
        return INSTANCE;
    }

    private boolean currentTestFailed;

    private WithProperties currentWithProperties;

    private GwtTestDataHolder() {
        AfterTestCallbackManager.get().registerCallback(this);
    }

    public void afterTest() throws Throwable {
        this.currentTestFailed = false;
        this.currentWithProperties = null;
    }

    public WithProperties getCurrentWithProperties() {
        return currentWithProperties;
    }

    public boolean isCurrentTestFailed() {
        return currentTestFailed;
    }

    public void setCurrentTestFailed(boolean currentTestFailed) {
        this.currentTestFailed = currentTestFailed;
    }

    public void setCurrentWithProperties(WithProperties currentWithProperties) {
        this.currentWithProperties = currentWithProperties;
    }

}

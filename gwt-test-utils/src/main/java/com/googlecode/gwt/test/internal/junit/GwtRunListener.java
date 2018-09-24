package com.googlecode.gwt.test.internal.junit;

import com.google.gwt.junit.client.WithProperties;
import com.googlecode.gwt.test.internal.AfterTestCallbackManager;
import com.googlecode.gwt.test.internal.GwtTestDataHolder;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * gwt-test-utils custom {@link RunListener} to be used for every custom JUnit {@link Runner}. It
 * registers {@link WithProperties} for the running test, potential assertion errors and failures
 * not to throw {@link AfterTestCallbackManager#triggerCallbacks() errors in addition}. <strong>For
 * internal use only.</strong>
 *
 * @author Gael Lazzari
 */
public class GwtRunListener extends RunListener {

    @Override
    public void testAssumptionFailure(Failure failure) {
        GwtTestDataHolder.get().setCurrentTestFailed(true);
    }

    @Override
    public void testFailure(Failure failure) {
        GwtTestDataHolder.get().setCurrentTestFailed(true);
    }

    @Override
    public void testStarted(Description description) {
        WithProperties withProperties = description.getAnnotation(WithProperties.class);
        GwtTestDataHolder.get().setCurrentWithProperties(withProperties);
    }

}

package com.googlecode.gwt.test.internal.junit;

import com.googlecode.gwt.test.internal.GwtConfig;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

/**
 * gwt-test-utils {@link Runner}, which adds a {@link GwtRunListener} before running each test.
 * <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 * @deprecated Included for backwards compatibility with JUnit 4.4.
 */
@Deprecated
public class GwtJUnit4ClassRunner extends JUnit4ClassRunner {

    public GwtJUnit4ClassRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.addListener(new GwtRunListener());
        GwtConfig.get().setupGwtModule(getTestClass().getJavaClass());
        super.run(notifier);
    }

}

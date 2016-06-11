package com.googlecode.gwt.test.internal.junit;

import com.googlecode.gwt.test.internal.GwtConfig;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * gwt-test-utils {@link Runner}, which adds a {@link GwtRunListener} before running each test.
 * <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
public class GwtBlockJUnit4ClassRunner extends BlockJUnit4ClassRunner {

    public GwtBlockJUnit4ClassRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.addListener(new GwtRunListener());
        GwtConfig.get().setupGwtModule(getTestClass().getJavaClass());
        super.run(notifier);
    }

}

package com.googlecode.gwt.test.internal.junit;

import com.googlecode.gwt.test.internal.GwtClassLoader;
import com.googlecode.gwt.test.internal.GwtFactory;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;

/**
 * <p>
 * Base class for gwt-test-utils JUnit {@link Runner}, which aims to wrap another custom
 * {@link Runner} implementation loaded by {@link GwtClassLoader} . <strong>For internal use
 * only.</strong>
 * </p>
 *
 * @author Gael Lazzari
 * @see AbstractGwtRunnerFactory
 */
public abstract class AbstractGwtRunner extends Runner implements Filterable {

    private final Runner runner;

    public AbstractGwtRunner(Class<?> clazz) throws Throwable {
        GwtFactory.initializeIfNeeded();
        runner = getRunnerFactory().create(clazz);
    }

    public void filter(Filter filter) throws NoTestsRemainException {
        if (runner instanceof Filterable) {
            ((Filterable) runner).filter(filter);
        }
    }

    @Override
    public Description getDescription() {
        return runner.getDescription();
    }

    @Override
    public void run(RunNotifier notifier) {
        runner.run(notifier);
    }

    protected abstract AbstractGwtRunnerFactory getRunnerFactory();

}

package com.googlecode.gwt.test;

import com.googlecode.gwt.test.internal.junit.AbstractGwtRunner;
import com.googlecode.gwt.test.internal.junit.AbstractGwtRunnerFactory;

/**
 * <p>
 * The gwt-test-utils basic JUnit Runner allowing to run tests classes which reference, directly or
 * indirectly, GWT components. It works by annotating your test class with
 * <strong>&#064;RunWith(GwtRunner.class)</strong>.
 * </p>
 *
 * @author Gael Lazzari
 */
public class GwtRunner extends AbstractGwtRunner {

    public GwtRunner(Class<?> clazz) throws Throwable {
        super(clazz);
    }

    @Override
    protected AbstractGwtRunnerFactory getRunnerFactory() {
        return new GwtRunnerFactory();
    }

}

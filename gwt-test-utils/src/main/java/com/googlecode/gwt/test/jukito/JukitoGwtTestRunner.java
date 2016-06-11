package com.googlecode.gwt.test.jukito;

import com.googlecode.gwt.test.internal.junit.AbstractGwtRunner;
import com.googlecode.gwt.test.internal.junit.AbstractGwtRunnerFactory;

/**
 * @author Przemysław Gałązka
 * @since 04-03-2013
 */
public class JukitoGwtTestRunner extends AbstractGwtRunner {

    public JukitoGwtTestRunner(Class<?> clazz) throws Throwable {
        super(clazz);
    }

    @Override
    protected AbstractGwtRunnerFactory getRunnerFactory() {
        return new JukitoGwtTestRunnerFactory();
    }

}

package com.googlecode.gwt.test.jukito;

import com.googlecode.gwt.test.internal.junit.AbstractGwtRunnerFactory;

/**
 * @author Przemysław Gałązka
 * @since 04-03-2013
 */
public class JukitoGwtTestRunnerFactory extends AbstractGwtRunnerFactory {

    @Override
    protected String getRunnerClassName(boolean hasJUnit45OrHigher, boolean hasJUnitParams) {
        return "org.jukito.JukitoRunner";
    }
}

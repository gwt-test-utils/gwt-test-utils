package com.googlecode.gwt.test.spring;

import com.googlecode.gwt.test.internal.junit.AbstractGwtRunnerFactory;

class GwtSpringRunnerFactory extends AbstractGwtRunnerFactory {

    @Override
    protected String getRunnerClassName(boolean hasJUnit45OrHigher, boolean hasJUnitParams) {

        return hasJUnitParams
                ? "com.googlecode.gwt.test.internal.junit.GwtSpringJUnitParamsClassRunner"
                : "com.googlecode.gwt.test.internal.junit.GwtSpringJUnit4ClassRunner";
    }

}

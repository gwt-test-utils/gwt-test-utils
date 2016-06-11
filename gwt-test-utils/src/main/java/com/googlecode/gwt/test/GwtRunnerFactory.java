package com.googlecode.gwt.test;

import com.googlecode.gwt.test.internal.junit.AbstractGwtRunnerFactory;

class GwtRunnerFactory extends AbstractGwtRunnerFactory {

    @Override
    protected String getRunnerClassName(boolean hasJUnit45OrHigher, boolean hasJUnitParams) {
        if (hasJUnit45OrHigher) {
            if (hasJUnitParams) {
                return "com.googlecode.gwt.test.internal.junit.GwtBlockJUnitParamsClassRunner";
            } else {
                return "com.googlecode.gwt.test.internal.junit.GwtBlockJUnit4ClassRunner";
            }
        } else {
            return "com.googlecode.gwt.test.internal.junit.GwtJUnit4ClassRunner";
        }
    }

}

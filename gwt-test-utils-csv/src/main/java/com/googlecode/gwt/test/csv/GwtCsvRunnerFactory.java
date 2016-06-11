package com.googlecode.gwt.test.csv;

import com.googlecode.gwt.test.internal.junit.AbstractGwtRunnerFactory;

public class GwtCsvRunnerFactory extends AbstractGwtRunnerFactory {

    @Override
    protected String getRunnerClassName(boolean hasJUnit45OrHigher, boolean hasJUnitParams) {
        return hasJUnit45OrHigher ? "com.googlecode.gwt.test.csv.internal.GwtBlockJUnit4CsvRunner"
                : "com.googlecode.gwt.test.csv.internal.GwtJUnit4CsvClassRunner";
    }

}

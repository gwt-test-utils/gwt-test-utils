package com.googlecode.gwt.test.spring;

import com.googlecode.gwt.test.internal.junit.AbstractGwtRunnerFactory;

class GwtSpringCsvRunnerFactory extends AbstractGwtRunnerFactory {

   @Override
   protected String getRunnerClassName(boolean hasJUnit45OrHigher, boolean hasJUnitParams) {
      return hasJUnit45OrHigher
               ? "com.googlecode.gwt.test.csv.internal.GwtSpring3CsvJUnit4ClassRunner"
               : "com.googlecode.gwt.test.csv.internal.GwtSpring2CsvJUnit4ClassRunner";
   }

}

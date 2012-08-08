package com.googlecode.gwt.test.internal.patchers;

import java.math.BigDecimal;

import com.google.gwt.i18n.client.NumberFormat;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(NumberFormat.class)
class NumberFormatPatcher {

   @PatchMethod
   static String toPrecision(double d, int digits) {
      return new BigDecimal(d).setScale(digits, BigDecimal.ROUND_DOWN).toString();
   }

}

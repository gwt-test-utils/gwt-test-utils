package com.googlecode.gwt.test.gxt2.internal.patchers;

import com.extjs.gxt.ui.client.util.Util;
import com.googlecode.gwt.test.internal.utils.GwtStringUtils;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(Util.class)
class UtilPatcher {

   @PatchMethod
   static int parseInt(String value, int defaultValue) {
      return GwtStringUtils.parseInt(value, defaultValue);
   }

}

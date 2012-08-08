package com.googlecode.gwt.test.gxt2.internal.patchers;

import com.extjs.gxt.ui.client.GXT;
import com.google.gwt.user.client.Window.Navigator;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(GXT.class)
class GXTPatcher {

   @PatchMethod
   static String getUserAgent() {
      return Navigator.getUserAgent();
   }

}

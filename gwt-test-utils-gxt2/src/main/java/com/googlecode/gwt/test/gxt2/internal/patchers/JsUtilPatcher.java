package com.googlecode.gwt.test.gxt2.internal.patchers;

import com.extjs.gxt.ui.client.js.JsUtil;
import com.google.gwt.core.client.JavaScriptObject;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(JsUtil.class)
class JsUtilPatcher {

   @PatchMethod
   static JavaScriptObject eval(String code) {
      return JavaScriptObject.createObject();
   }

}

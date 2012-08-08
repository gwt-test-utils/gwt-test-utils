package com.googlecode.gwt.test.gxt2.internal.patchers;

import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

@PatchClass(ComponentHelper.class)
class ComponentHelperPatcher {

   @PatchMethod
   static void doAttachNative(Widget widget) {
      GwtReflectionUtils.callPrivateMethod(widget, "onAttach");
   }

   @PatchMethod
   static void doDetachNative(Widget widget) {
      GwtReflectionUtils.callPrivateMethod(widget, "onDetach");
   }

   @PatchMethod
   static void setParent(Widget parent, Widget child) {
      GwtReflectionUtils.callPrivateMethod(child, "setParent", parent);
   }

}

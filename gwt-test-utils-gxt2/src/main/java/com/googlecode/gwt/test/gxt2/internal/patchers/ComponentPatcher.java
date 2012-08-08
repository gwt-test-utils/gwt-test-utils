package com.googlecode.gwt.test.gxt2.internal.patchers;

import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

@PatchClass(Component.class)
class ComponentPatcher {

   @PatchMethod
   static void setParent(Component component, Widget parent) {
      GwtReflectionUtils.setPrivateFieldValue(component, "parent", parent);
   }

}

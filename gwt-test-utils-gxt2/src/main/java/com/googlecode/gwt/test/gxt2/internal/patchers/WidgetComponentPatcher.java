package com.googlecode.gwt.test.gxt2.internal.patchers;

import com.extjs.gxt.ui.client.widget.WidgetComponent;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

@PatchClass(WidgetComponent.class)
class WidgetComponentPatcher {

   @PatchMethod
   static void setParent(final WidgetComponent component, final Widget parent, final Widget child) {
      GwtReflectionUtils.setPrivateFieldValue(child, "parent", parent);
   }

}

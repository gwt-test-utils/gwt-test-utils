package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.layout.client.Layout.AnimationCallback;
import com.google.gwt.user.client.ui.LayoutCommand;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

@PatchClass(LayoutPanel.class)
class LayoutPanelPatcher {

   @PatchMethod
   static void animate(LayoutPanel panel, final int duration, final AnimationCallback callback) {
      LayoutCommand layoutCmd = GwtReflectionUtils.getPrivateFieldValue(panel, "layoutCmd");
      layoutCmd.schedule(0, callback);
   }

}

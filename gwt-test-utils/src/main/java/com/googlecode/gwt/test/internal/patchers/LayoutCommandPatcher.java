package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.layout.client.Layout.AnimationCallback;
import com.google.gwt.user.client.ui.LayoutCommand;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

@PatchClass(LayoutCommand.class)
class LayoutCommandPatcher {

    @PatchMethod
    static void schedule(LayoutCommand cmd, int duration, AnimationCallback callback) {
        GwtReflectionUtils.setPrivateFieldValue(cmd, "callback", callback);
        GwtReflectionUtils.setPrivateFieldValue(cmd, "duration", 0);
        cmd.execute();
    }

}

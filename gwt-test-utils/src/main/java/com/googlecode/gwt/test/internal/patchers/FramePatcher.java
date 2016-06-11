package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.user.client.ui.Frame;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(Frame.class)
class FramePatcher {

    @PatchMethod
    static String getUrl(Frame frame) {
        IFrameElement e = frame.getElement().cast();
        return e.getSrc();
    }

    @PatchMethod
    static void setUrl(Frame frame, String url) {
        IFrameElement e = frame.getElement().cast();
        e.setSrc(url);
    }

}

package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(ScrollPanel.class)
class ScrollPanelPatcher {

    @PatchMethod
    static void ensureVisibleImpl(ScrollPanel scrollPanel, Element scroll, Element e) {

    }
}

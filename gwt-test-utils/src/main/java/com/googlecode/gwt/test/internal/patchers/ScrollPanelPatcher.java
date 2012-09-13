package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(ScrollPanel.class)
public class ScrollPanelPatcher {
    
    /**
     * Simple patch to get the all test working that use ensureVisible
     * @param scrollPanel
     * @param item 
     */
    @PatchMethod
    public static void ensureVisible(ScrollPanel scrollPanel, UIObject item) {

    }
}

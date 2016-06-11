package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.WidgetCollection;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

@PatchClass(StackPanel.class)
class StackPanelPatcher {

    @PatchMethod
    static int findDividerIndex(StackPanel panel, Element child) {
        WidgetCollection children = GwtReflectionUtils.getPrivateFieldValue(panel, "children");

        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getElement().equals(child)) {
                return i;
            }
        }

        return -1;
    }

}

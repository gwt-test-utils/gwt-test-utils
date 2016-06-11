package com.googlecode.gwt.test.internal.patchers.dom;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SelectElement;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.JavaScriptObjects;

@PatchClass(SelectElement.class)
class SelectElementPatcher {

    public static final String SELECTED_SIZE = "SELECTED_SIZE";

    private static final String SELECTED_INDEX = "selectedIndex";

    @PatchMethod
    static int getSelectedIndex(SelectElement select) {
        return JavaScriptObjects.hasProperty(select, SELECTED_INDEX) ? JavaScriptObjects.getInteger(
                select, SELECTED_INDEX) : -1;
    }

    @PatchMethod
    static int getSize(SelectElement select) {
        int visibleSize = JavaScriptObjects.hasProperty(select, SELECTED_SIZE)
                ? JavaScriptObjects.getInteger(select, SELECTED_SIZE) : -1;
        int actualSize = select.getChildNodes().getLength();

        if (visibleSize == -1 || visibleSize > actualSize) {
            visibleSize = actualSize;
        }

        return visibleSize;
    }

    static void refreshSelect(SelectElement select) {
        int visibleSize = select.getSize();

        for (int i = 0; i < select.getChildNodes().getLength(); i++) {
            Element e = select.getChildNodes().getItem(i).cast();

            if (i < visibleSize) {
                // this element is visible
                e.getStyle().clearProperty("display");
            } else {
                // it's not visible
                e.getStyle().setProperty("display", "none");
            }
        }

    }

    @PatchMethod
    static void setSize(SelectElement select, int size) {
        JavaScriptObjects.setProperty(select, SELECTED_SIZE, size);
        refreshSelect(select);
    }

}

package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.PotentialElement;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

@PatchClass(UIObject.class)
class UIObjectPatcher {

    @PatchMethod
    static double extractLengthValue(UIObject uiObject, String s) {
        if ("auto".equals(s) || "inherit".equals(s) || "".equals(s)) {
            return 0;
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                buffer.append(c);
            }
        }
        return Double.parseDouble(buffer.toString());
    }

    @PatchMethod
    static boolean isVisible(Element elem) {
        String display = elem.getStyle().getProperty("display");

        return !display.equals("none");
    }

    @PatchMethod
    static void replaceElement(UIObject uiObject, Element elem) {
        Element element = GwtReflectionUtils.getPrivateFieldValue(uiObject, "element");
        if (element != null) {
            // replace this.element in its parent with elem.
            replaceNode(uiObject, element, elem);
        }

        GwtReflectionUtils.setPrivateFieldValue(uiObject, "element", elem);
    }

    @PatchMethod
    static void replaceNode(UIObject uiObject, Element node, Element newNode) {
        Node parent = node.getParentNode();

        if (parent != null) {
            parent.insertBefore(newNode, node);
            parent.removeChild(node);
        }
    }

    @PatchMethod
    static void setElement(UIObject uiObject, com.google.gwt.user.client.Element elem) {
        Element element = GwtReflectionUtils.getPrivateFieldValue(uiObject, "element");
        assert element == null || PotentialElement.isPotential(element) : "Element may only be set once";

        GwtReflectionUtils.setPrivateFieldValue(uiObject, "element", elem);

        // Bind the widget to listen to element so we can trigger event on it even
        // if the widget has not been attached yet
        if (Widget.class.isInstance(uiObject)) {
            DOM.setEventListener(elem, (Widget) uiObject);
        }

    }

    @PatchMethod
    static void setVisible(Element elem, boolean visible) {
        String display = visible ? "" : "none";
        elem.getStyle().setProperty("display", display);
    }

    @PatchMethod
    static void updatePrimaryAndDependentStyleNames(Element elem, String newPrimaryStyle) {

        String[] classes = elem.getPropertyString("className").split(" ");

        if (classes.length < 1) {
            elem.setPropertyString("className", newPrimaryStyle);
        } else {
            String oldPrimaryStyle = classes[0];
            int oldPrimaryStyleLen = oldPrimaryStyle.length();

            classes[0] = newPrimaryStyle;
            for (int i = 1; i < classes.length; i++) {
                String name = classes[i];
                if (name.length() > oldPrimaryStyleLen && name.charAt(oldPrimaryStyleLen) == '-'
                        && name.indexOf(oldPrimaryStyle) == 0) {
                    classes[i] = newPrimaryStyle + name.substring(oldPrimaryStyleLen);
                }
            }

            StringBuilder sb = new StringBuilder();
            for (String name : classes) {
                sb.append(name + " ");
            }

            elem.setPropertyString("className", sb.toString().trim());
        }
    }

}

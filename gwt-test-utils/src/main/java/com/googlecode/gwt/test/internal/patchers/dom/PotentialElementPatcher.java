package com.googlecode.gwt.test.internal.patchers.dom;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.ui.PotentialElement;
import com.google.gwt.user.client.ui.UIObject;
import com.googlecode.gwt.test.internal.utils.JsoUtils;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import com.googlecode.gwt.test.utils.JavaScriptObjects;

@PatchClass(PotentialElement.class)
class PotentialElementPatcher {

    private static final String POTENTIALELEMENT_TAG = "POTENTIALELEMENT_TAG";
    private static final String POTENTIALELEMENT_UIOBJECT = "POTENTIALELEMENT_UIOBJECT";
    private static final String POTENTIALELEMENT_WRAPPED_ELEMENT = "POTENTIALELEMENT_WRAPPED_ELEMENT";

    @PatchMethod
    static PotentialElement build(UIObject o, String tagName) {
        PotentialElement e = JsoUtils.newNode(Node.ELEMENT_NODE).cast();
        Element wrappedElement = JsoUtils.newElement(tagName, o.getElement().getOwnerDocument());
        JavaScriptObjects.setProperty(e, POTENTIALELEMENT_TAG, true);
        JavaScriptObjects.setProperty(e, POTENTIALELEMENT_WRAPPED_ELEMENT, wrappedElement);
        JavaScriptObjects.setProperty(e, POTENTIALELEMENT_UIOBJECT, o);

        return e;
    }

    @PatchMethod
    static void declareShim() {

    }

    @PatchMethod
    static boolean isPotential(JavaScriptObject o) {
        return JavaScriptObjects.getBoolean(o, POTENTIALELEMENT_TAG);
    }

    @PatchMethod
    static Element resolve(Element maybePotential) {
        if (isPotential(maybePotential)) {
            UIObject o = JavaScriptObjects.getObject(maybePotential, POTENTIALELEMENT_UIOBJECT);
            GwtReflectionUtils.callPrivateMethod(o, "resolvePotentialElement");

            return JavaScriptObjects.getObject(maybePotential, POTENTIALELEMENT_WRAPPED_ELEMENT);

        } else {
            return maybePotential;
        }
    }

    @PatchMethod
    static Element setResolver(PotentialElement pe, UIObject resolver) {
        JavaScriptObjects.setProperty(pe, POTENTIALELEMENT_UIOBJECT, resolver);

        return pe;

    }

}

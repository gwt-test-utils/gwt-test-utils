package com.googlecode.gwt.test.internal.patchers.dom;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.impl.DOMImpl;
import com.googlecode.gwt.test.internal.utils.EventUtils;
import com.googlecode.gwt.test.internal.utils.JsoProperties;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.JavaScriptObjects;

@PatchClass(DOMImpl.class)
class DOMImplUserPatcher {

    private static final String ELEM_EVENTLISTENER = "ELEM_EVENTLISTENER";

    @PatchMethod
    static void eventCancelBubble(DOMImpl domImpl, Event evt, boolean cancel) {
        JavaScriptObjects.setProperty(evt, JsoProperties.EVENT_IS_STOPPED, cancel);
    }

    @PatchMethod
    static int eventGetTypeInt(DOMImpl domImpl, String type) {
        return EventUtils.getEventTypeInt(type);
    }

    @PatchMethod
    static Element getChild(DOMImpl domImpl, Element userElem, int index) {
        if (index >= userElem.getChildNodes().getLength()) {
            return null;
        }

        return userElem.getChildNodes().getItem(index).cast();
    }

    @PatchMethod
    static int getChildCount(Object domImpl, Element elem) {
        return elem.getChildCount();
    }

    @PatchMethod
    static int getChildIndex(DOMImpl domImpl, Element parent, Element child) {
        if (parent == null || child == null) {
            return -1;
        }

        for (int i = 0; i < parent.getChildNodes().getLength(); i++) {
            if (child.equals(parent.getChildNodes().getItem(i))) {
                return i;
            }
        }

        return -1;
    }

    @PatchMethod
    static EventListener getEventListener(Element elem) {
        return JavaScriptObjects.getObject(elem, ELEM_EVENTLISTENER);
    }

    @PatchMethod
    static int getEventsSunk(DOMImpl domImpl, Element elem) {
        return 1;
    }

    @PatchMethod
    static void initEventSystem(DOMImpl domImpl) {
    }

    @PatchMethod
    static void insertChild(DOMImpl domImpl, Element userParent, Element userChild, int index) {
        NodePatcher.insertAtIndex(userParent, userChild, index);
    }

    @PatchMethod
    static void releaseCapture(Object domImpl, Element elem) {

    }

    @PatchMethod
    static void setCapture(Object domImpl, Element elem) {

    }

    @PatchMethod
    static void setEventListener(Element elem, EventListener listener) {
        JavaScriptObjects.setProperty(elem, ELEM_EVENTLISTENER, listener);
    }

    @PatchMethod
    static void sinkEvents(DOMImpl domImpl, Element elem, int eventBits) {

    }

}

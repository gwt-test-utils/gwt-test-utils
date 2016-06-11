package com.googlecode.gwt.test.internal.patchers.dom;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.Event;
import com.googlecode.gwt.test.internal.BrowserSimulatorImpl;
import com.googlecode.gwt.test.internal.utils.JsoProperties;
import com.googlecode.gwt.test.patchers.InitMethod;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.utils.JavaScriptObjects;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

@PatchClass(DomEvent.class)
class DomEventPatcher {

    public static void triggerNativeEvent(NativeEvent event, Element target) {
        // only trigger native event if the effective event target (setup in Browser) is the same as
        // the target, in order to avoid triggering nativePreviewHandler while bubbling
        Element effectiveTarget = JavaScriptObjects.getObject(event, JsoProperties.EVENT_TARGET);

        if (effectiveTarget == target) {
            Event.fireNativePreviewEvent(event);
        }
    }

    @InitMethod
    static void initClass(CtClass c) throws CannotCompileException, NotFoundException {

        CtMethod fireNativeEvent = c.getMethod(
                "fireNativeEvent",
                "(Lcom/google/gwt/dom/client/NativeEvent;Lcom/google/gwt/event/shared/HasHandlers;Lcom/google/gwt/dom/client/Element;)V");

        // fire browser event loop first because some command or async callback may modify the DOM
        // structure + fire NativePreviewHandler
        fireNativeEvent.insertBefore(BrowserSimulatorImpl.class.getName() + ".get().fireLoopEnd(); "
                + DomEventPatcher.class.getName() + ".triggerNativeEvent($1, $3);");

        // fire browser event loop at the end because some command may have been scheduled or RPC call
        // made when the event was dispatched.
        fireNativeEvent.insertAfter(BrowserSimulatorImpl.class.getName() + ".get().fireLoopEnd();");
    }

}

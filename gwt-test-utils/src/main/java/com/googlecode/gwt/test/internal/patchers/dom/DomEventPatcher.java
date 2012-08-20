package com.googlecode.gwt.test.internal.patchers.dom;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.Event;
import com.googlecode.gwt.test.FinallyCommandTrigger;
import com.googlecode.gwt.test.internal.utils.JsoProperties;
import com.googlecode.gwt.test.patchers.InitMethod;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.utils.JavaScriptObjects;

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

      CtMethod onBrowserEvent = c.getMethod(
               "fireNativeEvent",
               "(Lcom/google/gwt/dom/client/NativeEvent;Lcom/google/gwt/event/shared/HasHandlers;Lcom/google/gwt/dom/client/Element;)V");

      // run finally scheduled commands first because they may modify the DOM
      // structure + fire NativePreviewHandler
      onBrowserEvent.insertBefore(FinallyCommandTrigger.class.getName() + ".triggerCommands(); "
               + DomEventPatcher.class.getName() + ".triggerNativeEvent($1, $3);");

      // run finally scheduled commands because some could have been scheduled
      // when the event was dispatched.
      onBrowserEvent.insertAfter(FinallyCommandTrigger.class.getName() + ".triggerCommands();");
   }

}

package com.googlecode.gwt.test.internal.patchers.dom;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import com.google.gwt.event.dom.client.DomEvent;
import com.googlecode.gwt.test.FinallyCommandTrigger;
import com.googlecode.gwt.test.patchers.InitMethod;
import com.googlecode.gwt.test.patchers.PatchClass;

@PatchClass(DomEvent.class)
class DomEventPatcher {

   @InitMethod
   static void initClass(CtClass c) throws CannotCompileException, NotFoundException {

      CtMethod onBrowserEvent = c.getMethod(
               "fireNativeEvent",
               "(Lcom/google/gwt/dom/client/NativeEvent;Lcom/google/gwt/event/shared/HasHandlers;Lcom/google/gwt/dom/client/Element;)V");

      // run finally scheduled commands first because they may modify the DOM
      // structure
      onBrowserEvent.insertBefore(FinallyCommandTrigger.class.getName() + ".triggerCommands();");

      // run finally scheduled commands because some could have been scheduled
      // when the event was dispatched.
      onBrowserEvent.insertAfter(FinallyCommandTrigger.class.getName() + ".triggerCommands();");

   }

}

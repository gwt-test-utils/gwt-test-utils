package com.googlecode.gwt.test.gwtbootstrap.patcher;

import com.github.gwtbootstrap.client.ui.base.AlertBase;
import com.github.gwtbootstrap.client.ui.event.CloseEvent;
import com.github.gwtbootstrap.client.ui.event.ClosedEvent;
import com.google.gwt.dom.client.Element;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

/**
 * Patcher for {@link AlertBase}
 * 
 * @author Kenichiro Tanaka
 * 
 */
@PatchClass(AlertBase.class)
class AlertBasePatcher {

   @PatchMethod
   static void close(AlertBase alertBase, Element e) {
      alertBase.fireEvent(new CloseEvent());
      alertBase.fireEvent(new ClosedEvent());
   }

   @PatchMethod
   static void configure(AlertBase alertBase, Element e) {
   }

   @PatchMethod
   static void setHandlerFunctions(AlertBase alertBase, Element e) {
   }
}

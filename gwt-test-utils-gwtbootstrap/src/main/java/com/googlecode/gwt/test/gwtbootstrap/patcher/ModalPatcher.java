package com.googlecode.gwt.test.gwtbootstrap.patcher;

import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.constants.VisibilityChange;
import com.google.gwt.dom.client.Element;
import com.googlecode.gwt.test.exceptions.ReflectionException;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

/**
 * Patcher for {@link Modal}.
 * 
 * @author Kenichiro Tanaka
 * 
 */
@PatchClass(Modal.class)
class ModalPatcher {
   @PatchMethod
   static void changeVisibility(Modal modal, Element e, String visibility) {
      if (VisibilityChange.SHOW.get().equals(visibility) && !modal.isVisible()) {
         show(modal);
      } else if (VisibilityChange.HIDE.get().equals(visibility) && modal.isVisible()) {
         hide(modal);
      } else if (VisibilityChange.TOGGLE.get().equals(visibility)) {
         if (modal.isVisible()) {
            hide(modal);
         } else {
            show(modal);
         }
      }
   }

   @PatchMethod
   static void configure(Modal modal, Element e, boolean k, boolean b, boolean s) {
   }

   @PatchMethod
   static void configure(Modal modal, Element e, boolean k, String b, boolean s) {
   }

   @PatchMethod
   static void reconfigure(Modal modal, Element e, boolean k, boolean b, boolean s) {
   }

   @PatchMethod
   static void reconfigure(Modal modal, Element e, boolean k, String b, boolean s) {
   }

   @PatchMethod
   static void setHandlerFunctions(Modal modal, Element e) {
   }

   @PatchMethod
   static void unsetHandlerFunctions(Modal modal, Element e) {
   }

   private static void callOn(Modal modal, String method) {
      try {
         // try with bootstrap 2.0.4.0 signature
         GwtReflectionUtils.callPrivateMethod(modal, method);
      } catch (ReflectionException e) {
         // try with bootstrap 2.1.1.0 signature
         GwtReflectionUtils.callPrivateMethod(modal, method, new Object[]{null});
      }
   }

   private static void hide(Modal modal) {
      callOn(modal, "onHide");
      modal.setVisible(false);
      callOn(modal, "onHidden");
   }

   private static void show(Modal modal) {
      callOn(modal, "onShow");
      modal.setVisible(true);
      callOn(modal, "onShown");
   }
}

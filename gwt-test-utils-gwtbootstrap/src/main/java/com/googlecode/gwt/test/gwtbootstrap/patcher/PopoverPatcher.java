package com.googlecode.gwt.test.gwtbootstrap.patcher;

import com.github.gwtbootstrap.client.ui.Popover;
import com.google.gwt.dom.client.Element;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

/**
 * Patcher for {@link Popover}.
 * <p>
 * This patcher do nothing only for avoiding {@link UnsatisfiedLinkError}.
 * 
 * @author Kenichiro Tanaka
 * 
 */
@PatchClass(Popover.class)
class PopoverPatcher {

   @PatchMethod
   static void changeVisibility(Element e, String visibility) {

   }

   @PatchMethod
   static void changeVisibility(Popover popover, Element e, String visibility) {
   }

   @PatchMethod
   static void configure(Popover popover, Element element, boolean animated, String placement,
            String trigger, int showDelay, int hideDelay) {
   }
}

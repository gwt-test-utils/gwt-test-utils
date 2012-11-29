package com.googlecode.gwt.test.gwtbootstrap.patcher;

import com.github.gwtbootstrap.client.ui.Tooltip;
import com.google.gwt.dom.client.Element;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

/**
 * Patcher for {@link Tooltip}.
 * <p>
 * This patcher do nothing only for avoiding {@link UnsatisfiedLinkError}.
 * 
 * @author Kenichiro Tanaka
 * 
 */
@PatchClass(Tooltip.class)
class TooltipPatcher {

   @PatchMethod
   static void changeVisibility(Element e, String visibility) {
   }

   @PatchMethod
   static void configure(Element element, boolean animated, String placement, String trigger,
            int showDelay, int hideDelay) {

   }

   @PatchMethod
   static void configure(String selector, String text, boolean animated, String placement,
            String trigger, int showDelay, int hideDelay) {
   }
}

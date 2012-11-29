package com.googlecode.gwt.test.gwtbootstrap.patcher;

import com.github.gwtbootstrap.client.ui.base.DropdownBase;
import com.google.gwt.dom.client.Element;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

/**
 * Patcher for {@link DropdownBase}.
 * <p>
 * This patcher do nothing only for avoiding {@link UnsatisfiedLinkError}.
 * 
 * @author Kenichiro Tanaka
 * 
 */
@PatchClass(DropdownBase.class)
class DropDownBaseTest {

   @PatchMethod
   static void configure(DropdownBase dropdownBase, Element e) {
   }
}

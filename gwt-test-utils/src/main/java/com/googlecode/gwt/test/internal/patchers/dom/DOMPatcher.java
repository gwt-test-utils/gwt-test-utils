package com.googlecode.gwt.test.internal.patchers.dom;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.DOM;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(DOM.class)
class DOMPatcher {

   @PatchMethod
   static Element getFirstChild(Element elem) {
      Node firstChild = elem.getFirstChildElement();
      if (firstChild != null) {
         return firstChild.cast();
      }
      return null;

   }

   @PatchMethod
   static Element getParent(Element elem) {
      com.google.gwt.dom.client.Element parentElem = elem.getParentElement();

      if (parentElem == null) {
         return null;
      }

      Element parent = parentElem.cast();
      return parent;

   }

}

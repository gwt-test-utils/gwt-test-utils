package com.googlecode.gwt.test.gxt2.internal.patchers;

import com.extjs.gxt.ui.client.core.DomHelper;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.Element;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import com.googlecode.gwt.test.internal.utils.GwtHtmlParser;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(DomHelper.class)
class DomHelperPatcher {

   @PatchMethod
   static Element append(Element elem, String html) {
      NodeList<Node> parsedNodes = GwtHtmlParser.parse(html);

      for (int i = 0; i < parsedNodes.getLength(); i++) {
         Node current = parsedNodes.getItem(i);
         elem.appendChild(current);
      }

      return elem;
   }

   @PatchMethod
   static Element insertAfter(Element elem, String html) {
      Element parent = elem.getParentElement().cast();
      NodeList<Node> parsedNodes = GwtHtmlParser.parse(html);

      for (int i = 0; i < parsedNodes.getLength(); i++) {
         Node previous = i > 0 ? parsedNodes.getItem(i - 1) : elem;

         Node next = parsedNodes.getItem(i);
         parent.insertAfter(next, previous);
      }

      // not well documented : should it really return the parent element ?
      return parent;
   }

   @PatchMethod
   static Element insertBefore(Element elem, String html) {
      Element parent = elem.getParentElement().cast();
      NodeList<Node> parsedNodes = GwtHtmlParser.parse(html);

      parent.insertBefore(parsedNodes.getItem(0), elem);

      for (int i = 1; i < parsedNodes.getLength(); i++) {

         Node next = parsedNodes.getItem(i);
         parent.insertAfter(next, parsedNodes.getItem(i - 1));
      }

      // not well documented : should it really return the parent element ?
      return parent;
   }

   @PatchMethod
   static Element insertFirst(Element elem, String html) {
      NodeList<Node> parsedNodes = GwtHtmlParser.parse(html);

      elem.insertFirst(parsedNodes.getItem(0));

      for (int i = 1; i < parsedNodes.getLength(); i++) {
         Node current = parsedNodes.getItem(i);
         elem.insertAfter(current, parsedNodes.getItem(i - 1));
      }

      return elem;
   }

   @PatchMethod
   static Element insertHtml(String where, Element el, String html) {
      // not sure about what I'm doing here..
      if ("beforeBegin".equals(where)) {
         return insertBefore(el, html);
      } else if ("afterBegin".equals(where)) {
         return insertFirst(el, html);
      } else if ("beforeEnd".equals(where)) {
         return append(el, html);
      } else if ("afterEnd".equals(where)) {
         return insertAfter(el, html);
      } else {
         throw new GwtTestPatchException(
                  "Cannot insert html at position '"
                           + where
                           + "', the only allowed values are ' beforeBegin / afterBegin / beforeEnd / afterEnd '");
      }
   }

}

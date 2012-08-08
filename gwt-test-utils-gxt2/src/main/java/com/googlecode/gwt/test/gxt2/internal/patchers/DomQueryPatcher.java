package com.googlecode.gwt.test.gxt2.internal.patchers;

import java.util.ArrayList;
import java.util.Set;

import se.fishtank.css.selectors.GwtNodeSelector;
import se.fishtank.css.selectors.NodeSelectorException;

import com.extjs.gxt.ui.client.core.DomQuery;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.Element;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import com.googlecode.gwt.test.internal.utils.JsoUtils;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(DomQuery.class)
class DomQueryPatcher {

   @PatchMethod
   static JavaScriptObject internalSelect(String selector) {
      Element body = Document.get().getBody().cast();
      return internalSelect(selector, body);
   }

   @PatchMethod
   static JavaScriptObject internalSelect(String selector, Element root) {
      try {
         Set<Node> nodeSet = new GwtNodeSelector(root).querySelectorAll(selector);
         return JsoUtils.newNodeList(new ArrayList<Node>(nodeSet));
      } catch (NodeSelectorException e) {
         throw new GwtTestPatchException("Error while trying to find GWT nodes matching '"
                  + selector + "'", e);
      }
   }

}

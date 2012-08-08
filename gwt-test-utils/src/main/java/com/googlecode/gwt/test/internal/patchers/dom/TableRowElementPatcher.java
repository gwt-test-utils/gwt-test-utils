package com.googlecode.gwt.test.internal.patchers.dom;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.googlecode.gwt.test.internal.utils.JsoUtils;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(TableRowElement.class)
class TableRowElementPatcher {

   @PatchMethod
   static NodeList<TableCellElement> getCells(TableRowElement element) {
      List<TableCellElement> cells = new ArrayList<TableCellElement>();

      for (int i = 0; i < element.getChildCount(); i++) {
         Node child = element.getChild(i);
         if (TableCellElement.class.isInstance(child)) {
            cells.add((TableCellElement) child);
         }
      }

      return JsoUtils.newNodeList(cells);
   }

   @PatchMethod
   static int getSectionRowIndex(TableRowElement element) {
      if (element == null) {
         return -1;
      }
      Element parent = element.getParentElement();
      if (parent == null) {
         return -1;
      }

      for (int i = 0; i < parent.getChildNodes().getLength(); i++) {
         if (element.equals(parent.getChildNodes().getItem(i))) {
            return i;
         }
      }

      return -1;

   }

}

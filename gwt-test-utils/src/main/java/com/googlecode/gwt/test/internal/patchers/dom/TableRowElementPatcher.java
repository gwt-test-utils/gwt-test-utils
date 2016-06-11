package com.googlecode.gwt.test.internal.patchers.dom;

import com.google.gwt.dom.client.*;
import com.googlecode.gwt.test.internal.utils.JsoUtils;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

import java.util.List;

@PatchClass(TableRowElement.class)
class TableRowElementPatcher {

    @PatchMethod
    static void deleteCell(TableRowElement element, int index) {

        List<Node> innerList = JsoUtils.getChildNodeInnerList(element);

        if (index == -1 || index >= innerList.size()) {
            index = innerList.size() - 1;

        }

        innerList.remove(index);
    }

    @PatchMethod
    static NodeList<TableCellElement> getCells(TableRowElement element) {
        return element.getChildNodes().<NodeList<TableCellElement>>cast();
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

    @PatchMethod
    static TableCellElement insertCell(TableRowElement element, int index) {

        List<Node> innerList = JsoUtils.getChildNodeInnerList(element);
        TableCellElement newCell = Document.get().createTDElement();

        if (index == -1 || index >= element.getCells().getLength()) {
            innerList.add(newCell);
        } else {
            innerList.add(index, newCell);
        }

        return newCell;
    }

}

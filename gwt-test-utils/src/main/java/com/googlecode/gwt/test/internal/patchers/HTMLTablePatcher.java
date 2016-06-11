package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTMLTable;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

@PatchClass(HTMLTable.class)
class HTMLTablePatcher {
    @PatchMethod
    static int getDOMCellCount(HTMLTable table, com.google.gwt.user.client.Element element, int row) {
        return element.getChildNodes().getItem(row).getChildNodes().getLength();
    }

    @PatchMethod
    static int getDOMCellCount(HTMLTable table, Element element, int row) {
        return element.getChildNodes().getItem(row).getChildNodes().getLength();
    }

    @PatchMethod
    static int getDOMRowCount(HTMLTable table, com.google.gwt.user.client.Element element) {
        return element.getChildNodes().getLength();
    }

    @PatchMethod
    static int getDOMRowCount(HTMLTable table, Element element) {
        return element.getChildNodes().getLength();
    }

    @PatchMethod
    static void addCells(HTMLTable table, Element element, int row, int num) {
        TableRowElement trCell = getCellElement(element, row);
        for (int i = 0; i < num; i++) {
            trCell.appendChild(Document.get().createTDElement());
        }
    }

    private static TableRowElement getCellElement(Element tableElement, int row) {
        return (TableRowElement) tableElement.getChildNodes().getItem(row);
    }

    @PatchMethod
    static Element getEventTargetCell(HTMLTable table, Event event) {
        Object bodyElem = GwtReflectionUtils.getPrivateFieldValue(table, "bodyElem");

        Element td = DOM.eventGetTarget(event);
        for (; td != null; td = DOM.getParent(td)) {
            // If it's a TD, it might be the one we're looking for.
            if (DOM.getElementProperty(td, "tagName").equalsIgnoreCase("td")) {
                // Make sure it's directly a part of this table before returning
                // it.
                Element tr = DOM.getParent(td);
                Object body = DOM.getParent(tr);

                if (body == bodyElem) {
                    return td;
                }
            }
            // If we run into this table's body, we're out of options.
            if (td == bodyElem) {
                return null;
            }
        }
        return null;
    }

}

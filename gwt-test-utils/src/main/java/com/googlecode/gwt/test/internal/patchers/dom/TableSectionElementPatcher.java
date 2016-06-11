package com.googlecode.gwt.test.internal.patchers.dom;

import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(TableSectionElement.class)
class TableSectionElementPatcher {

    @PatchMethod
    static void deleteRow(TableSectionElement e, int index) {
        NodeList<TableRowElement> rows = e.getRows();

        if (rows.getLength() < 1) {
            return;
        }

        if (index == -1) {
            index = rows.getLength() - 1;
        }

        TableRowElement rowToDelete = rows.getItem(index);
        e.removeChild(rowToDelete);
    }

    @PatchMethod
    static NodeList<TableRowElement> getRows(TableSectionElement e) {
        // deep search
        return e.getElementsByTagName("tr").cast();
    }
}

package com.googlecode.gwt.test.internal.patchers.dom;

import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(TableCellElement.class)
class TableCellElementPatcher {

    @PatchMethod
    static int getCellIndex(TableCellElement element) {
        TableRowElement row = TableRowElement.as(element.getParentElement());

        for (int i = 0; i < row.getCells().getLength(); i++) {
            if (element.equals(row.getChild(i))) {
                return i;
            }
        }
        return -1;
    }

}

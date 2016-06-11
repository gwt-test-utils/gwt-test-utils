package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(CellFormatter.class)
class HTMLTableCellFormatterPatcher {

    @PatchMethod
    static Element getCellElement(CellFormatter cellFormatter, Element table, int row, int col) {
        TableRowElement rowElement = (TableRowElement) table.getChildNodes().getItem(row);
        return rowElement.getChildNodes().getItem(col).cast();
    }

}

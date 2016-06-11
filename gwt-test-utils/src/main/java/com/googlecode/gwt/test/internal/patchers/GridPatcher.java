package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.user.client.ui.Grid;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(Grid.class)
class GridPatcher {

    @PatchMethod
    static void addRows(Element table, int rows, int columns) {
        String nbsp = "&nbsp;";
        for (int i = 0; i < rows; i++) {
            table.appendChild(createRow(columns, nbsp));
        }
    }

    private static TableRowElement createRow(int columns, String cellContent) {
        TableRowElement tr = Document.get().createTRElement();
        for (int i = 0; i < columns; i++) {
            TableCellElement cell = Document.get().createTDElement();
            cell.setInnerHTML(cellContent);
            tr.appendChild(cell);
        }

        return tr;
    }
}

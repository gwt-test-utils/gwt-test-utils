package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class UiBinderWithCellWidgetTest extends GwtTestTest {

    @Test
    public void uiBinderWithCellWidget() {
        // Act

        // never happen GwtTestUiBinderException
        UiBinderWithCellWidget uiPanel = GWT.create(UiBinderWithCellWidget.class);

        // RowCountChangeEvent will fire
        uiPanel.table.setRowData(Arrays.asList("1", "2"));

        // Assert
        assertEquals(2, uiPanel.rowCount);

    }

}

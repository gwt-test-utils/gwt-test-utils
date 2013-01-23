package com.googlecode.gwt.test.uibinder;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.googlecode.gwt.test.GwtTestTest;

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

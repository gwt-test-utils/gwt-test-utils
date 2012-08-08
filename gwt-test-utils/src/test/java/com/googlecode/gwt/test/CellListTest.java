package com.googlecode.gwt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.googlecode.gwt.test.utils.events.Browser;
import com.googlecode.gwt.test.utils.events.Browser.BrowserErrorHandler;

public class CellListTest extends GwtTestTest {

   private static final List<String> DAYS = Arrays.asList("Sunday", "Monday", "Tuesday",
            "Wednesday", "Thursday", "Friday", "Saturday");

   private CellList<String> cellList;

   private final StringBuilder sb = new StringBuilder();

   @Before
   public void beforeCellListTest() {
      sb.delete(0, sb.length());

      setBrowserErrorHandler(new BrowserErrorHandler() {

         public void onError(String errorMessage) {
            sb.append(errorMessage);
         }
      });

      // Create a cell to render each value.
      TextCell textCell = new TextCell();

      // Create the CellList that uses the cell.
      cellList = new CellList<String>(textCell);

      // Set the total row count. This isn't strictly necessary, but it affects
      // paging calculations, so its good habit to keep the row count up to
      // date.
      cellList.setRowCount(DAYS.size(), true);

      // Push the data into the widget.
      cellList.setRowData(0, DAYS);

      cellList.setVisibleRange(0, 5);

      // Add it to the root panel.
      RootPanel.get().add(cellList);

      // Pre-Assert
      assertEquals(DAYS.size(), cellList.getRowCount());
      assertEquals(5, cellList.getVisibleItemCount());
      assertEquals("Thursday", cellList.getVisibleItem(cellList.getVisibleItemCount() - 1));
   }

   @Test
   public void selectWithClick() {
      // Arrange
      final StringBuilder sb = new StringBuilder();

      final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
      cellList.setSelectionModel(selectionModel);
      selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
         public void onSelectionChange(SelectionChangeEvent event) {
            String selected = selectionModel.getSelectedObject();
            if (selected != null) {
               sb.append("selected : " + selected);
            }
         }
      });

      // Act 1
      Browser.click(cellList, "Wednesday");

      // Assert 1
      assertEquals("selected : Wednesday", sb.toString());
      assertTrue(cellList.getSelectionModel().isSelected("Wednesday"));

      // Act 2 : deselect

      Browser.click(cellList, "Wednesday");

      // Assert 2
      assertEquals("selected : Wednesday", sb.toString());
      assertFalse(cellList.getSelectionModel().isSelected("Wednesday"));
   }

   @Test
   public void selectWithClick_OutOfRange() {
      // Arrange
      final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
      cellList.setSelectionModel(selectionModel);
      selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
         public void onSelectionChange(SelectionChangeEvent event) {
            String selected = selectionModel.getSelectedObject();
            if (selected != null) {
               sb.append("selected : " + selected);
            }
         }
      });

      Browser.click(cellList, "Saturday");

      // Assert : no trigger because "Saturday" is not visible
      assertEquals("the item to click is now visible in the targeted CellList instance",
               sb.toString());
      assertFalse(cellList.getSelectionModel().isSelected("Saturday"));
   }
}

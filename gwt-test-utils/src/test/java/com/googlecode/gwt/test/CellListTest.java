package com.googlecode.gwt.test;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.googlecode.gwt.test.utils.events.Browser;
import com.googlecode.gwt.test.utils.events.Browser.BrowserErrorHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CellListTest extends GwtTestTest {

    private static final List<String> DAYS = Arrays.asList("Sunday", "Monday", "Tuesday",
            "Wednesday", "Thursday", "Friday", "Saturday");
    private final StringBuilder sb = new StringBuilder();
    private CellList<String> cellList;

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

        // Preconditions
        assertThat(cellList.getRowCount()).isEqualTo(DAYS.size());
        assertThat(cellList.getVisibleItemCount()).isEqualTo(5);
        assertThat(cellList.getVisibleItem(cellList.getVisibleItemCount() - 1)).isEqualTo("Thursday");
    }

    @Test
    public void selectWithClick() {
        // Given
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

        // When 1
        Browser.click(cellList, "Wednesday");

        // Then 1
        assertThat(sb.toString()).isEqualTo("selected : Wednesday");
        assertThat(cellList.getSelectionModel().isSelected("Wednesday")).isTrue();

        // When 2 : deselect

        Browser.click(cellList, "Wednesday");

        // Then 2
        assertThat(sb.toString()).isEqualTo("selected : Wednesday");
        assertThat(cellList.getSelectionModel().isSelected("Wednesday")).isFalse();
    }

    @Test
    public void selectWithClick_OutOfRange() {
        // Given
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

        // Then : no trigger because "Saturday" is not visible
        assertThat(sb.toString()).isEqualTo("the item to click is now visible in the targeted CellList instance");
        assertThat(cellList.getSelectionModel().isSelected("Saturday")).isFalse();
    }
}

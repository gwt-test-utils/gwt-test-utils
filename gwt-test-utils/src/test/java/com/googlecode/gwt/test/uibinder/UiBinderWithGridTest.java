package com.googlecode.gwt.test.uibinder;

import com.google.gwt.user.client.ui.Grid;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class UiBinderWithGridTest extends GwtTestTest {

    @Test
    public void instanciation() {
        // Act
        UiBinderWithGrid uiBinderGrid = new UiBinderWithGrid();

        Grid grid = uiBinderGrid.getGrid();

        // Assert
        assertEquals(new Integer(2), new Integer(grid.getRowCount()));
        assertSame(uiBinderGrid.myLabel, grid.getWidget(0, 0));
        assertSame(uiBinderGrid.myHTML, grid.getWidget(0, 1));
        assertEquals(uiBinderGrid.myDiv.toString(),
                grid.getCellFormatter().getElement(1, 0).getInnerHTML());
        assertEquals(uiBinderGrid.mySpan.toString(),
                grid.getCellFormatter().getElement(1, 1).getInnerHTML());
        // styles
        assertEquals("optionalHeaderStyle", grid.getRowFormatter().getStyleName(0));
        assertEquals("optionalFooCellStyle", grid.getCellFormatter().getStyleName(0, 0));
        assertEquals("optionalSpanCellStyle", grid.getCellFormatter().getStyleName(1, 1));
    }

}

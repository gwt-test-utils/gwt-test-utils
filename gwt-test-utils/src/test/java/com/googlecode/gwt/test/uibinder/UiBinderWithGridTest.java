package com.googlecode.gwt.test.uibinder;

import com.google.gwt.user.client.ui.Grid;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UiBinderWithGridTest extends GwtTestTest {

    @Test
    public void instanciation() {
        // When
        UiBinderWithGrid uiBinderGrid = new UiBinderWithGrid();

        Grid grid = uiBinderGrid.getGrid();

        // Then
        assertThat(grid.getRowCount()).isEqualTo(2);
        assertThat(grid.getWidget(0, 0)).isSameAs(uiBinderGrid.myLabel);
        assertThat(grid.getWidget(0, 1)).isSameAs(uiBinderGrid.myHTML);
        assertThat(grid.getCellFormatter().getElement(1, 0).getInnerHTML()).isEqualTo(uiBinderGrid.myDiv.toString());
        assertThat(grid.getCellFormatter().getElement(1, 1).getInnerHTML()).isEqualTo(uiBinderGrid.mySpan.toString());
        // styles
        assertThat(grid.getRowFormatter().getStyleName(0)).isEqualTo("optionalHeaderStyle");
        assertThat(grid.getCellFormatter().getStyleName(0, 0)).isEqualTo("optionalFooCellStyle");
        assertThat(grid.getCellFormatter().getStyleName(1, 1)).isEqualTo("optionalSpanCellStyle");
    }

}

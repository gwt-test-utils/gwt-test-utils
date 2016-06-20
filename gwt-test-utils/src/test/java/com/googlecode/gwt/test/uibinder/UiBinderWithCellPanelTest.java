package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UiBinderWithCellPanelTest extends GwtTestTest {

    @Test
    public void uiBinderWithCellPanel() {
        // When
        UiBinderWithCellPanel uiPanel = GWT.create(UiBinderWithCellPanel.class);

        // Then
        assertThat(uiPanel.leftSide.getText()).isEqualTo("Left");
        assertThat(uiPanel.rightSide.getText()).isEqualTo("Right");
        assertThat(uiPanel.getPanel().getWidgetCount()).isEqualTo(2);
        assertThat(uiPanel.toString()).isEqualTo("<table cellSpacing=\"0\" cellPadding=\"0\"><tbody><tr><td align=\"right\" style=\"vertical-align: top; \" width=\"5em\"><div class=\"gwt-Label\">Left</div></td><td align=\"left\" style=\"vertical-align: top; \" width=\"15em\"><div class=\"gwt-Label\">Right</div></td></tr></tbody></table>");
    }
}

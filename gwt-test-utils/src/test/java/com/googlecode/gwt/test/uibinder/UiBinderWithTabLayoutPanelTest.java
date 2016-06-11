package com.googlecode.gwt.test.uibinder;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UiBinderWithTabLayoutPanelTest extends GwtTestTest {

    @Test
    public void uiBinderWithTabLayoutPanel() {
        // Arrange
        UiBinderWithTabLayoutPanel panel = new UiBinderWithTabLayoutPanel();

        // Act
        RootLayoutPanel.get().add(panel);

        // Assert
        assertEquals("able", panel.first.getText());
        assertEquals("Custom header", panel.customHeader.getText());
        assertEquals("baker", panel.second.getText());

        assertEquals("<b>HTML</b>",
                panel.getPanel().getTabWidget(0).getElement().getInnerHTML());

        assertEquals(panel.customHeader, panel.getPanel().getTabWidget(panel.second));

    }

}

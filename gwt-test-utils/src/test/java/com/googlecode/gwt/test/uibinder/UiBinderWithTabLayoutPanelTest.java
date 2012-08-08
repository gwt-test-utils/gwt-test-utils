package com.googlecode.gwt.test.uibinder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.googlecode.gwt.test.GwtTestTest;

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

      assertEquals("<b>HTML</b> header",
               panel.getPanel().getTabWidget(0).getElement().getInnerHTML());

      assertEquals(panel.customHeader, panel.getPanel().getTabWidget(panel.second));

   }

}

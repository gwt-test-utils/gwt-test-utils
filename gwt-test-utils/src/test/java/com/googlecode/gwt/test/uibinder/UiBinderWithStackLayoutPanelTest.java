package com.googlecode.gwt.test.uibinder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.googlecode.gwt.test.GwtTestTest;

public class UiBinderWithStackLayoutPanelTest extends GwtTestTest {

   @Test
   public void uiBinderWithStackLayoutPanel() {
      // Arrange
      UiBinderWithStackLayoutPanel panel = new UiBinderWithStackLayoutPanel();

      // Act
      RootLayoutPanel.get().add(panel);

      // Assert
      assertEquals("able", panel.first.getText());
      assertEquals("Custom header", panel.customHeader.getText());
      assertEquals("baker", panel.second.getText());
      assertEquals(panel.first, panel.getPanel().getVisibleWidget());

      assertEquals("<b>HTML</b> header",
               panel.getPanel().getHeaderWidget(0).getElement().getInnerHTML());

      assertEquals(panel.customHeader, panel.getPanel().getHeaderWidget(panel.second));
   }

}

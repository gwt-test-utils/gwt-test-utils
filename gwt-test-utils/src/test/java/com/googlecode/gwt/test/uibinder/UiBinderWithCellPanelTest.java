package com.googlecode.gwt.test.uibinder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.googlecode.gwt.test.GwtTestTest;

public class UiBinderWithCellPanelTest extends GwtTestTest {

   @Test
   public void uiBinderWithCellPanel() {
      // Act
      UiBinderWithCellPanel uiPanel = GWT.create(UiBinderWithCellPanel.class);

      // Assert
      assertEquals("Left", uiPanel.leftSide.getText());
      assertEquals("Right", uiPanel.rightSide.getText());
      assertEquals(2, uiPanel.getPanel().getWidgetCount());
      assertEquals(
               "<table cellSpacing=\"0\" cellPadding=\"0\"><tbody><tr><td align=\"right\" style=\"vertical-align: top; \" width=\"5em\"><div class=\"gwt-Label\">Left</div></td><td align=\"left\" style=\"vertical-align: top; \" width=\"15em\"><div class=\"gwt-Label\">Right</div></td></tr></tbody></table>",
               uiPanel.toString());
   }
}

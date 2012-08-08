package com.googlecode.gwt.test.uibinder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.user.client.ui.DockLayoutPanel.Direction;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.googlecode.gwt.test.GwtTestTest;

public class UiBinderWithDockLayoutPanelTest extends GwtTestTest {

   @Test
   public void uiBinderWithDockLayoutPanel() {
      // Arrange
      UiBinderWithDockLayoutPanel panel = new UiBinderWithDockLayoutPanel();

      // Act
      RootLayoutPanel.get().add(panel);

      // Assert
      assertEquals("North", panel.northLabel.getText());
      assertEquals("Center", panel.centerLabel.getText());
      assertEquals("East", panel.eastLabel.getText());
      assertEquals("South", panel.southLabel.getText());
      assertEquals("Center", panel.centerLabel.getText());
      assertEquals(Direction.NORTH, panel.getLayout().getWidgetDirection(panel.northLabel));
      assertEquals("<ul><li id=\"li-west0\">west0</li><li id=\"li-west1\">west1</li></ul>",
               panel.westHTML.getHTML());

      LIElement li0 = panel.westHTML.getElement().getFirstChildElement().getChild(0).cast();
      LIElement li1 = panel.westHTML.getElement().getFirstChildElement().getChild(1).cast();
      assertEquals("west0", li0.getInnerText());
      assertEquals("west1", li1.getInnerText());

      assertEquals(li0, Document.get().getElementById("li-west0"));
      assertEquals(li1, Document.get().getElementById("li-west1"));
   }

}

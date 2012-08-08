package com.googlecode.gwt.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackPanel;
import com.googlecode.gwt.test.utils.events.Browser;

public class StackPanelTest extends GwtTestTest {

   private int index = -1;

   @Test
   public void click() {
      // Arrange
      index = -1;
      StackPanel panel = new StackPanel() {

         @Override
         public void showStack(int index) {
            StackPanelTest.this.index = index;
         };
      };

      panel.add(new Anchor());
      panel.add(new Anchor());

      // Act
      Browser.click(panel, 1);

      // Assert
      assertEquals(1, index);
   }

   @Test
   public void stackPanel() {
      // Arrange
      StackPanel panel = new StackPanel();

      // Act
      panel.add(new Label("Foo"), "foo");
      Label label = new Label("Bar");
      panel.add(label, "bar");
      panel.add(new Label("Baz"), "baz");

      // Assert
      assertEquals(3, panel.getWidgetCount());
      assertEquals(label, panel.getWidget(1));
      assertEquals(1, panel.getWidgetIndex(label));
   }

   @Test
   public void title() {
      // Arrange
      StackPanel sp = new StackPanel();
      // Pre-Assert
      assertEquals("", sp.getTitle());

      // Act
      sp.setTitle("title");

      // Assert
      assertEquals("title", sp.getTitle());
   }

   @Test
   public void visible() {
      // Arrange
      StackPanel sp = new StackPanel();
      // Pre-Assert
      assertEquals(true, sp.isVisible());

      // Act
      sp.setVisible(false);

      // Assert
      assertEquals(false, sp.isVisible());
   }

}

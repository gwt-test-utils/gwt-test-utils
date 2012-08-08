package com.googlecode.gwt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class RootPanelTest extends GwtTestTest {

   @Test
   public void add() {
      // Arrange
      Label label = new Label();
      assertFalse(label.isAttached());

      // Act
      RootPanel.get().add(label);

      // Assert
      assertEquals(label, RootPanel.get().getWidget(0));
      assertTrue(label.isAttached());
   }

   @Test(expected = IndexOutOfBoundsException.class)
   public void init() {
      // Pre-Assert
      assertEquals(0, RootPanel.get().getWidgetCount());

      // Act
      RootPanel.get().getWidget(0);
   }

}

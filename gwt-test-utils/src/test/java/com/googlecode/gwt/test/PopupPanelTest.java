package com.googlecode.gwt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gwt.user.client.ui.PopupPanel;

public class PopupPanelTest extends GwtTestTest {

   @Test
   public void autoHideEnabled() {
      // Arrange
      PopupPanel popupPanel = new PopupPanel(true);
      // Pre-Assert
      assertTrue(popupPanel.isAutoHideEnabled());

      // Act
      popupPanel.setAutoHideEnabled(false);

      // Assert
      assertFalse(popupPanel.isAutoHideEnabled());
   }

   @Test
   public void center() {
      // Arrange
      PopupPanel popup = new PopupPanel();
      popup.setAnimationEnabled(true);

      // Act
      popup.center();

      // Assert
      assertEquals(0, popup.getOffsetHeight());
      assertEquals(0, popup.getOffsetWidth());
   }

   @Test
   public void show() {
      // Arrange
      PopupPanel popup = new PopupPanel();
      // Pre-Assert
      assertFalse(popup.isShowing());

      // Act
      popup.show();

      // Assert
      assertTrue(popup.isShowing());
   }

   @Test
   public void showGlass() {
      // Arrange
      PopupPanel popup = new PopupPanel();
      popup.setGlassEnabled(true);
      // Pre-Assert
      assertFalse(popup.isShowing());

      // Act
      popup.show();

      // Assert
      assertTrue(popup.isShowing());
   }

   @Test
   public void visible() {
      // Arrange
      PopupPanel popup = new PopupPanel();
      // Pre-Assert
      assertFalse(popup.isVisible());
      assertEquals("hidden", popup.getElement().getStyle().getProperty("visibility"));

      // Act
      popup.setVisible(true);

      // Assert
      assertTrue(popup.isVisible());
      assertEquals("visible", popup.getElement().getStyle().getProperty("visibility"));
   }

}

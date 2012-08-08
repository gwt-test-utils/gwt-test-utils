package com.googlecode.gwt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.googlecode.gwt.test.utils.events.Browser;

public class ToggleButtonTest extends GwtTestTest {

   private boolean clicked;

   @Test
   public void click() {
      // Arrange
      final ToggleButton toggleButton = new ToggleButton("Up", "Down");

      // needs to be attached
      RootPanel.get().add(toggleButton);

      clicked = false;

      toggleButton.addClickHandler(new ClickHandler() {
         public void onClick(ClickEvent event) {
            clicked = true;
         }
      });

      // Pre-Assert
      assertFalse("ToggleButton should not be toggled by default", toggleButton.isDown());
      assertEquals("Up", toggleButton.getText());

      // Act
      Browser.click(toggleButton);

      // Assert
      assertTrue("ToggleButton onClick was not triggered", clicked);
      assertTrue("ToggleButton should be toggled after being clicked once", toggleButton.isDown());
      assertEquals("Down", toggleButton.getText());

      // Act 2
      Browser.click(toggleButton);
      assertFalse("ToggleButton should not be toggled after being clicked twice",
               toggleButton.isDown());
      assertEquals("Up", toggleButton.getText());
   }

}

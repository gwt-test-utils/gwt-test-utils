package com.googlecode.gwt.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.user.client.ui.Button;

public class DebugIdDisabledTest extends GwtTestTest {

   @Override
   public boolean ensureDebugId() {
      return false;
   }

   @Test
   public void ensureDebugId_Disabled() {
      // Arrange
      Button b = new Button();

      // Act
      b.ensureDebugId("myDebugId");

      // Assert
      assertEquals("", b.getElement().getId());
   }

}

package com.googlecode.gwt.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.user.client.ui.Button;

public class DebugIdEnabledTest extends GwtTestTest {

   @Override
   public boolean ensureDebugId() {
      return true;
   }

   @Test
   public void ensureDebugId_Enabled() {
      // Arrange
      Button b = new Button();

      // Act
      b.ensureDebugId("myDebugId");

      // Assert
      assertEquals("gwt-debug-myDebugId", b.getElement().getId());
   }

}

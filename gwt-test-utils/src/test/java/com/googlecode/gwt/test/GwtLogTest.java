package com.googlecode.gwt.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.gwt.core.client.GWT;

public class GwtLogTest extends GwtTestTest {

   private String message;

   private Throwable t;

   @Before
   public void beforeGwtLogTest() {
      setLogHandler(new GwtLogHandler() {

         public void log(String message, Throwable t) {
            GwtLogTest.this.message = message;
            GwtLogTest.this.t = t;
         }

      });
   }

   @Test
   public void log() {
      // Arrange
      message = null;
      t = null;
      Throwable throwable = new Exception("test");

      // Act
      GWT.log("toto", throwable);

      // Assert
      assertEquals("toto", message);
      assertEquals(throwable, t);
   }
}

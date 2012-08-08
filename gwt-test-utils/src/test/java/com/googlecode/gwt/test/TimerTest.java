package com.googlecode.gwt.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gwt.user.client.Timer;

public class TimerTest extends GwtTestTest {

   boolean bool;
   int i;

   @Test
   public void schedule() throws Exception {
      // Arrange
      bool = false;
      Timer timer = new Timer() {

         @Override
         public void run() {
            bool = !bool;
         }
      };

      // Act
      timer.schedule(500);

      // Assert
      assertTrue("The token was not set after Timer has run", bool);
   }

   @Test
   public void scheduleRepeating() throws Exception {
      // Arrange
      i = 0;
      Timer timer = new Timer() {

         @Override
         public void run() {
            i++;
         }
      };

      // Act
      timer.scheduleRepeating(500);

      // Assert
      assertTrue("timer should be run more than once", i > 1);
   }
}

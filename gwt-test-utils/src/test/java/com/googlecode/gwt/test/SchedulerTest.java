package com.googlecode.gwt.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;

public class SchedulerTest extends GwtTestTest {

   @Test
   public void scheduleIncremental() {
      // Arrange
      final StringBuilder sb = new StringBuilder();
      final int COUNT = 2;

      RepeatingCommand command = new RepeatingCommand() {

         private int index = 0;

         public boolean execute() {
            sb.append(index++);
            return index <= COUNT;
         }
      };

      // Act
      Scheduler.get().scheduleIncremental(command);

      // Assert
      assertEquals("012", sb.toString());
   }
}

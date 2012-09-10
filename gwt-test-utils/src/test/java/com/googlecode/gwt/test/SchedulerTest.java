package com.googlecode.gwt.test;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

public class SchedulerTest extends GwtTestTest {

   @Test
   public void scheduleDeferred() {
      // Arrange
      final StringBuilder sb = new StringBuilder();

      Scheduler.get().scheduleDeferred(new ScheduledCommand() {

         public void execute() {
            sb.append("scheduleDeferred");

         }
      });

      // Pre-Assert
      assertThat(sb.toString()).isEmpty();

      // Act
      getBrowserSimulator().fireLoopEnd();

      // Assert
      assertThat(sb.toString()).isEqualTo("scheduleDeferred");
   }

   @Test
   public void scheduleEntry() {
      // Arrange
      final StringBuilder sb = new StringBuilder();

      Scheduler.get().scheduleEntry(new ScheduledCommand() {

         public void execute() {
            sb.append("scheduleEntry");

         }
      });

      // Pre-Assert
      assertThat(sb.toString()).isEmpty();

      // Act
      getBrowserSimulator().fireLoopEnd();

      // Assert
      assertThat(sb.toString()).isEqualTo("scheduleEntry");
   }

   @Test
   public void scheduleFinally() {
      // Arrange
      final StringBuilder sb = new StringBuilder();

      Scheduler.get().scheduleFinally(new ScheduledCommand() {

         public void execute() {
            sb.append("scheduleFinally");

         }
      });

      // Pre-Assert
      assertThat(sb.toString()).isEmpty();

      // Act
      getBrowserSimulator().fireLoopEnd();

      // Assert
      assertThat(sb.toString()).isEqualTo("scheduleFinally");
   }

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
      assertThat(sb.toString()).isEqualTo("012");
   }

   @Test
   public void scheduleOrder() {
      // Arrange
      final StringBuilder sb = new StringBuilder();

      Scheduler.get().scheduleEntry(new ScheduledCommand() {

         public void execute() {
            sb.append("scheduleEntry1 ");

         }
      });

      Scheduler.get().scheduleFinally(new ScheduledCommand() {

         public void execute() {
            sb.append("scheduleFinally1 ");

         }
      });

      Scheduler.get().scheduleDeferred(new ScheduledCommand() {

         public void execute() {
            sb.append("scheduleDeferred1 ");

            Scheduler.get().scheduleEntry(new ScheduledCommand() {

               public void execute() {
                  sb.append("scheduleEntry2 ");

                  Scheduler.get().scheduleEntry(new ScheduledCommand() {

                     public void execute() {
                        sb.append("scheduleEntry3 ");

                     }
                  });

               }
            });

            Scheduler.get().scheduleFinally(new ScheduledCommand() {

               public void execute() {
                  sb.append("scheduleFinally2 ");

                  Scheduler.get().scheduleFinally(new ScheduledCommand() {

                     public void execute() {
                        sb.append("scheduleFinally3 ");

                     }
                  });

               }
            });

            Scheduler.get().scheduleDeferred(new ScheduledCommand() {

               public void execute() {
                  sb.append("scheduleDeferred2 ");

               }
            });

         }
      });

      // Act 1
      getBrowserSimulator().fireLoopEnd();

      // Assert 1
      assertThat(sb.toString()).isEqualTo("scheduleFinally1 scheduleEntry1 scheduleDeferred1 ");

      // Act 2
      getBrowserSimulator().fireLoopEnd();

      // Assert 2
      assertThat(sb.toString()).isEqualTo(
               "scheduleFinally1 scheduleEntry1 scheduleDeferred1 scheduleFinally2 scheduleFinally3 scheduleEntry2 scheduleEntry3 scheduleDeferred2 ");

   }
}

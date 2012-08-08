package com.googlecode.gwt.test;

import java.util.LinkedList;
import java.util.Queue;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import com.googlecode.gwt.test.internal.AfterTestCallback;
import com.googlecode.gwt.test.internal.AfterTestCallbackManager;

/**
 * Trigger {@link ScheduledCommand} and {@link RepeatingCommand} which were scheduled after all DOM
 * manipulation.
 * 
 * @see Scheduler#scheduleFinally(ScheduledCommand)
 * @see Scheduler#scheduleFinally(RepeatingCommand)
 * 
 * @author Gael Lazzari
 * 
 */
public class FinallyCommandTrigger implements AfterTestCallback {

   private static final FinallyCommandTrigger INSTANCE = new FinallyCommandTrigger();

   public static void add(RepeatingCommand repeatingCommand) {
      INSTANCE.repeatingCommands.add(repeatingCommand);
   }

   public static void add(ScheduledCommand scheduledCommand) {
      INSTANCE.scheduledCommands.add(scheduledCommand);
   }

   public static void clearPendingCommands() {
      INSTANCE.repeatingCommands.clear();
      INSTANCE.scheduledCommands.clear();
   }

   public static void triggerCommands() {
      if (INSTANCE.isTriggering) {
         return;
      }

      try {
         INSTANCE.isTriggering = true;

         while (!INSTANCE.repeatingCommands.isEmpty()) {
            executeRepeatingCommand(INSTANCE.repeatingCommands.poll());
         }

         while (!INSTANCE.scheduledCommands.isEmpty()) {
            INSTANCE.scheduledCommands.poll().execute();
         }
      } finally {
         INSTANCE.isTriggering = false;
      }
   }

   private static void executeRepeatingCommand(RepeatingCommand cmd) {
      boolean repeat = true;
      while (repeat) {
         repeat = cmd.execute();
      }
   }

   private boolean isTriggering;
   private final Queue<RepeatingCommand> repeatingCommands = new LinkedList<Scheduler.RepeatingCommand>();

   private final Queue<ScheduledCommand> scheduledCommands = new LinkedList<Scheduler.ScheduledCommand>();

   private FinallyCommandTrigger() {
      AfterTestCallbackManager.get().registerCallback(this);
   }

   /**
    * Check there is no pending command to execute. A {@link GwtTestPatchException} would we thrown.
    */
   public void afterTest() throws Throwable {

      int nb = repeatingCommands.size() + scheduledCommands.size();

      if (nb > 0) {
         repeatingCommands.clear();
         scheduledCommands.clear();

         String message = (nb > 1)
                  ? "There are "
                           + nb
                           + " pending commands which were scheduled to run after DOM manipulation. You have to trigger them by calling '"
                  : "There is one pending command which was scheduled to run after DOM manipulation. You have to trigger it by calling '";

         throw new GwtTestPatchException(message + FinallyCommandTrigger.class.getName()
                  + ".triggerCommands() static method AFTER arranging your test");
      }

   }

}

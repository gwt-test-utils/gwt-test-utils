package com.googlecode.gwt.test.internal;

import java.util.LinkedList;
import java.util.Queue;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Command;
import com.googlecode.gwt.test.BrowserSimulator;
import com.googlecode.gwt.test.exceptions.GwtTestException;

/**
 * Trigger {@link ScheduledCommand}, {@link RepeatingCommand} and RPC callbacks which were scheduled
 * to be run before and after the Browser
 * 
 * @see Scheduler
 * 
 * @author Gael Lazzari
 * 
 */
public class BrowserSimulatorImpl implements BrowserSimulator, AfterTestCallback {

   private static final BrowserSimulatorImpl INSTANCE = new BrowserSimulatorImpl();

   public static BrowserSimulatorImpl get() {
      return INSTANCE;
   }

   private static void executeRepeatingCommand(RepeatingCommand cmd) {
      boolean repeat = true;
      while (repeat) {
         repeat = cmd.execute();
      }
   }

   private final Queue<Command> asyncCallbackCommands = new LinkedList<Command>();
   private final Queue<ScheduledCommand> deferredScheduledCommands = new LinkedList<Scheduler.ScheduledCommand>();
   private final Queue<RepeatingCommand> entryRepeatingCommands = new LinkedList<Scheduler.RepeatingCommand>();
   private final Queue<ScheduledCommand> entryScheduledCommands = new LinkedList<Scheduler.ScheduledCommand>();
   private final Queue<RepeatingCommand> finallyRepeatingCommands = new LinkedList<Scheduler.RepeatingCommand>();
   private final Queue<ScheduledCommand> finallyScheduledCommands = new LinkedList<Scheduler.ScheduledCommand>();

   private boolean isTriggering;

   private BrowserSimulatorImpl() {
      AfterTestCallbackManager.get().registerCallback(this);
   }

   /**
    * Check there is no pending command to execute. A {@link GwtTestException} would we thrown.
    */
   public void afterTest() throws Throwable {

      if (deferredScheduledCommands.size() == 0 //
               && finallyScheduledCommands.size() == 0 //
               && finallyRepeatingCommands.size() == 0 //
               && entryScheduledCommands.size() == 0 //
               && entryRepeatingCommands.size() == 0 //
               && asyncCallbackCommands.size() == 0) {
         return;
      }

      String testName = GwtConfig.get().getModuleRunner().getClass().getSimpleName();
      String format = "%s pending %s must be triggered manually by calling %s.getBrowserSimulator().fireLoopEnd() before making your test assertions";
      String errorMessage = null;

      if (deferredScheduledCommands.size() > 0) {
         errorMessage = String.format(format, deferredScheduledCommands.size(),
                  "scheduledDeferred ScheduledCommand(s)", testName);
      } else if (entryScheduledCommands.size() > 0) {
         errorMessage = String.format(format, entryScheduledCommands.size(),
                  "scheduledEntry ScheduledCommand(s)", testName);
      } else if (entryRepeatingCommands.size() > 0) {
         errorMessage = String.format(format, entryRepeatingCommands.size(),
                  "scheduledEntry RepeatingCommand(s)", testName);
      } else if (finallyScheduledCommands.size() > 0) {
         errorMessage = String.format(format, finallyRepeatingCommands.size(),
                  "scheduledFinally ScheduledCommand(s)", testName);
      } else if (finallyRepeatingCommands.size() > 0) {
         errorMessage = String.format(format, finallyRepeatingCommands.size(),
                  "scheduledFinally RepeatingCommand(s)", testName);
      } else {
         errorMessage = String.format(format, asyncCallbackCommands.size(), "AsyncCallback",
                  testName);
      }

      clearPendingCommands();

      throw new GwtTestException(errorMessage);

   }

   public void clearPendingCommands() {
      deferredScheduledCommands.clear();
      entryScheduledCommands.clear();
      entryRepeatingCommands.clear();
      finallyScheduledCommands.clear();
      finallyRepeatingCommands.clear();
      asyncCallbackCommands.clear();
   }

   public void fireLoopEnd() {
      if (isTriggering) {
         return;
      }

      try {
         isTriggering = true;

         while (!finallyScheduledCommands.isEmpty()) {
            finallyScheduledCommands.poll().execute();
         }

         while (!finallyRepeatingCommands.isEmpty()) {
            executeRepeatingCommand(finallyRepeatingCommands.poll());
         }

         while (!entryScheduledCommands.isEmpty()) {
            entryScheduledCommands.poll().execute();
         }

         while (!entryRepeatingCommands.isEmpty()) {
            executeRepeatingCommand(entryRepeatingCommands.poll());
         }

         Queue<ScheduledCommand> tempDeferredScheduledCommands = new LinkedList<Scheduler.ScheduledCommand>(
                  deferredScheduledCommands);

         while (!tempDeferredScheduledCommands.isEmpty()) {
            ScheduledCommand cmd = tempDeferredScheduledCommands.poll();
            cmd.execute();

            deferredScheduledCommands.remove(cmd);
         }

         while (!asyncCallbackCommands.isEmpty()) {
            asyncCallbackCommands.poll().execute();
         }

      } finally {
         isTriggering = false;
      }
   }

   public void recordAsyncCall(Command asyncCallbackCommand) {
      asyncCallbackCommands.add(asyncCallbackCommand);
   }

   public void scheduleDeferred(ScheduledCommand scheduledCommand) {
      deferredScheduledCommands.add(scheduledCommand);
   }

   public void scheduleEntry(RepeatingCommand repeatingCommand) {
      entryRepeatingCommands.add(repeatingCommand);
   }

   public void scheduleEntry(ScheduledCommand scheduledCommand) {
      entryScheduledCommands.add(scheduledCommand);
   }

   public void scheduleFinally(RepeatingCommand repeatingCommand) {
      finallyRepeatingCommands.add(repeatingCommand);
   }

   public void scheduleFinally(ScheduledCommand scheduledCommand) {
      finallyScheduledCommands.add(scheduledCommand);
   }

}

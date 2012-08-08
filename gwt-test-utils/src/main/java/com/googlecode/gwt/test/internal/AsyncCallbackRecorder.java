package com.googlecode.gwt.test.internal;

import java.util.LinkedList;
import java.util.Queue;

import com.google.gwt.user.client.Command;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;

/**
 * <strong>For internal use only.</strong>
 * 
 * @author Gael Lazzari
 * 
 */
public class AsyncCallbackRecorder implements AfterTestCallback {

   public static final AsyncCallbackRecorder INSTANCE = new AsyncCallbackRecorder();

   public static AsyncCallbackRecorder get() {
      return INSTANCE;
   }

   private final Queue<Command> asyncCallbackCommands;

   private int recordingCount = 0;

   private AsyncCallbackRecorder() {
      this.asyncCallbackCommands = new LinkedList<Command>();
      AfterTestCallbackManager.get().registerCallback(this);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.googlecode.gwt.test.internal.AfterTestCallback#afterTest()
    */
   public void afterTest() throws Throwable {
      if (asyncCallbackCommands.size() > 0) {
         asyncCallbackCommands.clear();
         recordingCount = 0;
         throw new GwtTestPatchException(
                  "There are "
                           + asyncCallbackCommands.size()
                           + " pending asynchronus server calls. You have to trigger their callbacks manually be calling "
                           + AsyncCallbackRecorder.class.getName()
                           + ".triggerRecordedCallbacks() method in your test");
      }
   }

   /**
    * 
    * @param asyncCallbackCommand
    */
   public void handleAsyncCallback(Command asyncCallbackCommand) {
      if (recordingCount > 0) {
         asyncCallbackCommands.add(asyncCallbackCommand);
      } else {
         asyncCallbackCommand.execute();
      }
   }

   public void recordAsyncCalls() {
      recordingCount++;
   }

   public void triggerRecordedAsyncCallbacks() {
      recordingCount--;

      if (recordingCount == 0) {
         while (!asyncCallbackCommands.isEmpty()) {
            asyncCallbackCommands.poll().execute();
         }
      }
   }

}

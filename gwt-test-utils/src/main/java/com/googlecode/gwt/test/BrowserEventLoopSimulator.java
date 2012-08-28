package com.googlecode.gwt.test;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

/**
 * A Browser event-loop simulator interface, to give a programmatic way of running
 * {@link ScheduledCommand}, {@link RepeatingCommand} and RPC callbacks.
 * 
 * <p>
 * A browser event loop end is automatically triggered by gwt-test-utils before and after a DOM
 * event is processed.
 * </p>
 * 
 * @author Gael Lazzari
 * 
 * @see Scheduler#scheduleDeferred(ScheduledCommand)
 * @see Scheduler#scheduleFinally(ScheduledCommand)
 * @see Scheduler#scheduleFinally(RepeatingCommand)
 * 
 */
public interface BrowserEventLoopSimulator {

   /**
    * Fire a Browser event loop end, so that commands scheduled with
    * {@link Scheduler#scheduleDeferred(ScheduledCommand)},
    * {@link Scheduler#scheduleFinally(ScheduledCommand)} and
    * {@link Scheduler#scheduleFinally(RepeatingCommand)} can be processed. Eventual
    * {@link AsyncCallback} are also executed if there were some {@link RemoteService} call executed
    * during a test.
    */
   void fireLoopEnd();

}

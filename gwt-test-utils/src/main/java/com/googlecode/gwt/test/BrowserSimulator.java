package com.googlecode.gwt.test;

import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

/**
 * A Browser simulation interface, to give a programmatic way of running Browser specific behavior.
 *
 * @author Gael Lazzari
 */
public interface BrowserSimulator {

    /**
     * Fire a Browser event loop end, so that {@link ScheduledCommand} and {@link RepeatingCommand}
     * scheduled before/after GWT-generated code can be processed. Eventual {@link AsyncCallback} are
     * also executed if there were some {@link RemoteService} call executed during a test.
     * <p>
     * <p>
     * A browser event loop end is <strong>automatically triggered</strong> by gwt-test-utils before
     * and after a DOM event is processed.
     * </p>
     * <p>
     * <p>
     * Commands eventually scheduled by an {@link AsyncCallback} should also be triggered just after
     * every queued async callbacks have been executed.
     * </p>
     *
     * @see Scheduler#scheduleDeferred(ScheduledCommand)
     * @see Scheduler#scheduleFinally(ScheduledCommand)
     * @see Scheduler#scheduleFinally(RepeatingCommand)
     * @see Scheduler#scheduleEntry(ScheduledCommand)
     * @see Scheduler#scheduleEntry(RepeatingCommand)
     */
    void fireLoopEnd();

}

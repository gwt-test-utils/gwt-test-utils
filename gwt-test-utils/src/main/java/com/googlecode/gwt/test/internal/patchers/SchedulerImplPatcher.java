package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.client.impl.SchedulerImpl;
import com.googlecode.gwt.test.internal.BrowserSimulatorImpl;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(SchedulerImpl.class)
class SchedulerImplPatcher {

    @PatchMethod
    static void scheduleDeferred(SchedulerImpl impl, ScheduledCommand cmd) {
        BrowserSimulatorImpl.get().scheduleDeferred(cmd);
    }

    @PatchMethod
    static void scheduleEntry(SchedulerImpl impl, RepeatingCommand cmd) {
        BrowserSimulatorImpl.get().scheduleEntry(cmd);
    }

    @PatchMethod
    static void scheduleEntry(SchedulerImpl impl, ScheduledCommand cmd) {
        BrowserSimulatorImpl.get().scheduleEntry(cmd);
    }

    @PatchMethod
    static void scheduleFinally(SchedulerImpl impl, RepeatingCommand cmd) {
        BrowserSimulatorImpl.get().scheduleFinally(cmd);
    }

    @PatchMethod
    static void scheduleFinally(SchedulerImpl impl, ScheduledCommand cmd) {
        BrowserSimulatorImpl.get().scheduleFinally(cmd);
    }

    @PatchMethod
    static void scheduleFixedDelay(SchedulerImpl impl, RepeatingCommand cmd, int delayMs) {
        executeRepeatingCommand(cmd);
    }

    @PatchMethod
    static void scheduleFixedPeriod(SchedulerImpl impl, RepeatingCommand cmd, int delayMs) {
        executeRepeatingCommand(cmd);
    }

    @PatchMethod
    static void scheduleIncremental(SchedulerImpl impl, RepeatingCommand cmd) {
        executeRepeatingCommand(cmd);
    }

    private static void executeRepeatingCommand(RepeatingCommand cmd) {
        boolean repeat = true;
        while (repeat) {
            repeat = cmd.execute();
        }
    }

}

package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.user.client.Timer;
import com.googlecode.gwt.test.internal.AfterTestCallback;
import com.googlecode.gwt.test.internal.AfterTestCallbackManager;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

@PatchClass(Timer.class)
class TimerPatcher {

    private static class TimerHolder implements AfterTestCallback {

        private final Map<Timer, java.util.Timer> timers = new HashMap<>();

        TimerHolder() {
            AfterTestCallbackManager.get().registerCallback(this);
        }

        public void afterTest() throws Throwable {
            for (java.util.Timer timer : timers.values())
                // cancel all pending timers
                timer.cancel();

            // reset the TIMER_HOLDER instance
            timers.clear();
        }

        void cancel(Timer timer) {
            if (!timers.containsKey(timer))
                return;
            this.timers.get(timer).cancel();
        }

        void hold(Timer timer, java.util.Timer impl) {
            timers.put(timer, impl);
        }

    }

    private static final TimerHolder TIMER_HOLDER = new TimerHolder();

    @PatchMethod
    static void cancel(Timer timer) {
        TIMER_HOLDER.cancel(timer);
    }

    @PatchMethod
    static void schedule(final Timer timer, int delayMillis) {
        if (delayMillis <= 0) {
            throw new IllegalArgumentException("must be positive");
        }

        TIMER_HOLDER.cancel(timer);

        java.util.Timer impl = new java.util.Timer();
        TIMER_HOLDER.hold(timer, impl);

        impl.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.run();
            }
        }, delayMillis);

    }

    @PatchMethod
    static void scheduleRepeating(final Timer timer, int periodMillis) {
        if (periodMillis <= 0) {
            throw new IllegalArgumentException("must be positive");
        }

        TIMER_HOLDER.cancel(timer);

        java.util.Timer impl = new java.util.Timer();
        TIMER_HOLDER.hold(timer, impl);

        impl.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timer.run();
            }
        }, periodMillis, periodMillis);

    }

}

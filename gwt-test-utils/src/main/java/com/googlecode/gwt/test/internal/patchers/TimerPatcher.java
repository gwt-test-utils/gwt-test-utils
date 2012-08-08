package com.googlecode.gwt.test.internal.patchers;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.Timer;
import com.googlecode.gwt.test.internal.AfterTestCallback;
import com.googlecode.gwt.test.internal.AfterTestCallbackManager;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(Timer.class)
class TimerPatcher {

   static class TimerHolder implements AfterTestCallback {

      private final Map<Timer, Integer> cache = new HashMap<Timer, Integer>();

      TimerHolder() {
         AfterTestCallbackManager.get().registerCallback(this);
      }

      public void afterTest() throws Throwable {
         cache.clear();
      }

   }

   private static int DEFAULT_REPEAT_TIME = 5;

   private static final TimerHolder TIMER_HOLDER = new TimerHolder();

   @PatchMethod
   static void clearTimeout(int id) {

   }

   @PatchMethod
   static void schedule(Timer timer, int delayMillis) throws Exception {
      if (delayMillis <= 0) {
         throw new IllegalArgumentException("must be positive");
      }

      if (!TIMER_HOLDER.cache.containsKey(timer)) {
         TIMER_HOLDER.cache.put(timer, 0);
      }

      int runTimes = TIMER_HOLDER.cache.get(timer);
      if (runTimes < DEFAULT_REPEAT_TIME) {
         TIMER_HOLDER.cache.put(timer, ++runTimes);
         timer.run();
      }

   }

   @PatchMethod
   static void scheduleRepeating(Timer timer, int periodMillis) throws Exception {
      if (periodMillis <= 0) {
         throw new IllegalArgumentException("must be positive");
      }

      if (!TIMER_HOLDER.cache.containsKey(timer)) {
         TIMER_HOLDER.cache.put(timer, 0);
      }

      int runTimes = TIMER_HOLDER.cache.get(timer);
      while (runTimes < DEFAULT_REPEAT_TIME && TIMER_HOLDER.cache.get(timer) < DEFAULT_REPEAT_TIME) {
         TIMER_HOLDER.cache.put(timer, ++runTimes);
         timer.run();
      }

   }

}

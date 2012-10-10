package com.googlecode.gwt.test;

import java.util.List;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

/**
 * Class in charge of reseting all necessary GWT internal objects after the execution of a unit
 * test. <strong>For internal use only.</strong>
 * 
 * @author Bertrand Paquet
 * @author Gael Lazzari
 * 
 */
public class GwtReset {

   private static final GwtReset INSTANCE = new GwtReset();

   public static GwtReset get() {
      return INSTANCE;
   }

   private static void getStaticAndCallClear(Class<?> clazz, String fieldName) {
      GwtReflectionUtils.callPrivateMethod(
               GwtReflectionUtils.getStaticFieldValue(clazz, fieldName), "clear");
   }

   private GwtReset() {
   }

   public void reset() throws Exception {
      getStaticAndCallClear(Timer.class, "timers");
      getStaticAndCallClear(RootPanel.class, "rootPanels");
      getStaticAndCallClear(RootPanel.class, "widgetsToDetach");
      GwtReflectionUtils.setStaticField(RootLayoutPanel.class, "singleton", null);

      GwtReflectionUtils.setStaticField(AbstractCellTable.class, "TABLE_IMPL", null);

      Object commandExecutor = GwtReflectionUtils.getStaticFieldValue(
               Class.forName("com.google.gwt.user.client.DeferredCommand"), "commandExecutor");
      GwtReflectionUtils.callPrivateMethod(
               GwtReflectionUtils.getPrivateFieldValue(commandExecutor, "commands"), "clear");

      GwtReflectionUtils.setStaticField(NumberFormat.class, "cachedDecimalFormat", null);
      GwtReflectionUtils.setStaticField(NumberFormat.class, "cachedScientificFormat", null);
      GwtReflectionUtils.setStaticField(NumberFormat.class, "cachedPercentFormat", null);
      GwtReflectionUtils.setStaticField(NumberFormat.class, "cachedCurrencyFormat", null);

      GwtReflectionUtils.setPrivateFieldValue(LocaleInfo.getCurrentLocale(), "dateTimeConstants",
               null);
      GwtReflectionUtils.setPrivateFieldValue(LocaleInfo.getCurrentLocale(), "dateTimeFormatInfo",
               null);
      GwtReflectionUtils.setPrivateFieldValue(LocaleInfo.getCurrentLocale(), "numberConstants",
               null);
      getStaticAndCallClear(DateTimeFormat.class, "cache");

      GwtReflectionUtils.setStaticField(Window.class, "handlers", null);

      GwtReflectionUtils.setStaticField(DisclosurePanel.class, "contentAnimation", null);
      GwtReflectionUtils.setStaticField(DeckPanel.class, "slideAnimation", null);

      Class<?> animationSchedulerImplClass = Class.forName("com.google.gwt.animation.client.AnimationSchedulerImplTimer");
      Object animationSchedulerImpl = GwtReflectionUtils.getStaticFieldValue(
               animationSchedulerImplClass, "INSTANCE");
      List<?> animationRequests = (List<?>) GwtReflectionUtils.getPrivateFieldValue(
               animationSchedulerImpl, "animationRequests");
      animationRequests.clear();

      try {
         Class<?> clazz = Class.forName("com.google.gwt.user.client.Event$");
         GwtReflectionUtils.setStaticField(clazz, "handlers", null);
      } catch (GwtTestException e) {
         // something goes wrong with Overlay types support, just ignore the reset
      }

   }
}

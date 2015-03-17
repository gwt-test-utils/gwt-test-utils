package com.googlecode.gwt.test.internal.patchers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.googlecode.gwt.test.internal.AfterTestCallback;
import com.googlecode.gwt.test.internal.AfterTestCallbackManager;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

@PatchClass(target="com.google.gwt.user.client.History.HistoryImpl")
class HistoryImplPatcher {

   static class GwtBrowserHistory implements AfterTestCallback {

      private int currentIndex;
      private final List<String> stack;

      GwtBrowserHistory() {
         this.stack = new ArrayList<String>();
         this.currentIndex = -1;
         AfterTestCallbackManager.get().registerCallback(this);
      }

      /**
       * Add a new token in the history if it does not equal the current token
       *
       * @param token
       */
      public void addToken(String token) {
         String current = getCurrentToken();
         if (!current.equals(token)) {
            // remove possible token which could be reach with a forward so it
            // won't
            // be possible anymore
            while (stack.size() > currentIndex + 1) {
               stack.remove(stack.size() - 1);
            }
            stack.add(token);
            currentIndex = stack.size() - 1;
         }
      }

      public void afterTest() throws Throwable {
         stack.clear();
         currentIndex = -1;

         Object historyImpl = GwtReflectionUtils.getStaticFieldValue(History.class, "impl");
         GwtReflectionUtils.callPrivateMethod(GwtReflectionUtils.getPrivateFieldValue(
                  GwtReflectionUtils.getPrivateFieldValue(
                           GwtReflectionUtils.getPrivateFieldValue(historyImpl, "handlers"),
                           "eventBus"), "map"), "clear");
      }

      /**
       * Simulate a Browser back button click
       *
       * @return the previous token or an empty String
       */
      public String back() {
         String token = null;
         boolean fireEvent = false;
         switch (currentIndex) {
            case 0:
               currentIndex--;
               fireEvent = true;
            case -1:
               token = "";
               break;
            default:
               fireEvent = true;
               token = stack.get(--currentIndex);
         }

         if (fireEvent) {
            fireHistoryChanged(token);
         }

         return token;
      }

      /**
       * Simulate a Browser forward button click
       *
       * @return the next token or an empty String
       */
      public String forward() {
         if (currentIndex >= stack.size() - 1) {
            return "";
         } else {
            String token = stack.get(++currentIndex);
            fireHistoryChanged(token);
            return token;
         }
      }

      /**
       * Return the current token in history
       *
       * @return the current token in history or an empty String if no token is set in the URL
       */
      public String getCurrentToken() {
         return (currentIndex == -1) ? "" : stack.get(currentIndex);
      }

      private void fireHistoryChanged(String token) {
         Object impl = GwtReflectionUtils.getStaticFieldValue(History.class, "impl");
         try {
           GwtReflectionUtils.findMethod(impl.getClass(), "fireHistoryChangedImpl", String.class).invoke(impl, token);
         } catch (Exception e) {
           throw new RuntimeException(e);
         }
      }

   }

   static GwtBrowserHistory BROWSER_HISTORY = new GwtBrowserHistory();

   @PatchMethod
   static String encodeFragment(Object impl, String fragment) {
      return URL.encodeQueryString(fragment).replace("#", "%23");
   }

   @PatchMethod
   static String getToken() {
      return BROWSER_HISTORY.getCurrentToken();
   }

   @PatchMethod
   static boolean init(Object historyImpl) {
      String hash = Window.Location.getHash();
      int index = hash.indexOf("#");
      if (index > -1) {
         String token = hash.substring(index + 1);
         GwtReflectionUtils.callPrivateMethod(historyImpl, "setToken", token);
         nativeUpdate(historyImpl, token);
      }
      return true;
   }

   @PatchMethod
   static void nativeUpdate(Object historyImpl, String historyToken) {
      if (historyToken == null) {
         historyToken = "";
      }

      BROWSER_HISTORY.addToken(historyToken);
   }

}

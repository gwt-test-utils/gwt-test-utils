package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.googlecode.gwt.test.internal.AfterTestCallback;
import com.googlecode.gwt.test.internal.AfterTestCallbackManager;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.patchers.PatchMethod.ParamType;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@PatchClass(target = HistoryImplPatcher.HISTORY_IMPL)
class HistoryImplPatcher {

    static final String HISTORY_IMPL = "com.google.gwt.user.client.History$HistoryImpl";

    static class GwtBrowserHistory implements AfterTestCallback {

        private int currentIndex;
        private final List<String> stack;

        GwtBrowserHistory() {
            this.stack = new ArrayList<>();
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

            Object historyImpl = GwtReflectionUtils.getStaticFieldValue(History.class, "historyEventSource");
            GwtReflectionUtils.callPrivateMethod(GwtReflectionUtils.getPrivateFieldValue(
                    GwtReflectionUtils.getPrivateFieldValue(
                            GwtReflectionUtils.getPrivateFieldValue(historyImpl, "handlers"),
                            "eventBus"), "map"), "clear");
            GwtReflectionUtils.setStaticField(History.class, "token", "");
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
            Object impl = GwtReflectionUtils.getStaticFieldValue(History.class, "historyEventSource");
            try {
                Method method = GwtReflectionUtils.findMethod(impl.getClass(), "fireValueChangedEvent", String.class);
                method.invoke(impl, token);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    static GwtBrowserHistory BROWSER_HISTORY = new GwtBrowserHistory();

    @PatchMethod
    static String encodeHistoryToken(@ParamType(HISTORY_IMPL) Object impl, String fragment) {
        return URL.encodeQueryString(fragment).replace("#", "%23");
    }

    @PatchMethod
    static void newToken(@ParamType(HISTORY_IMPL) Object impl, String token) {
        if (token == null) {
            token = "";
        }
        BROWSER_HISTORY.addToken(token);
    }

    @PatchMethod
    static String getToken() {
        return BROWSER_HISTORY.getCurrentToken();
    }

    @PatchMethod
    static boolean init(@ParamType(HISTORY_IMPL) Object historyImpl) {
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
    static void nativeUpdate(@ParamType(HISTORY_IMPL) Object historyImpl, String historyToken) {
        if (historyToken == null) {
            historyToken = "";
        }

        BROWSER_HISTORY.addToken(historyToken);
    }


    @PatchMethod
    static void attachListener(@ParamType(HISTORY_IMPL) Object historyImpl) {
    }

}

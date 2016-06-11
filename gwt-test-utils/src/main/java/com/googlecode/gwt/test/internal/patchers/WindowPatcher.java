package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;
import com.googlecode.gwt.test.WindowOperationsHandler;
import com.googlecode.gwt.test.exceptions.GwtTestConfigurationException;
import com.googlecode.gwt.test.internal.GwtConfig;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtDomUtils;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

@PatchClass(Window.class)
class WindowPatcher {

    @PatchMethod
    static void alert(String msg) {
        WindowOperationsHandler handler = GwtConfig.get().getModuleRunner().getWindowOperationsHandler();
        if (handler != null) {
            handler.alert(msg);
        }
    }

    @PatchMethod
    static boolean confirm(String msg) {
        WindowOperationsHandler handler = GwtConfig.get().getModuleRunner().getWindowOperationsHandler();
        if (handler == null) {
            throw new GwtTestConfigurationException(
                    "A call to Window.confirm(msg) was triggered, but no "
                            + WindowOperationsHandler.class.getSimpleName()
                            + " has been registered. You need to setup your own with the protected 'setWindowOperationsHandler' method available in your test class");

        }

        return handler.confirm(msg);
    }

    @PatchMethod
    static String getTitle() {
        return Document.get().getTitle();
    }

    @PatchMethod
    static void moveBy(int dx, int dy) {

    }

    @PatchMethod
    static void moveTo(int x, int y) {

    }

    @PatchMethod
    static void open(String url, String name, String features) {
        WindowOperationsHandler handler = GwtConfig.get().getModuleRunner().getWindowOperationsHandler();
        if (handler != null) {
            handler.open(url, name, features);
        }
    }

    @PatchMethod
    static void print() {
        WindowOperationsHandler handler = GwtConfig.get().getModuleRunner().getWindowOperationsHandler();
        if (handler != null) {
            handler.print();
        }
    }

    @PatchMethod
    static String prompt(String msg, String initialValue) {
        WindowOperationsHandler handler = GwtConfig.get().getModuleRunner().getWindowOperationsHandler();
        if (handler != null) {
            return handler.prompt(msg, initialValue);
        }

        return null;
    }

    @PatchMethod
    static void resizeBy(int width, int height) {
        Element viewportElement = GwtReflectionUtils.callPrivateMethod(Document.get(),
                "getViewportElement", "com.google.gwt.dom.client.Document");
        int currentWidth = Document.get().getClientWidth();
        GwtDomUtils.setClientWidth(viewportElement, currentWidth + width);

        int currentHeight = Document.get().getClientHeight();
        GwtDomUtils.setClientHeight(viewportElement, currentHeight + height);
    }

    @PatchMethod
    static void resizeTo(int width, int height) {
        Element viewportElement = GwtReflectionUtils.callPrivateMethod(Document.get(),
                "getViewportElement", "com.google.gwt.dom.client.Document");

        GwtDomUtils.setClientWidth(viewportElement, width);
        GwtDomUtils.setClientHeight(viewportElement, height);
    }

    @PatchMethod
    static void scrollTo(int left, int top) {

    }

    @PatchMethod
    static void setMargin(String size) {
        Document.get().getBody().getStyle().setProperty("margin", size);
    }

    @PatchMethod
    static void setStatus(String status) {

    }

    @PatchMethod
    static void setTitle(String title) {
        Document.get().setTitle(title);
    }

}

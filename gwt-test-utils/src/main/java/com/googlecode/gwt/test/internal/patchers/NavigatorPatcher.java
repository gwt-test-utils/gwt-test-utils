package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.user.client.Window.Navigator;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(Navigator.class)
class NavigatorPatcher {

    @PatchMethod
    static String getAppCodeName() {
        return "gwt-test-utils fake navigator-codeName";
    }

    @PatchMethod
    static String getAppName() {
        return "gwt-test-utils fake navigator";
    }

    @PatchMethod
    static String getAppVersion() {
        return "gwt-test-utils-1.0.0";
    }

    @PatchMethod
    static String getPlatform() {
        return "only the JVM @gwt-test-utils";
    }

    @PatchMethod
    static String getUserAgent() {
        return "gwt-test-utils fake navigator user-agent";
    }

    @PatchMethod
    static boolean isJavaEnabled() {
        return true;
    }

}

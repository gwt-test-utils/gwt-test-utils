package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.user.client.impl.WindowImpl;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(WindowImpl.class)
class WindowImplPatcher {

    @PatchMethod
    static String getHash(WindowImpl windowImpl) {
        String token = HistoryImplPatcher.BROWSER_HISTORY.getCurrentToken();

        return (token.length() > 0) ? "#" + token : token;
    }

    @PatchMethod
    static String getQueryString(WindowImpl windowImpl) {
        return "";
    }

    @PatchMethod
    static void initWindowCloseHandler(WindowImpl windowImpl) {

    }

    @PatchMethod
    static void initWindowResizeHandler(WindowImpl windowImpl) {

    }

    @PatchMethod
    static void initWindowScrollHandler(WindowImpl windowImpl) {

    }

}

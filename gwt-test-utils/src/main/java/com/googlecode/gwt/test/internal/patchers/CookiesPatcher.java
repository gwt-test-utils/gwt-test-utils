package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.user.client.Cookies;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import java.util.HashMap;
import java.util.Map;

@PatchClass(Cookies.class)
class CookiesPatcher {

    @PatchMethod
    static void loadCookies(HashMap<String, String> m) {
    }

    @PatchMethod
    static boolean needsRefresh() {
        return false;
    }

    @PatchMethod
    static void removeCookieNative(String name) {
        Map<String, String> cachedCookies = getCookiesMap();

        if (cachedCookies != null) {
            cachedCookies.remove(name);
        }
    }

    @PatchMethod
    static void setCookieImpl(String name, String value, double expires, String domain, String path,
                              boolean secure) {

        Map<String, String> cachedCookies = getCookiesMap();
        if (cachedCookies == null) {
            cachedCookies = new HashMap<>();
            GwtReflectionUtils.setStaticField(Cookies.class, "cachedCookies", cachedCookies);
        }

        cachedCookies.put(name, value);
    }

    @PatchMethod
    static String uriEncode(String s) {
        return s;
    }

    private static Map<String, String> getCookiesMap() {
        Map<String, String> cachedCookies = GwtReflectionUtils.getStaticFieldValue(Cookies.class,
                "cachedCookies");
        return cachedCookies;
    }

}

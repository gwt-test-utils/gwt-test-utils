package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.core.client.JavaScriptObject;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.JavaScriptObjects;

import java.util.HashMap;
import java.util.Map;

@PatchClass(target = "com.google.gwt.dom.builder.shared.HtmlStylesBuilder$FastStringMapClient")
class FastStringMapClientPatcher {

    private static final String INTERNAL_MAP = "FAST_STRING_INTERNAL_MAP";

    @PatchMethod
    static String getImpl(Object fastStringMap, JavaScriptObject map, String key) {
        return getInternalMap(map).get(key);
    }

    @PatchMethod
    static void putImpl(Object fastStringMap, JavaScriptObject map, String key, String value) {
        getInternalMap(map).put(key, value);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, String> getInternalMap(JavaScriptObject map) {
        Map<String, String> internalMap = JavaScriptObjects.getObject(map, INTERNAL_MAP);

        if (internalMap == null) {
            internalMap = new HashMap<>();
            JavaScriptObjects.setProperty(map, INTERNAL_MAP, internalMap);
        }

        return internalMap;
    }

}

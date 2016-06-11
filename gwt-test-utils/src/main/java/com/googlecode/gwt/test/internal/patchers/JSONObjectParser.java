package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.JavaScriptObjects;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@PatchClass(JSONObject.class)
class JSONObjectParser {

    private static final String JSONOBJECT_MAP = "JSONOBJECT_MAP";

    @PatchMethod
    static void addAllKeys(JSONObject jsonObject, Collection<String> s) {
        Map<String, JSONValue> map = getInnerMap(jsonObject);
        s.addAll(map.keySet());
    }

    @PatchMethod
    static boolean containsKey(JSONObject jsonObject, String key) {
        Map<String, JSONValue> map = getInnerMap(jsonObject);

        return map.containsKey(key);
    }

    @PatchMethod
    static JSONValue get0(JSONObject jsonObject, String key) {
        Map<String, JSONValue> map = getInnerMap(jsonObject);

        return map.get(key);

    }

    @PatchMethod
    static void put0(JSONObject jsonObject, String key, JSONValue value) {
        Map<String, JSONValue> map = getInnerMap(jsonObject);
        map.put(key, value);
    }

    private static Map<String, JSONValue> getInnerMap(JSONObject jsonObject) {
        JavaScriptObject jsObject = jsonObject.getJavaScriptObject();

        Map<String, JSONValue> map = JavaScriptObjects.getObject(jsObject, JSONOBJECT_MAP);
        if (map == null) {
            map = new LinkedHashMap<String, JSONValue>();
            JavaScriptObjects.setProperty(jsObject, JSONOBJECT_MAP, map);
        }

        return map;
    }
}

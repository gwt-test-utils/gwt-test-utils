package com.googlecode.gwt.test.internal.i18n;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.Dictionary;
import com.googlecode.gwt.test.internal.utils.JsoUtils;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import com.googlecode.gwt.test.utils.JavaScriptObjects;

import java.util.HashMap;
import java.util.Map;

/**
 * <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
public class DictionaryUtils {

    private static final String DICTIONARY_ENTRIES = "DICTIONARY_ENTRIES";

    public static void addEntries(Dictionary dictionary, Map<String, String> entries) {
        getEntries(dictionary).putAll(entries);
    }

    public static void attach(Dictionary dictionary, String name) {
        JavaScriptObject dict = JsoUtils.newNode(0);

        Map<String, String> entries = new HashMap<String, String>();
        JavaScriptObjects.setProperty(dict, DICTIONARY_ENTRIES, entries);

        GwtReflectionUtils.setPrivateFieldValue(dictionary, "dict", dict);
    }

    public static JavaScriptObject getDict(Dictionary dictionary) {
        return GwtReflectionUtils.getPrivateFieldValue(dictionary, "dict");
    }

    public static Map<String, String> getEntries(Dictionary dictionary) {
        return JavaScriptObjects.getObject(getDict(dictionary), DICTIONARY_ENTRIES);
    }

    private DictionaryUtils() {

    }

}

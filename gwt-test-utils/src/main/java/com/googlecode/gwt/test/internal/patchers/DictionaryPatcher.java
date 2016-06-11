package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.i18n.client.Dictionary;
import com.googlecode.gwt.test.internal.i18n.DictionaryUtils;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

@PatchClass(Dictionary.class)
class DictionaryPatcher {

    @PatchMethod
    static void addKeys(Dictionary dictionary, HashSet<String> s) {
        Map<String, String> entries = DictionaryUtils.getEntries(dictionary);

        s.addAll(entries.keySet());
    }

    @PatchMethod
    static void addValues(Dictionary dictionary, ArrayList<String> s) {
        Map<String, String> entries = DictionaryUtils.getEntries(dictionary);

        s.addAll(entries.values());
    }

    @PatchMethod
    static void attach(Dictionary dictionary, String name) {
        DictionaryUtils.attach(dictionary, name);
    }

    @PatchMethod
    static String get(Dictionary dictionary, String key) {
        Map<String, String> entries = DictionaryUtils.getEntries(dictionary);

        return entries.get(key);
    }

}

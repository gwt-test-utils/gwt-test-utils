package com.googlecode.gwt.test.internal.patchers;

import com.googlecode.gwt.test.internal.AfterTestCallback;
import com.googlecode.gwt.test.internal.AfterTestCallbackManager;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@PatchClass(target = "com.google.gwt.storage.client.StorageImpl")
class StorageImplPatcher {

    private static class StorageHolder implements AfterTestCallback {

        private static StorageHolder instance;

        private static StorageHolder get() {
            if (instance == null) {
                instance = new StorageHolder();
            }
            return instance;
        }

        private final Map<String, String> localStorage = new LinkedHashMap<String, String>();
        private final Map<String, String> sessionStorage = new LinkedHashMap<String, String>();

        StorageHolder() {
            AfterTestCallbackManager.get().registerCallback(this);
        }

        public void afterTest() throws Throwable {
            localStorage.clear();
            sessionStorage.clear();
        }

    }

    static final String LOCAL_STORAGE = "localStorage";
    static final String SESSION_STORAGE = "sessionStorage";

    @PatchMethod
    static void clear(Object storageImpl, String storage) {
        getStorage(storage).clear();
    }

    @PatchMethod
    static String getItem(Object storageImpl, String storage, String key) {
        return getStorage(storage).get(key);
    }

    @PatchMethod
    static int getLength(Object storageImpl, String storage) {
        return getStorage(storage).size();
    }

    @PatchMethod
    static String key(Object storageImpl, String storage, int index) {
        Set<String> keys = getStorage(storage).keySet();

        if (index < 0 || index >= keys.size()) {
            return null;
        }

        Iterator<String> it = keys.iterator();

        int i = 0;
        while (i++ < index) {
            it.next();
        }

        return it.next();
    }

    @PatchMethod
    static void removeItem(Object storageImpl, String storage, String key) {
        getStorage(storage).remove(key);
    }

    @PatchMethod
    static void setItem(Object storageImpl, String storage, String key, String data) {
        getStorage(storage).put(key, data);
    }

    private static Map<String, String> getStorage(String storage) {
        if (LOCAL_STORAGE.equals(storage)) {
            return StorageHolder.get().localStorage;
        }
        return StorageHolder.get().sessionStorage;
    }

}

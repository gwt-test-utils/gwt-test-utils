package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.storage.client.Storage;
import com.googlecode.gwt.test.internal.GwtConfig;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(Storage.class)
class StoragePatcher {

    @PatchMethod
    static boolean isLocalStorageSupported() {
        return GwtConfig.get().getModuleRunner().isLocalStorageSupported();
    }

    @PatchMethod
    static boolean isSessionStorageSupported() {
        return GwtConfig.get().getModuleRunner().isSessionStorageSupported();
    }

}

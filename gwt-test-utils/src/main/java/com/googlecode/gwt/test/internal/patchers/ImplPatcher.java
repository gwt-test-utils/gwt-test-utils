package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.core.client.impl.Impl;
import com.googlecode.gwt.test.internal.GwtConfig;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@PatchClass(Impl.class)
class ImplPatcher {

    @PatchMethod
    static int getHashCode(Object o) {
        return HashCodeBuilder.reflectionHashCode(o);
    }

    @PatchMethod
    static String getHostPageBaseURL() {
        return "http://127.0.0.1:8888/";
    }

    @PatchMethod
    static String getModuleBaseURL() {
        return getHostPageBaseURL() + getModuleName() + "/";
    }

    @PatchMethod
    static String getModuleName() {
        return GwtConfig.get().getModuleAlias();
    }

}

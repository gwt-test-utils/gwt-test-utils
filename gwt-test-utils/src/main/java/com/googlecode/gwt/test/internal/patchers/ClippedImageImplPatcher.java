package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.impl.ClippedImageImpl;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(ClippedImageImpl.class)
public class ClippedImageImplPatcher {

    @PatchMethod
    public static JavaScriptObject createOnLoadHandlerFunction() {
        return null;
    }
}

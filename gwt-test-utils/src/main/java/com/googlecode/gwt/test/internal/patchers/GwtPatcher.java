package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.googlecode.gwt.test.internal.handlers.GwtTestGWTBridge;
import com.googlecode.gwt.test.patchers.InitMethod;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;

@PatchClass(GWT.class)
class GwtPatcher {

    @InitMethod
    static void init(CtClass ctClass) throws CannotCompileException {

        CtConstructor staticInitializer = ctClass.makeClassInitializer();

        staticInitializer.insertAfter("setBridge(" + GwtTestGWTBridge.class.getName() + ".get());");
    }

    @PatchMethod
    static void runAsyncImpl(final RunAsyncCallback callback) {
        callback.onSuccess();
    }

}

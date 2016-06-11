package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.xml.client.ProcessingInstruction;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

@PatchClass(target = "com.google.gwt.xml.client.impl.ProcessingInstructionImpl")
class ProcessingInstructionImplPatcher {

    @PatchMethod
    static String toString(ProcessingInstruction processingInstruction) {
        JavaScriptObject jso = GwtReflectionUtils.getPrivateFieldValue(processingInstruction,
                "jsObject");
        return jso.toString();
    }

}

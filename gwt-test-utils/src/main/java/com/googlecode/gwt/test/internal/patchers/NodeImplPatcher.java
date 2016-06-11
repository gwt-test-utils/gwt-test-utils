package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.xml.client.Node;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

@PatchClass(target = "com.google.gwt.xml.client.impl.NodeImpl")
class NodeImplPatcher {

    @PatchMethod
    static String toString(Node nodeImpl) {
        JavaScriptObject jso = GwtReflectionUtils.getPrivateFieldValue(nodeImpl, "jsObject");
        return jso.toString();
    }

}

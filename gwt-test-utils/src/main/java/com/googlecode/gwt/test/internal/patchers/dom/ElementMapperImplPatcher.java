package com.googlecode.gwt.test.internal.patchers.dom;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.impl.ElementMapperImpl;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.JavaScriptObjects;

@PatchClass(ElementMapperImpl.class)
class ElementMapperImplPatcher {

    private static final String widgetId = "__uiObjectID";

    @PatchMethod
    static void clearIndex(Element e) {
        JavaScriptObjects.setProperty(e, widgetId, null);
        e.setPropertyString(widgetId, null);
    }

    @PatchMethod
    static int getIndex(Element e) {
        String index = JavaScriptObjects.getObject(e, widgetId);
        return index == null ? -1 : Integer.parseInt(index);
    }

    @PatchMethod
    static void setIndex(Element e, int index) {
        JavaScriptObjects.setProperty(e, widgetId, Integer.toString(index));
    }

}

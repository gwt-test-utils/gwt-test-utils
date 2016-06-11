package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.impl.TextBoxImpl;
import com.googlecode.gwt.test.internal.utils.JsoProperties;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.JavaScriptObjects;

@PatchClass(TextBoxImpl.class)
class TextBoxImplPatcher {

    @PatchMethod
    static int getCursorPos(TextBoxImpl textBoxImpl, Element e) {
        return JavaScriptObjects.getInteger(e, JsoProperties.SELECTION_START);
    }

    @PatchMethod
    static int getSelectionLength(TextBoxImpl textBoxImpl, Element e) {
        int selectionStart = JavaScriptObjects.getInteger(e, JsoProperties.SELECTION_START);
        int selectionEnd = JavaScriptObjects.getInteger(e, JsoProperties.SELECTION_END);
        return selectionEnd - selectionStart;
    }

    @PatchMethod
    static void setSelectionRange(TextBoxImpl textBoxImpl, Element e, int pos, int length) {
        JavaScriptObjects.setProperty(e, JsoProperties.SELECTION_START, pos);
        JavaScriptObjects.setProperty(e, JsoProperties.SELECTION_END, pos + length);
    }

}

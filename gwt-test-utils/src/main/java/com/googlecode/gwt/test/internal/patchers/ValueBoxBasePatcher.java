package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.AutoDirectionHandler;
import com.google.gwt.text.shared.Parser;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.googlecode.gwt.test.patchers.InitMethod;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import com.googlecode.gwt.test.utils.JavassistUtils;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;

@PatchClass(ValueBoxBase.class)
class ValueBoxBasePatcher {

    @InitMethod
    static void initClass(CtClass c) throws CannotCompileException {
        CtConstructor cons = JavassistUtils.findConstructor(c, Element.class, Renderer.class,
                Parser.class);
        cons.insertAfter("setText(\"\");");
    }

    @PatchMethod
    static void setText(ValueBoxBase<?> valueBoxBase, String text) {
        DOM.setElementProperty(valueBoxBase.getElement(), "value", text != null ? text : "");
        AutoDirectionHandler autoDirHandler = GwtReflectionUtils.getPrivateFieldValue(valueBoxBase,
                "autoDirHandler");
        autoDirHandler.refreshDirection();

        int cursorPos = text != null ? text.length() : 0;

        valueBoxBase.setCursorPos(cursorPos);
    }

}

package com.googlecode.gwt.test.internal.patchers.dom;

import com.google.gwt.event.dom.client.MouseEvent;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(MouseEvent.class)
class MouseEventPatcher {

    @PatchMethod
    static int getX(MouseEvent<?> event) {
        return event.getClientX();
    }

    @PatchMethod
    static int getY(MouseEvent<?> event) {
        return event.getClientY();
    }

}

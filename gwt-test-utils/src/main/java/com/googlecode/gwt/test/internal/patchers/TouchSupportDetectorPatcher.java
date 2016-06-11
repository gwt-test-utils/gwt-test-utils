package com.googlecode.gwt.test.internal.patchers;

import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(target = "com.google.gwt.event.dom.client.TouchEvent$TouchSupportDetector")
class TouchSupportDetectorPatcher {

    @PatchMethod
    static boolean detectTouchSupport(Object touchSupportDetector) {
        return false;
    }

}

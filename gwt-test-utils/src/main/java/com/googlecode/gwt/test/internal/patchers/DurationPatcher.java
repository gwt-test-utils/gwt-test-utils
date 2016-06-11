package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.core.client.Duration;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(Duration.class)
class DurationPatcher {

    @PatchMethod
    static double currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @PatchMethod
    static int uncheckedConversion(double elapsed) {
        return (int) elapsed;
    }

}

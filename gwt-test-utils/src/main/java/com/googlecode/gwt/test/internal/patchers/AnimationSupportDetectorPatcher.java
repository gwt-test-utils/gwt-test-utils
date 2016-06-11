package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.animation.client.AnimationScheduler.AnimationSupportDetector;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(AnimationSupportDetector.class)
public class AnimationSupportDetectorPatcher {

    @PatchMethod
    static boolean isNativelySupported(AnimationSupportDetector detector) {
        return false;
    }

}

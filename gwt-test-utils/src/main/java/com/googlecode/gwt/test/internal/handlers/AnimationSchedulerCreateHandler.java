package com.googlecode.gwt.test.internal.handlers;

import com.google.gwt.animation.client.AnimationScheduler;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

class AnimationSchedulerCreateHandler implements GwtCreateHandler {

    public Object create(Class<?> classLiteral) throws Exception {
        if (!AnimationScheduler.class.equals(classLiteral)) {
            return null;
        }

        Class<?> implClass = Class.forName("com.google.gwt.animation.client.AnimationSchedulerImplTimer");
        return GwtReflectionUtils.instantiateClass(implClass);
    }

}

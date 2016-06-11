package com.googlecode.gwt.test.internal.handlers;

import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.googlecode.gwt.test.GwtCreateHandler;

public class AutoBeanCreateHandler implements GwtCreateHandler {

    @SuppressWarnings("unchecked")
    public Object create(Class<?> classLiteral) throws Exception {
        if (AutoBeanFactory.class.isAssignableFrom(classLiteral)) {
            return AutoBeanFactorySource.create((Class<? extends AutoBeanFactory>) classLiteral);
        }

        return null;
    }
}

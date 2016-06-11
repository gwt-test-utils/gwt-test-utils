package com.googlecode.gwt.test.internal.handlers;

import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

/**
 * GwtCreateHandler for ResizeLayoutPanel$Impl.
 *
 * @author Gael Lazzari
 */
class ResizeLayoutPanelImplCreateHandler implements GwtCreateHandler {

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.gwt.test.GwtCreateHandler#create(java.lang.Class)
     */
    public Object create(Class<?> classLiteral) throws Exception {
        if (!"com.google.gwt.user.client.ui.ResizeLayoutPanel$Impl".equals(classLiteral.getName())) {
            return null;
        }

        Class<?> clazz = Class.forName("com.google.gwt.user.client.ui.ResizeLayoutPanel$ImplStandard");
        return GwtReflectionUtils.instantiateClass(clazz);
    }

}

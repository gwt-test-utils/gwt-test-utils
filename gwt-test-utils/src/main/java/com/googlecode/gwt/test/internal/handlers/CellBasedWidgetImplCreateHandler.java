package com.googlecode.gwt.test.internal.handlers;

import com.google.gwt.user.cellview.client.CellBasedWidgetImplStandardBase;
import com.googlecode.gwt.test.GwtCreateHandler;

/**
 * GwtCreateHandler for CellBasedWidgetImpl.
 *
 * @author Gael Lazzari
 */
class CellBasedWidgetImplCreateHandler implements GwtCreateHandler {

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.gwt.test.GwtCreateHandler#create(java.lang.Class)
     */
    public Object create(Class<?> classLiteral) {
        if ("com.google.gwt.user.cellview.client.CellBasedWidgetImpl".equals(classLiteral.getName())) {
            return new CellBasedWidgetImplStandardBase();
        } else {
            return null;
        }
    }

}

package com.googlecode.gwt.test.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class SimpleJS extends JavaScriptObject {

    protected SimpleJS() {
    }

    public final native String getString() /*-{
        return this.string;
    }-*/;
}

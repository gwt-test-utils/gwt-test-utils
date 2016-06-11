package com.googlecode.gwt.test.uibinder;

import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

public class UiConstructorWidget extends Composite {

    public static enum AnotherType {
        T1, T2
    }

    public static enum Type {
        T1, T2;
    }

    AnotherType anotherType;
    final int size;

    final Type type;

    @UiConstructor
    public UiConstructorWidget(int size, Type type) {
        this.size = size;
        this.type = type;

        initWidget(new HTML(String.valueOf(this.size) + ": " + this.type.toString()));
    }

    public void setAnotherType(AnotherType anotherType) {
        this.anotherType = anotherType;
    }

}

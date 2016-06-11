package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class UiBinderWithDom extends UIObject { // Could extend Widget instead
    interface MyUiBinder extends UiBinder<DivElement, UiBinderWithDom> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    SpanElement nameSpan;

    public UiBinderWithDom() {
        // createAndBindUi initializes this.nameSpan
        setElement(uiBinder.createAndBindUi(this));
    }

    public void setName(String name) {
        nameSpan.setInnerText(name);
    }
}

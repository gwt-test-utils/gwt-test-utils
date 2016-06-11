package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;

public class UiBinderWithUiChild extends Composite {

    interface MyUiBinder extends UiBinder<WidgetWithUiChild, UiBinderWithUiChild> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    public UiBinderWithUiChild() {
        this.initWidget(uiBinder.createAndBindUi(this));
    }

    public WidgetWithUiChild getWidgetWithUiChild() {
        return (WidgetWithUiChild) getWidget();
    }

}

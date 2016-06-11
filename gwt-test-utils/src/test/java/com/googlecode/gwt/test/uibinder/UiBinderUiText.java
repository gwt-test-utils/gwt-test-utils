package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

public class UiBinderUiText extends Composite {

    interface MyUiBinder extends UiBinder<HTMLPanel, UiBinderUiText> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    Label label;

    @UiField
    Label msgLabel;

    public UiBinderUiText() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}

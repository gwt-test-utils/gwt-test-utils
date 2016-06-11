package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackLayoutPanel;

public class UiBinderWithStackLayoutPanel extends Composite {

    interface MyUiBinder extends UiBinder<StackLayoutPanel, UiBinderWithStackLayoutPanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    Label customHeader;

    @UiField
    Label first;

    @UiField
    Label second;

    public UiBinderWithStackLayoutPanel() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public StackLayoutPanel getPanel() {
        return (StackLayoutPanel) this.getWidget();
    }

}

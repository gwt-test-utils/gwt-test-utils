package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;

public class UiBinderWithLayoutPanel extends Composite {

    interface MyUiBinder extends UiBinder<LayoutPanel, UiBinderWithLayoutPanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    Label defaultLabel;

    @UiField
    Label headerLabel;

    public UiBinderWithLayoutPanel() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public LayoutPanel getPanel() {
        return (LayoutPanel) this.getWidget();
    }

}

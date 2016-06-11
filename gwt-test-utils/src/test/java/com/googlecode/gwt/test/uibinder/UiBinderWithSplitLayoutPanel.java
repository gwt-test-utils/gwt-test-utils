package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

public class UiBinderWithSplitLayoutPanel extends Composite {

    interface UiBinderWithSplitLayoutPanelUiBinder extends
            UiBinder<SplitLayoutPanel, UiBinderWithSplitLayoutPanel> {
    }

    private static UiBinderWithSplitLayoutPanelUiBinder uiBinder = GWT.create(UiBinderWithSplitLayoutPanelUiBinder.class);

    @UiField
    Label centerLabel;

    @UiField
    Label eastLabel;

    @UiField
    Label northLabel;

    @UiField
    Label southLabel;

    @UiField
    HTML westHTML;

    public UiBinderWithSplitLayoutPanel() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public SplitLayoutPanel getLayout() {
        return (SplitLayoutPanel) getWidget();
    }

}

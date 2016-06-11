package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

public class UiBinderWithDockLayoutPanel extends Composite {

    interface UiBinderWithDocLayoutPanelUiBinder extends
            UiBinder<DockLayoutPanel, UiBinderWithDockLayoutPanel> {
    }

    private static UiBinderWithDocLayoutPanelUiBinder uiBinder = GWT.create(UiBinderWithDocLayoutPanelUiBinder.class);

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

    public UiBinderWithDockLayoutPanel() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public DockLayoutPanel getLayout() {
        return (DockLayoutPanel) getWidget();
    }

}

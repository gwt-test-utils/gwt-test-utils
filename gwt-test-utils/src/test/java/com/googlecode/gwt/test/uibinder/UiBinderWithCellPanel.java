package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

public class UiBinderWithCellPanel extends Composite {

    interface UiBinderCellPanelUiBinder extends UiBinder<CellPanel, UiBinderWithCellPanel> {
    }

    private static UiBinderCellPanelUiBinder uiBinder = GWT.create(UiBinderCellPanelUiBinder.class);

    @UiField
    Label leftSide;

    @UiField
    Label rightSide;

    public UiBinderWithCellPanel() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public CellPanel getPanel() {
        return (CellPanel) getWidget();
    }

}

package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;

public class UiBinderWithDisclosurePanel extends Composite {

    interface UiBinderDisclosurePanelUiBinder extends
            UiBinder<HTMLPanel, UiBinderWithDisclosurePanel> {
    }

    private static UiBinderDisclosurePanelUiBinder uiBinder = GWT.create(UiBinderDisclosurePanelUiBinder.class);

    @UiField
    DisclosurePanel disclosurePanelWithCustomHeader;

    @UiField
    DisclosurePanel disclosurePanelWithTextHeader;

    public UiBinderWithDisclosurePanel() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}

package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

public class UiBinderWithImports extends Composite {

    @UiTemplate("UiBinderWithImports-UiTemplate.ui.xml")
    interface MyUiBinder extends UiBinder<HTMLPanel, UiBinderWithImports> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    Label enumImport;

    @UiField
    Label multipleConstantsImport;

    @UiField
    DateLabel myDateLabel;

    @UiField
    DateLabel myDateLabel2;

    @UiField
    DateLabel myDateLabel3;

    @UiField
    Label singleConstantImport;

    public UiBinderWithImports() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}

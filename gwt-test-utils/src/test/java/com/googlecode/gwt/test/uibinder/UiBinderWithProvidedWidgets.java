package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class UiBinderWithProvidedWidgets extends Composite {

    interface MyUiBinder extends UiBinder<HTMLPanel, UiBinderWithProvidedWidgets> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField(provided = true)
    ProvidedLabel firstProvidedLabel;

    @UiField(provided = true)
    ProvidedLabel secondProvidedLabel;

    public UiBinderWithProvidedWidgets() {
        firstProvidedLabel = new ProvidedLabel("first provided string");
        secondProvidedLabel = new ProvidedLabel("second provided string");

        initWidget(uiBinder.createAndBindUi(this));
    }

}

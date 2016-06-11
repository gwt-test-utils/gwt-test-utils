package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;

public class UiBinderWithListBox extends Composite {

    private static interface MyListBoxUiBinder extends UiBinder<ListBox, UiBinderWithListBox> {
    }

    private static MyListBoxUiBinder uiBinder = GWT.create(MyListBoxUiBinder.class);

    @UiField
    ListBox listBox;

    public UiBinderWithListBox() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}

package com.googlecode.gwt.test.editors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class PersonEditor extends Composite implements Editor<Person> {

    interface Binder extends UiBinder<Widget, PersonEditor> {
    }

    interface PersonDriver extends SimpleBeanEditorDriver<Person, PersonEditor> {
    }

    private static final Binder BINDER = GWT.create(Binder.class);

    @UiField
    AddressEditor address;
    @UiField
    TextBox firstName;
    @UiField
    TextBox lastName;

    public PersonEditor() {
        initWidget(BINDER.createAndBindUi(this));
    }
}
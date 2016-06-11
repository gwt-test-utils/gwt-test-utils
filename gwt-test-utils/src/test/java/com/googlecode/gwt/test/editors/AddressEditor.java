package com.googlecode.gwt.test.editors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.Arrays;
import java.util.Collection;

public class AddressEditor extends Composite implements Editor<Address> {

    interface AddressDriver extends SimpleBeanEditorDriver<Address, AddressEditor> {
    }

    interface Binder extends UiBinder<Widget, AddressEditor> {
    }

    private static final Binder BINDER = GWT.create(Binder.class);

    @UiField
    TextBox city;

    @UiField(provided = true)
    @Path("state")
    ValueListBox<String> stateWithPath;

    @UiField
    private TextBox street;

    @UiField
    private TextBox zipWithPath;

    public AddressEditor() {

        stateWithPath = new ValueListBox<String>(new AbstractRenderer<String>() {

            public String render(String object) {
                return object;
            }

        });
        stateWithPath.setAcceptableValues(buildCollection("Austria", "France", "Germany"));

        initWidget(BINDER.createAndBindUi(this));

    }

    public TextBox street() {
        return street;
    }

    @Path("zip")
    TextBox zipWithPathEditor() {
        return zipWithPath;
    }

    private Collection<String> buildCollection(String... string) {
        return Arrays.asList(string);
    }
}

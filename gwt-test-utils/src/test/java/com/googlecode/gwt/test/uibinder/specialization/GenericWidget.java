package com.googlecode.gwt.test.uibinder.specialization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

class GenericWidget<T> extends Composite {

    @SuppressWarnings("rawtypes")
    interface GenericWidgetContainerUiBinder extends UiBinder<Widget, GenericWidget> {
    }

    static class ItemWidget extends GenericWidget<Item> {

        public ItemWidget(final Item object) {
            super(object);
        }
    }

    static class PersonWidget extends GenericWidget<Person> {

        public PersonWidget(final Person object) {
            super(object);
        }
    }

    private static GenericWidgetContainerUiBinder uiBinder = GWT.create(GenericWidgetContainerUiBinder.class);

    @UiField
    Label genericLabel;

    public GenericWidget(final T object) {
        initWidget(uiBinder.createAndBindUi(this));
        genericLabel.setText(object.toString());
    }
}

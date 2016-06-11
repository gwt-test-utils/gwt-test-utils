package com.googlecode.gwt.test.uibinder.specialization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.googlecode.gwt.test.uibinder.specialization.GenericWidget.ItemWidget;
import com.googlecode.gwt.test.uibinder.specialization.GenericWidget.PersonWidget;

class UiBinderWithMoreThanOneUiBinderFactoriesForSameType extends Composite {

    interface MyUiBinder extends
            UiBinder<HTMLPanel, UiBinderWithMoreThanOneUiBinderFactoriesForSameType> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    public GenericWidget<Item> itemWidget;

    @UiField
    public GenericWidget<Person> personWidget;

    public UiBinderWithMoreThanOneUiBinderFactoriesForSameType() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiFactory
    protected ItemWidget createItemWidget() {
        return new ItemWidget(new Item("item created by @UiFactory"));
    }

    @UiFactory
    protected PersonWidget createPersonWidget() {
        return new PersonWidget(new Person("person created by @UiFactory"));
    }
}

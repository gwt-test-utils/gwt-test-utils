package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

public class UiBinderWithMenuBar extends Composite {

    interface MyUiBinder extends UiBinder<MenuBar, UiBinderWithMenuBar> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    MenuBar menu1;

    @UiField
    MenuBar menu2;

    @UiField
    MenuBar menu3;

    @UiField
    MenuItem menuItem1;

    @UiField
    MenuItem menuItem2;

    @UiField
    MenuItem menuItem3;

    @UiField
    MenuItem subMenuItem1;

    @UiField
    MenuItem subMenuItem2;

    @UiField
    MenuItem subMenuItem3;

    public UiBinderWithMenuBar() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public MenuBar getMenu() {
        return (MenuBar) this.getWidget();
    }

}

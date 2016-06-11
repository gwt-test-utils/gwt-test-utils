package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class UiBinderWithGrid extends Composite {

    private static interface MyGridUiBinder extends UiBinder<Grid, UiBinderWithGrid> {
    }

    private static MyGridUiBinder uiBinder = GWT.create(MyGridUiBinder.class);

    @UiField
    DivElement myDiv;

    @UiField
    HTML myHTML;

    @UiField
    Label myLabel;

    @UiField
    SpanElement mySpan;

    public UiBinderWithGrid() {
        initWidget(uiBinder.createAndBindUi(this));

        getGrid().getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
    }

    public Grid getGrid() {
        return (Grid) getWidget();
    }

}

package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.RowCountChangeEvent;

public class UiBinderWithCellWidget extends Composite {

    interface UiBinderWithCellWidgetUiBinder extends UiBinder<Widget, UiBinderWithCellWidget> {
    }

    private static UiBinderWithCellWidgetUiBinder uiBinder = GWT.create(UiBinderWithCellWidgetUiBinder.class);

    int rowCount;

    @UiField
    CellTable<String> table;

    public UiBinderWithCellWidget() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("table")
    void onRowCountChange(RowCountChangeEvent evt) {
        this.rowCount = evt.getNewRowCount();
    }

}

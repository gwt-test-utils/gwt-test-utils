package com.googlecode.gwt.test.uibinder;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class IsWidgetLabel implements IsWidget {

    private final Label wrapped;

    public IsWidgetLabel() {
        wrapped = new Label("isWidget Label");
    }

    public Widget asWidget() {
        return wrapped;
    }

    public String getText() {
        return ((Label) asWidget()).getText();
    }

}

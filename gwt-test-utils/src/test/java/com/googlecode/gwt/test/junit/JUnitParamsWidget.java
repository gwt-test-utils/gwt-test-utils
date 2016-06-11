package com.googlecode.gwt.test.junit;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;

class JUnitParamsWidget extends Composite implements HasText {

    JUnitParamsWidget() {
        initWidget(new FlowPanel());
    }

    public String getText() {
        return this.getWidget().getElement().getInnerText();
    }

    public void setText(String text) {
        this.getWidget().getElement().setInnerText("text : " + text);
    }

}

package com.googlecode.gwt.test.csv.data;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class MyComposite extends Composite {

    private Button compositeButton;
    private Label compositeLabel;

    public MyComposite(String labelText, String buttonHTML) {
        FlowPanel mainPanel = new FlowPanel();
        compositeLabel = new Label(labelText);
        compositeButton = new Button(buttonHTML);
        mainPanel.add(compositeLabel);
        mainPanel.add(compositeButton);

        initWidget(mainPanel);
    }

}

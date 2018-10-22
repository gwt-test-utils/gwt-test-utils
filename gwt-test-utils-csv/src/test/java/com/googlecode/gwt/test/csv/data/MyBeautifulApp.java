package com.googlecode.gwt.test.csv.data;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class MyBeautifulApp implements EntryPoint {

    private Button b1;

    private Button b2;

    private Button b3;

    private Button b4;

    private Button b5;

    private Label historyLabel;

    private TextBox invisibleTB;

    private Label l;

    private ListBox lb;

    private MyComposite myComposite;

    private TextBox t;

    public void onModuleLoad() {
        FlowPanel panel = new FlowPanel();
        b1 = new Button("Button1's HTML");
        b1.getElement().setId("button-1");
        panel.add(b1);

        b2 = new Button("Button2's HTML");
        b2.getElement().setId("button-2");
        panel.add(b2);

        l = new Label();
        l.setText("init");
        panel.add(l);

        t = new TextBox();
        panel.add(t);

        historyLabel = new Label();
        panel.add(historyLabel);

        invisibleTB = new TextBox();
        invisibleTB.setVisible(false);
        panel.add(invisibleTB);

        lb = new ListBox();
        lb.addItem("lbText0");
        lb.addItem("lbText1");
        lb.addItem("lbText2");
        panel.add(lb);

        RootPanel.get().add(panel);

        b1.addClickHandler(event -> l.setText("click on b1"));
        b2.addClickHandler(event -> l.setText("click on b2"));
        b3 = new Button("Button3's HTML");
        panel.add(b3);
        b3.addClickHandler(event -> {
            MyRemoteServiceAsync remoteServiceAsync = GWT.create(MyRemoteService.class);
            remoteServiceAsync.myMethod(l.getText(), new AsyncCallback<String>() {

                public void onFailure(Throwable arg0) {
                    l.setText("error");
                }

                public void onSuccess(String arg0) {
                    l.setText(arg0);
                }

            });
        });

        b4 = new Button("Button4's HTML");
        panel.add(b4);

        b4.addClickHandler(event -> {
            MyRemoteServiceAsync remoteServiceAsync = GWT.create(MyRemoteService.class);
            remoteServiceAsync.myMethod2(new MyCustomObject("toto"),
                    new AsyncCallback<MyCustomObject>() {

                        public void onFailure(Throwable t) {
                            throw new RuntimeException(t);
                        }

                        public void onSuccess(MyCustomObject object) {
                            l.setText(object.myField + " " + object.myTransientField);
                        }

                    });
        });

        b5 = new Button("Button5's HTML");
        panel.add(b5);

        b5.addClickHandler(event -> {
            MyRemoteServiceAsync remoteServiceAsync = GWT.create(MyRemoteService.class);
            remoteServiceAsync.myMethod3(new AsyncCallback<Void>() {

                public void onFailure(Throwable arg0) {
                    l.setText("error");
                }

                public void onSuccess(Void arg0) {
                    l.setText("success");
                }

            });
        });

        t.addChangeHandler(event -> historyLabel.setText("t was filled with value \"" + t.getText() + "\""));

        invisibleTB.addChangeHandler(event -> historyLabel.setText("invisibleTB was filled with value \"" + invisibleTB.getText()
                + "\""));

        myComposite = new MyComposite("myComposite Label", "MyComposite Button");
        panel.add(myComposite);
    }

}

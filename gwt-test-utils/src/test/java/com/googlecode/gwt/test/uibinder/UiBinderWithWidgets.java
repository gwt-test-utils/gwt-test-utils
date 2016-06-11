package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

public class UiBinderWithWidgets extends Composite {

    interface MyStyle extends CssResource {
        String disabled();

        String enabled();
    }

    interface MyUiBinder extends UiBinder<HTMLPanel, UiBinderWithWidgets> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    Button button;

    @UiField
    DataResource data;

    @UiField
    Image image;

    @UiField
    Image imageWithUrl;

    @UiField
    ImageResource img;

    @UiField
    IsWidgetLabel isWidgetLabel;

    @UiField
    ListBox listBox;

    @UiField
    Label msgInnerWidget;

    @UiField
    Label msgLabel;

    @UiField(provided = true)
    ProvidedLabel providedLabel;

    @UiField
    PushButton pushButton;

    @UiField
    RadioButton radioButton1;

    @UiField
    RadioButton radioButton2;

    @UiField
    MyStyle style;

    @UiField
    TextBox textBox;

    @UiField
    UiConstructorLabel uiConstructorLabel;

    @UiField
    UiFactoryLabel uiFactoryLabel;

    @UiField
    VerticalPanel verticalPanel;

    private final String uiFactoryLabelText;

    public UiBinderWithWidgets(String... names) {
        providedLabel = new ProvidedLabel("my provided string");
        this.uiFactoryLabelText = names[0];

        initWidget(uiBinder.createAndBindUi(this));
        for (String name : names) {
            listBox.addItem(name);
        }

        providedLabel.setStyleName(style.disabled());
    }

    @UiHandler("textBox")
    public void textBox_onValueChange(ValueChangeEvent<String> event) {
        msgLabel.setText(event.getValue());
    }

    @UiHandler("button")
    void handleClick(ClickEvent e) {
        listBox.setVisibleItemCount(2);
    }

    /**
     * Used by MyUiBinder to instantiate UiFactoryLabel
     */
    @UiFactory
    UiFactoryLabel makeUiFactoryLabel() { // method name is insignificant
        return new UiFactoryLabel(uiFactoryLabelText);
    }

}

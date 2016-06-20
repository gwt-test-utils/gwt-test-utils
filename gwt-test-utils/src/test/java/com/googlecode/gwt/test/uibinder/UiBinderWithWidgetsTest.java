package com.googlecode.gwt.test.uibinder;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.resources.MyClientBundle;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import com.googlecode.gwt.test.utils.events.Browser;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UiBinderWithWidgetsTest extends GwtTestTest {

    @Test
    public void click_UiHandler() {
        // Given
        UiBinderWithWidgets w = new UiBinderWithWidgets("gael", "eric");

        // Preconditions
        assertThat(w.listBox.getVisibleItemCount()).isEqualTo(1);

        // When
        Browser.click(w.button);

        // Then
        assertThat(w.listBox.getVisibleItemCount()).isEqualTo(2);
    }

    @Test
    public void fill_TextBox() {
        // Given
        UiBinderWithWidgets w = new UiBinderWithWidgets("gael", "eric");

        // Preconditions
        assertThat(w.msgLabel.getText()).isEqualTo("We strongly urge you to reconsider.");

        // When
        Browser.fillText(w.textBox, "ValueChangeHandler has been triggered !");

        // Then
        assertThat(w.msgLabel.getText()).isEqualTo("ValueChangeHandler has been triggered !");
    }

    @Test
    public void uiObjectTag() {
        // Given
        UiBinderWithWidgets w = new UiBinderWithWidgets("gael", "eric");

        // When
        RootPanel.get().add(w);

        // Then
        assertThat(w.listBox.getVisibleItemCount()).isEqualTo(1);

        HTMLPanel wrappedPanel = GwtReflectionUtils.callPrivateMethod(w, "getWidget");
        assertThat(wrappedPanel.getWidget(0)).isEqualTo(w.listBox);

        assertThat(w.radioButton1.getText()).isEqualTo("Test radio 1");
        assertThat(w.radioButton1.getName()).isEqualTo("MyRadioGroup");
        assertThat(w.radioButton1.getValue()).isTrue();

        assertThat(w.radioButton2.getText()).isEqualTo("Test radio 2");
        assertThat(w.radioButton2.getName()).isEqualTo("MyRadioGroup");
        assertThat(w.radioButton2.getValue()).isFalse();

        assertThat(w.image.getUrl()).isEqualTo(MyClientBundle.INSTANCE.cellTableLoading().getSafeUri().asString());
        assertThat(w.image.getAltText()).isEqualTo("Loading...");
        assertThat(w.image.getStyleName()).isEqualTo("pretty");

        assertThat(w.imageWithUrl.getUrl()).isEqualTo("http://slazzer.com/image.jpg");

        assertThat(w.providedLabel.getText()).isEqualTo("my provided label");
        assertThat(w.providedLabel.getCustomText()).isEqualTo("custom text setup in ui.xml");
        assertThat(w.providedLabel.providedString).isEqualTo("my provided string");
        assertThat(w.providedLabel.getStyleName()).isEqualTo("disabled");

        assertThat(w.uiFactoryLabel.getText()).isEqualTo("my UiFactory label");
        assertThat(w.uiFactoryLabel.getCustomText()).isEqualTo("custom text setup in ui.xml");
        assertThat(w.uiFactoryLabel.uiFactoryString).isEqualTo("gael");

        assertThat(w.uiConstructorLabel.getText()).isEqualTo("my UiConstructor label");
        assertThat(w.uiConstructorLabel.getCustomText()).isEqualTo("custom text setup in ui.xml");
        assertThat(w.uiConstructorLabel.uiConstructorLabel).isEqualTo("uiConstructor property");

        Label label = (Label) wrappedPanel.getWidget(1);
        assertThat(label).isNotNull();
        assertThat(label.getText()).isEqualTo("Keep your ducks");

        SpanElement spanElement = Document.get().getElementById("mySpan").cast();
        assertThat(spanElement.getInnerText()).isEqualTo("some span for testing");
        assertThat(spanElement.getClassName()).isEqualTo("pretty");

        assertThat(w.msgLabel.getText()).isEqualTo("We strongly urge you to reconsider.");

        assertThat(w.msgInnerWidget.getText()).isEqualTo("9'00");
        assertThat(wrappedPanel.getWidget(5)).isEqualTo(w.msgInnerWidget);

        assertThat(w.verticalPanel.getHorizontalAlignment()).isEqualTo(HasHorizontalAlignment.ALIGN_LEFT);
        assertThat(w.verticalPanel.getVerticalAlignment()).isEqualTo(HasVerticalAlignment.ALIGN_MIDDLE);

        // Thenion on specific style
        assertThat(w.style.getName()).isEqualTo("style");
        assertThat(w.style.disabled()).isEqualTo("disabled");
        assertThat(w.style.getText()).isEqualTo(".redBox {background-color: pink;border: 1px solid red;}.enabled {color: black;}.disabled {color: gray;}");

        // Thenion on inner style
        assertThat(w.pushButton.getStyleName()).isEqualTo("gwt-PushButton testStyle pretty gwt-PushButton-up");

        // Thenion on inner image
        assertThat(w.img.getName()).isEqualTo("img");
        assertThat(w.img.getSafeUri().asString()).isEqualTo("http://127.0.0.1:8888/gwt_test_utils_module/img.jpg");

        // Thenion on inner data
        assertThat(w.data.getName()).isEqualTo("data");
        assertThat(w.data.getSafeUri().asString()).isEqualTo("http://127.0.0.1:8888/gwt_test_utils_module/MyChildConstants.properties");

        // Thenion on "IsWidget
        assertThat(w.isWidgetLabel.getText()).isEqualTo("isWidget Label");

        assertThat(DOM.getStyleAttribute(w.textBox.getElement(), "textAlign")).isEqualTo("justify");
    }
}

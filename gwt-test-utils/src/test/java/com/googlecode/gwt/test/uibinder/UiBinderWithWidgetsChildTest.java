package com.googlecode.gwt.test.uibinder;

import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.utils.events.Browser;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UiBinderWithWidgetsChildTest extends GwtTestTest {

    @Test
    public void click_UiHandler() {
        // Given
        UiBinderWithWidgetsChild w = new UiBinderWithWidgetsChild("gael", "eric");

        // Preconditions
        assertThat(w.listBox.getVisibleItemCount()).isEqualTo(1);

        // When
        Browser.click(w.button);

        // Then
        assertThat(w.listBox.getVisibleItemCount()).isEqualTo(2);
    }

    @Test
    public void uiObjectTag() {
        // Given
        UiBinderWithWidgetsChild w = new UiBinderWithWidgetsChild("gael", "eric");

        // When
        RootPanel.get().add(w);

        // Then
        assertThat(w.listBox.getVisibleItemCount()).isEqualTo(1);

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

        // Thenion on inner style
        assertThat(w.style.getName()).isEqualTo("style");
        assertThat(w.style.getText()).isEqualTo(".redBox {background-color: pink;border: 1px solid red;}.enabled {color: black;}.disabled {color: gray;}");

        // override by child assertion
        assertThat(w.pushButton.getText()).isEqualTo("override by child");

    }

}

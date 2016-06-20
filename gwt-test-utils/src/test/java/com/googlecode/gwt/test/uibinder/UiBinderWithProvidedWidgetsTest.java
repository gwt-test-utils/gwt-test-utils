package com.googlecode.gwt.test.uibinder;

import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UiBinderWithProvidedWidgetsTest extends GwtTestTest {

    @Test
    public void instanciation() {
        // When
        UiBinderWithProvidedWidgets w = new UiBinderWithProvidedWidgets();

        assertThat(w.firstProvidedLabel.getText()).isEqualTo("my first label");
        assertThat(w.firstProvidedLabel.getCustomText()).isEqualTo("first custom text setup in ui.xml");
        assertThat(w.firstProvidedLabel.providedString).isEqualTo("first provided string");

        assertThat(w.secondProvidedLabel.getText()).isEqualTo("my second label");
        assertThat(w.secondProvidedLabel.getCustomText()).isEqualTo("second custom text setup in ui.xml");
        assertThat(w.secondProvidedLabel.providedString).isEqualTo("second provided string");
    }
}

package com.googlecode.gwt.test.uibinder;

import com.google.gwt.user.client.ui.HTML;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UiBinderWithUiChildTest extends GwtTestTest {

    @Test
    public void instanciation() {
        // When
        UiBinderWithUiChild w = new UiBinderWithUiChild();

        // Then
        assertThat(w.getWidgetWithUiChild().labelCount()).isEqualTo(2);
        assertThat(w.getWidgetWithUiChild().getLabel(0).getText()).isEqualTo("My first child label");
        assertThat(w.getWidgetWithUiChild().getLabel(1).getText()).isEqualTo("My second child label");
        assertThat(w.getWidgetWithUiChild().customWidgetCount()).isEqualTo(1);
        HTML html = (HTML) w.getWidgetWithUiChild().getCustomChild(0);
        assertThat(html.getHTML()).isEqualTo("My child HTML");
    }

}

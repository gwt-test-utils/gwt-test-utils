package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UiBinderWithDisclosurePanelTest extends GwtTestTest {

    @Test
    public void uiBinderWithDisclosurePanel() {
        // When
        UiBinderWithDisclosurePanel uiPanel = GWT.create(UiBinderWithDisclosurePanel.class);

        // Then
        assertThat(uiPanel.disclosurePanelWithTextHeader.getHeaderTextAccessor().getText()).isEqualTo("Some header text");
        assertThat(((Label) uiPanel.disclosurePanelWithTextHeader.getContent()).getText()).isEqualTo("Body label 1");

        assertThat(uiPanel.disclosurePanelWithCustomHeader.getHeaderTextAccessor().getText()).isEqualTo("Header label");
        assertThat(((Label) uiPanel.disclosurePanelWithCustomHeader.getContent()).getText()).isEqualTo("Body label 2");
    }

}

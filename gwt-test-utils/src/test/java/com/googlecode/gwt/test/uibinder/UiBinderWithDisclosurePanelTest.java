package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UiBinderWithDisclosurePanelTest extends GwtTestTest {

    @Test
    public void uiBinderWithDisclosurePanel() {
        // Act
        UiBinderWithDisclosurePanel uiPanel = GWT.create(UiBinderWithDisclosurePanel.class);

        // Assert
        assertEquals("Some header text",
                uiPanel.disclosurePanelWithTextHeader.getHeaderTextAccessor().getText());
        assertEquals("Body label 1",
                ((Label) uiPanel.disclosurePanelWithTextHeader.getContent()).getText());
        assertEquals("Header label",
                uiPanel.disclosurePanelWithCustomHeader.getHeaderTextAccessor().getText());
        assertEquals("Body label 2",
                ((Label) uiPanel.disclosurePanelWithCustomHeader.getContent()).getText());
    }

}

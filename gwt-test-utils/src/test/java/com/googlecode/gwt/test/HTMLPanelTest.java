package com.googlecode.gwt.test;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HTMLPanelTest extends GwtTestTest {

    @Test
    public void getElementById() {
        // Given
        HTMLPanel panel = new HTMLPanel("<div id=\"childDiv\" class=\"myClass\">some text</div>");

        // When
        DivElement childDiv = panel.getElementById("childDiv").cast();

        // Then
        assertThat(childDiv.getClassName()).isEqualTo("myClass");
        assertThat(childDiv.getInnerText()).isEqualTo("some text");
    }

    @Test
    public void getInnerHTML() {
        // Given
        HTMLPanel panel = new HTMLPanel(
                "<p>you can <b>test</b><a href=\"somelink\">here</a> and everything will be different</p>");

        // When & Then
        assertThat(panel.getElement().getInnerHTML()).isEqualTo("<p>you can <b>test</b><a href=\"somelink\">here</a> and everything will be different</p>");
    }
}

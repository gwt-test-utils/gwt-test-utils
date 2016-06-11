package com.googlecode.gwt.test;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HTMLPanelTest extends GwtTestTest {

    @Test
    public void getElementById() {
        // Arrange
        HTMLPanel panel = new HTMLPanel("<div id=\"childDiv\" class=\"myClass\">some text</div>");

        // Act
        DivElement childDiv = panel.getElementById("childDiv").cast();

        // Assert
        assertEquals("myClass", childDiv.getClassName());
        assertEquals("some text", childDiv.getInnerText());
    }

    @Test
    public void getInnerHTML() {
        // Arrange
        HTMLPanel panel = new HTMLPanel(
                "<p>you can <b>test</b><a href=\"somelink\">here</a> and everything will be different</p>");

        // Act & Assert
        assertEquals(
                "<p>you can <b>test</b><a href=\"somelink\">here</a> and everything will be different</p>",
                panel.getElement().getInnerHTML());
    }
}

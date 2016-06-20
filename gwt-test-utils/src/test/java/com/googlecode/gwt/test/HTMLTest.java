package com.googlecode.gwt.test;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.ui.HTML;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HTMLTest extends GwtTestTest {

    @Test
    public void checkToString() {
        // Given
        HTML html = new HTML("this is a <b>great</b> test.<br>Enjoy!");

        // When
        String result = html.toString();

        // Then
        assertThat(result).isEqualTo("<div class=\"gwt-HTML\">this is a <b>great</b> test.<br>Enjoy!</div>");
    }

    @Test
    public void getHTML() {
        // Given
        HTML html = new HTML("<a href='#'>link</a><br/>test&nbsp;test");

        // When
        String result = html.getHTML();

        // Then
        assertThat(result).isEqualTo("<a href=\"#\">link</a><br>test&nbsp;test");
    }

    @Test
    public void getText() {
        // Given
        HTML html = new HTML("<a href='#'>link</a><br/>test&nbsp;test&nbsp;");

        // When
        String result = html.getText();

        // Then
        assertThat(result).isEqualTo("linktest test ");

        // When 2
        html.setText("override <b>not bold text</b>");

        // Then 2
        assertThat(html.getText()).isEqualTo("override <b>not bold text</b>");
    }

    @Test
    public void html_withAnchor() {
        // Given
        HTML widget = new HTML("<a href=\"foo\" target=\"bar\">baz</a>");

        // When
        NodeList<Element> nodeList = widget.getElement().getElementsByTagName("a");

        // Then
        assertThat(nodeList.getLength()).isEqualTo(1);
        AnchorElement anchor = nodeList.getItem(0).cast();
        assertThat(anchor.getHref()).isEqualTo("foo");
        assertThat(anchor.getTarget()).isEqualTo("bar");
    }

    @Test
    public void html_withSpecialChars() {
        // Given
        HTML html = new HTML("<span>R&eacute;sidence&nbsp;:&nbsp;</span>");

        // When
        String result = html.getHTML();

        // Then
        assertThat(result).isEqualTo("<span>Résidence&nbsp;:&nbsp;</span>");
    }

}

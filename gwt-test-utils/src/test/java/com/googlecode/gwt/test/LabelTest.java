package com.googlecode.gwt.test;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.user.client.ui.Label;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LabelTest extends GwtTestTest {

    @SuppressWarnings("deprecation")
    @Test
    public void direction() {
        // Given
        Label label = new Label();
        // Preconditions
        assertThat(label.getDirection()).isEqualTo(Direction.DEFAULT);

        // When
        label.setDirection(Direction.RTL);

        // Then
        assertThat(label.getDirection()).isEqualTo(Direction.RTL);
    }

    @Test
    public void getText_with_HTML() {
        // Given
        Label label = new Label("<a href='#'>link</a><br/>test&nbsp;test");

        String text = label.getText();

        // Then
        assertThat(text).isEqualTo("<a href='#'>link</a><br/>test&nbsp;test");
    }

    @Test
    public void id() {
        // Given
        Label label = new Label();

        // When
        label.getElement().setId("myId");

        // Then
        assertThat(label.getElement().getAttribute("id")).isEqualTo("myId");
    }

    @Test
    public void text() {
        // Given
        Label label = new Label("foo");
        // Preconditions
        assertThat(label.getText()).isEqualTo("foo");

        // When
        label.setText("text");

        // Then
        assertThat(label.getText()).isEqualTo("text");
    }

    @Test
    public void title() {
        // Given
        Label label = new Label();

        // When
        label.setTitle("title");

        // Then
        assertThat(label.getTitle()).isEqualTo("title");
    }

    @Test
    public void visible() {
        // Given
        Label label = new Label();
        // Preconditions
        assertThat(label.isVisible()).isEqualTo(true);

        // When
        label.setVisible(false);

        // Then
        assertThat(label.isVisible()).isFalse();
    }

    @Test
    public void wordWrap() {
        // Given
        Label label = new Label();
        // Preconditions
        assertThat(label.getWordWrap()).isFalse();

        // When
        label.setWordWrap(true);

        // Then
        assertThat(label.getWordWrap()).isTrue();
    }

    @Test
    public void wrap() {
        // Given
        // Element.setInnerHTML & Document.get().getElementById are supposed to
        // work
        Document.get().getBody().setInnerHTML("<div id=\"anId\"></div>");
        DivElement div = Document.get().getElementById("anId").cast();

        // When
        Label label = Label.wrap(div);
        label.setText("My wrapped label !");

        // Then
        assertThat(label.getElement()).isEqualTo(div);
        assertThat(div.getInnerText()).isEqualTo("My wrapped label !");
    }

}

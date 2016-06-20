package com.googlecode.gwt.test;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.user.client.ui.Anchor;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AnchorTest extends GwtTestTest {

    @Test
    public void absoluteLeft() {
        // Given
        Anchor a = new Anchor();

        // Then
        assertThat(a.getAbsoluteLeft()).isEqualTo(0);
    }

    @Test
    public void absoluteTop() {
        // Given
        Anchor a = new Anchor();

        // Then
        assertThat(a.getAbsoluteTop()).isEqualTo(0);
    }

    @Test
    public void href() {
        // Given
        Anchor a = new Anchor("toto", "href");
        // Preconditions
        assertThat(a.getText()).isEqualTo("toto");
        assertThat(a.getHref()).isEqualTo("href");

        // When
        a.setHref("new");

        // Then
        assertThat(a.getHref()).isEqualTo("new");
    }

    @Test
    public void html() {
        // Given
        Anchor a = new Anchor("<h1>foo</h1>", true);
        assertThat(a.getHTML()).isEqualTo("<h1>foo</h1>");

        // When
        a.setHTML("<h1>test</h1>");

        // Then
        assertThat(a.getHTML()).isEqualTo("<h1>test</h1>");
        assertThat(a.getElement().getChildCount()).isEqualTo(1);
        HeadingElement h1 = a.getElement().getChild(0).cast();
        assertThat(h1.getTagName()).isEqualTo("H1");
        assertThat(h1.getInnerText()).isEqualTo("test");
    }

    @Test
    public void name() {
        // Given
        Anchor a = new Anchor();

        // When
        a.setName("toto");

        // Then
        assertThat(a.getName()).isEqualTo("toto");
    }

    @Test
    public void tabIndex() {
        // Given
        Anchor a = new Anchor();

        // When
        a.setTabIndex(1);

        // Then
        assertThat(a.getTabIndex()).isEqualTo(1);
    }

    @Test
    public void tagName() {
        // Given
        Anchor a = new Anchor();

        // Then
        assertThat(a.getElement().getTagName()).isEqualTo("a");
    }

    @Test
    public void target() {
        // Given
        Anchor a = new Anchor();

        // When
        a.setTarget("myTarget");

        // Then
        assertThat(a.getTarget()).isEqualTo("myTarget");
    }

    @Test
    public void text() {
        // Given
        Anchor a = new Anchor("foo");
        assertThat(a.getText()).isEqualTo("foo");

        // When
        a.setText("toto");

        // Then
        assertThat(a.getText()).isEqualTo("toto");
    }

    @Test
    public void title() {
        // Given
        Anchor a = new Anchor();

        // When
        a.setTitle("title");

        // Then
        assertThat(a.getTitle()).isEqualTo("title");
    }

    @Test
    public void visible() {
        // Given
        Anchor a = new Anchor();
        assertThat(a.isVisible()).isEqualTo(true);

        // When
        a.setVisible(false);

        // Then
        assertThat(a.isVisible()).isEqualTo(false);
    }

}

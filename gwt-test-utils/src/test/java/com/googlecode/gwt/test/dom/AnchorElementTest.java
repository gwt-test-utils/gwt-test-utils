package com.googlecode.gwt.test.dom;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AnchorElementTest extends GwtTestTest {

    private AnchorElement a;

    @Test
    public void accessKey() {
        // Preconditions
        assertThat(a.getAccessKey()).isEqualTo("");

        // When
        a.setAccessKey("k");

        // Then
        assertThat(a.getAccessKey()).isEqualTo("k");
    }

    @Test
    public void as() {
        // When
        AnchorElement asElement = AnchorElement.as(a);

        // Then
        assertThat(asElement).isEqualTo(a);
    }

    @Test
    public void blur() {
        // just check blur() does not throw any exception
        a.blur();
    }

    @Test
    public void focus() {
        // just check focus() does not throw any exception
        a.focus();
    }

    @Test
    public void href() {
        // Preconditions
        assertThat(a.getHref()).isEqualTo("");

        // When
        a.setHref("Href");

        // Then
        assertThat(a.getHref()).isEqualTo("Href");
    }

    @Test
    public void hreflang() {
        // Preconditions
        assertThat(a.getHreflang()).isEqualTo("");

        // When
        a.setHreflang("Href");

        // Then
        assertThat(a.getHreflang()).isEqualTo("Href");
    }

    @Before
    public void initDocument() {
        a = Document.get().createAnchorElement();
    }

    @Test
    public void name() {
        // Preconditions
        assertThat(a.getName()).isEqualTo("");

        // When
        a.setName("Name");

        // Then
        assertThat(a.getName()).isEqualTo("Name");
    }

    @Test
    public void rel() {
        // Preconditions
        assertThat(a.getRel()).isEqualTo("");

        // When
        a.setRel("Rel");

        // Then
        assertThat(a.getRel()).isEqualTo("Rel");
    }

    @Test
    public void tabIndex() {
        // Preconditions
        assertThat(a.getTabIndex()).isEqualTo(0);

        // When
        a.setTabIndex(4);

        // Then
        assertThat(a.getTabIndex()).isEqualTo(4);
    }

    @Test
    public void target() {
        // Preconditions
        assertThat(a.getTarget()).isEqualTo("");

        // When
        a.setTarget("Target");

        // Then
        assertThat(a.getTarget()).isEqualTo("Target");
    }

    @Test
    public void type() {
        // Preconditions
        assertThat(a.getType()).isEqualTo("");

        // When
        a.setType("Type");

        // Then
        assertThat(a.getType()).isEqualTo("Type");
    }
}

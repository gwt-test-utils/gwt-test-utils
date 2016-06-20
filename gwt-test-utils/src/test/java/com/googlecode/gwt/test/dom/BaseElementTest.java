package com.googlecode.gwt.test.dom;

import com.google.gwt.dom.client.BaseElement;
import com.google.gwt.dom.client.Document;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseElementTest extends GwtTestTest {

    private BaseElement b;

    @Test
    public void as() {
        // When
        BaseElement asElement = BaseElement.as(b);

        // Then
        assertThat(asElement).isEqualTo(b);
    }

    @Test
    public void href() {
        // Preconditions
        assertThat(b.getHref()).isEqualTo("");

        // When
        b.setHref("Href");

        // Then
        assertThat(b.getHref()).isEqualTo("Href");
    }

    @Before
    public void initDocument() {
        b = Document.get().createBaseElement();
    }

    @Test
    public void target() {
        // Preconditions
        assertThat(b.getTarget()).isEqualTo("");

        // When
        b.setTarget("Target");

        // Then
        assertThat(b.getTarget()).isEqualTo("Target");
    }

}

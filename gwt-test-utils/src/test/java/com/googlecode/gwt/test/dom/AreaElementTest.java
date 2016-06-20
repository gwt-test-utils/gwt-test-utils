package com.googlecode.gwt.test.dom;

import com.google.gwt.dom.client.AreaElement;
import com.google.gwt.dom.client.Document;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AreaElementTest extends GwtTestTest {

    private AreaElement a;

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
    public void alt() {
        // Preconditions
        assertThat(a.getAlt()).isEqualTo("");

        // When
        a.setAlt("Alt");

        // Then
        assertThat(a.getAlt()).isEqualTo("Alt");
    }

    @Test
    public void as() {
        // When
        AreaElement asElement = AreaElement.as(a);

        // Then
        assertThat(asElement).isEqualTo(a);
    }

    @Test
    public void coords() {
        // Preconditions
        assertThat(a.getCoords()).isEqualTo("");

        // When
        a.setCoords("Coords");

        // Then
        assertThat(a.getCoords()).isEqualTo("Coords");
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

    @Before
    public void initDocument() {
        a = Document.get().createAreaElement();
    }

    @Test
    public void shape() {
        // Preconditions
        assertThat(a.getShape()).isEqualTo("");

        // When
        a.setShape("Shape");

        // Then
        assertThat(a.getShape()).isEqualTo("Shape");
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

}

package com.googlecode.gwt.test.dom;

import com.google.gwt.dom.client.AreaElement;
import com.google.gwt.dom.client.Document;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AreaElementTest extends GwtTestTest {

    private AreaElement a;

    @Test
    public void accessKey() {
        // Pre-Assert
        assertEquals("", a.getAccessKey());

        // Act
        a.setAccessKey("k");

        // Assert
        assertEquals("k", a.getAccessKey());
    }

    @Test
    public void alt() {
        // Pre-Assert
        assertEquals("", a.getAlt());

        // Act
        a.setAlt("Alt");

        // Assert
        assertEquals("Alt", a.getAlt());
    }

    @Test
    public void as() {
        // Act
        AreaElement asElement = AreaElement.as(a);

        // Assert
        assertEquals(a, asElement);
    }

    @Test
    public void coords() {
        // Pre-Assert
        assertEquals("", a.getCoords());

        // Act
        a.setCoords("Coords");

        // Assert
        assertEquals("Coords", a.getCoords());
    }

    @Test
    public void href() {
        // Pre-Assert
        assertEquals("", a.getHref());

        // Act
        a.setHref("Href");

        // Assert
        assertEquals("Href", a.getHref());
    }

    @Before
    public void initDocument() {
        a = Document.get().createAreaElement();
    }

    @Test
    public void shape() {
        // Pre-Assert
        assertEquals("", a.getShape());

        // Act
        a.setShape("Shape");

        // Assert
        assertEquals("Shape", a.getShape());
    }

    @Test
    public void tabIndex() {
        // Pre-Assert
        assertEquals(0, a.getTabIndex());

        // Act
        a.setTabIndex(4);

        // Assert
        assertEquals(4, a.getTabIndex());
    }

    @Test
    public void target() {
        // Pre-Assert
        assertEquals("", a.getTarget());

        // Act
        a.setTarget("Target");

        // Assert
        assertEquals("Target", a.getTarget());
    }

}

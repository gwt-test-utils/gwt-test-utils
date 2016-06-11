package com.googlecode.gwt.test.dom;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AnchorElementTest extends GwtTestTest {

    private AnchorElement a;

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
    public void as() {
        // Act
        AnchorElement asElement = AnchorElement.as(a);

        // Assert
        assertEquals(a, asElement);
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
        // Pre-Assert
        assertEquals("", a.getHref());

        // Act
        a.setHref("Href");

        // Assert
        assertEquals("Href", a.getHref());
    }

    @Test
    public void hreflang() {
        // Pre-Assert
        assertEquals("", a.getHreflang());

        // Act
        a.setHreflang("Href");

        // Assert
        assertEquals("Href", a.getHreflang());
    }

    @Before
    public void initDocument() {
        a = Document.get().createAnchorElement();
    }

    @Test
    public void name() {
        // Pre-Assert
        assertEquals("", a.getName());

        // Act
        a.setName("Name");

        // Assert
        assertEquals("Name", a.getName());
    }

    @Test
    public void rel() {
        // Pre-Assert
        assertEquals("", a.getRel());

        // Act
        a.setRel("Rel");

        // Assert
        assertEquals("Rel", a.getRel());
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

    @Test
    public void type() {
        // Pre-Assert
        assertEquals("", a.getType());

        // Act
        a.setType("Type");

        // Assert
        assertEquals("Type", a.getType());
    }
}

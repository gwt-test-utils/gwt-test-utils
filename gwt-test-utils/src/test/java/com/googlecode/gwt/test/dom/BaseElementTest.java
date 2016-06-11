package com.googlecode.gwt.test.dom;

import com.google.gwt.dom.client.BaseElement;
import com.google.gwt.dom.client.Document;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseElementTest extends GwtTestTest {

    private BaseElement b;

    @Test
    public void as() {
        // Act
        BaseElement asElement = BaseElement.as(b);

        // Assert
        assertEquals(b, asElement);
    }

    @Test
    public void href() {
        // Pre-Assert
        assertEquals("", b.getHref());

        // Act
        b.setHref("Href");

        // Assert
        assertEquals("Href", b.getHref());
    }

    @Before
    public void initDocument() {
        b = Document.get().createBaseElement();
    }

    @Test
    public void target() {
        // Pre-Assert
        assertEquals("", b.getTarget());

        // Act
        b.setTarget("Target");

        // Assert
        assertEquals("Target", b.getTarget());
    }

}

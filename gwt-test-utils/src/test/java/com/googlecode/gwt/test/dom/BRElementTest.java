package com.googlecode.gwt.test.dom;

import com.google.gwt.dom.client.BRElement;
import com.google.gwt.dom.client.Document;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BRElementTest extends GwtTestTest {

    private BRElement b;

    @Test
    public void as() {
        // Act
        BRElement asElement = BRElement.as(b);

        // Assert
        assertEquals(b, asElement);
    }

    @Before
    public void initDocument() {
        b = Document.get().createBRElement();
    }

}

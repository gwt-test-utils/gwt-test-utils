package com.googlecode.gwt.test.dom;

import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.Document;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BodyElementTest extends GwtTestTest {

    private BodyElement b;

    @Test
    public void as() {
        // When
        BodyElement asElement = BodyElement.as(b);

        // Then
        assertThat(asElement).isEqualTo(b);
    }

    @Before
    public void initDocument() {
        b = (BodyElement) Document.get().createElement("body");
    }

}

package com.googlecode.gwt.test.dom;

import com.google.gwt.dom.client.BRElement;
import com.google.gwt.dom.client.Document;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BRElementTest extends GwtTestTest {

    private BRElement b;

    @Test
    public void as() {
        // When
        BRElement asElement = BRElement.as(b);

        // Then
        assertThat(asElement).isEqualTo(b);
    }

    @Before
    public void initDocument() {
        b = Document.get().createBRElement();
    }

}

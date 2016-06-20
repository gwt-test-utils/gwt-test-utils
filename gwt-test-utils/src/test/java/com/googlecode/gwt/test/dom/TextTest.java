package com.googlecode.gwt.test.dom;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Text;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TextTest extends GwtTestTest {

    @Test
    public void checkToString() {
        // Given
        Text text = Document.get().createTextNode("some text");

        // When
        String toString = text.toString();

        // Then
        assertThat(toString).isEqualTo("'some text'");

    }

}

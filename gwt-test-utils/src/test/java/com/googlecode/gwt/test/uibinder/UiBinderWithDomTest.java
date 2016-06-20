package com.googlecode.gwt.test.uibinder;

import com.google.gwt.dom.client.Document;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UiBinderWithDomTest extends GwtTestTest {

    @Test
    public void uiBinderDom() {
        // Given
        UiBinderWithDom helloWorld = new UiBinderWithDom();
        Document.get().getBody().appendChild(helloWorld.getElement());

        // When
        helloWorld.setName("World");

        // Then
        assertThat(helloWorld.nameSpan.getInnerText()).isEqualTo("World");
        assertThat(Document.get().getElementById("name").getInnerText()).isEqualTo("World");
    }
}

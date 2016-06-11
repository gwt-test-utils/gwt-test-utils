package com.googlecode.gwt.test.uibinder;

import com.google.gwt.dom.client.Document;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UiBinderWithDomTest extends GwtTestTest {

    @Test
    public void uiBinderDom() {
        // Arrange
        UiBinderWithDom helloWorld = new UiBinderWithDom();
        Document.get().getBody().appendChild(helloWorld.getElement());

        // Act
        helloWorld.setName("World");

        // Assert
        assertEquals("World", helloWorld.nameSpan.getInnerText());
        assertEquals("World", Document.get().getElementById("name").getInnerText());
    }
}

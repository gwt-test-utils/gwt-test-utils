package com.googlecode.gwt.test.xml;

import com.google.gwt.xml.client.*;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class XMLDocumentTest extends GwtTestTest {

    @Test
    public void createCDATASection() {
        // Given
        Document document = XMLParser.createDocument();

        // When
        CDATASection cdata = document.createCDATASection("my cdata Value");

        // Then
        assertThat(cdata.getData()).isEqualTo("my cdata Value");
        assertThat(cdata.getOwnerDocument().getDocumentElement()).isEqualTo(document.getDocumentElement());
    }

    @Test
    public void createElement() {
        // Given
        Document document = XMLParser.createDocument();

        // When
        Element element = document.createElement("elem");

        // Then
        assertThat(element.getTagName()).isEqualTo("elem");
        assertThat(element.getOwnerDocument().getDocumentElement()).isEqualTo(document.getDocumentElement());
    }

    @Test
    public void createTextNode() {
        // Given
        Document document = XMLParser.createDocument();

        // When
        Text text = document.createTextNode("my text");

        // Then
        assertThat(text.getData()).isEqualTo("my text");
        assertThat(text.getOwnerDocument().getDocumentElement()).isEqualTo(document.getDocumentElement());
    }

    @Test
    public void documentToString() {
        Document document = XMLParser.createDocument();
        Element e = document.createElement("ThisIsATest");
        e.appendChild(document.createTextNode("SomeTextNode"));
        document.appendChild(e);

        assertThat(document.toString()).isEqualTo("<ThisIsATest>SomeTextNode</ThisIsATest>");
    }

}

package com.googlecode.gwt.test.xml;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class XMLElementTest extends GwtTestTest {

    @Test
    public void elementToString() {
        // Given
        Document document = XMLParser.createDocument();
        Element e = document.createElement("ThisIsATest");
        e.appendChild(document.createTextNode("SomeTextNode"));

        // When
        String toString = e.toString();

        // Then
        assertThat(toString).isEqualTo("<ThisIsATest>SomeTextNode</ThisIsATest>");
    }

    @Test
    public void emptyElementToString() {
        // Given
        Document document = XMLParser.createDocument();
        Element e = document.createElement("ThisIsATest");

        // When
        String toString = e.toString();

        // Then
        assertThat(toString).isEqualTo("<ThisIsATest/>");
    }

    @Test
    public void setAttribute() {
        // Given
        Document document = XMLParser.createDocument();
        Element element = document.createElement("elem");

        // When
        element.setAttribute("myAttr", "myValue");

        // Then
        assertThat(element.getAttribute("myAttr")).isEqualTo("myValue");
    }

}

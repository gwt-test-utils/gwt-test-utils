package com.googlecode.gwt.test.xml;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class XMLElementTest extends GwtTestTest {

    @Test
    public void elementToString() {
        // Arrange
        Document document = XMLParser.createDocument();
        Element e = document.createElement("ThisIsATest");
        e.appendChild(document.createTextNode("SomeTextNode"));

        // Act
        String toString = e.toString();

        // Assert
        assertThat(toString).isEqualTo("<ThisIsATest>SomeTextNode</ThisIsATest>");
    }

    @Test
    public void emptyElementToString() {
        // Arrange
        Document document = XMLParser.createDocument();
        Element e = document.createElement("ThisIsATest");

        // Act
        String toString = e.toString();

        // Assert
        assertThat(toString).isEqualTo("<ThisIsATest/>");
    }

    @Test
    public void setAttribute() {
        // Arrange
        Document document = XMLParser.createDocument();
        Element element = document.createElement("elem");

        // Act
        element.setAttribute("myAttr", "myValue");

        // Assert
        assertThat(element.getAttribute("myAttr")).isEqualTo("myValue");
    }

}

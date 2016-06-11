package com.googlecode.gwt.test.uibinder;

import com.google.gwt.user.client.ui.Widget;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * SAX handler for UiBinder xml.
 *
 * @param <T> The type of the root declared {@link Widget}.
 * @author Gael Lazzari
 */
class UiXmlContentHandler<T> implements ContentHandler {

    private UiTagBuilder<T> builder;
    private final Object owner;

    private T rootComponent;
    private final Class<T> rootComponentClass;

    public UiXmlContentHandler(Class<T> rootComponentClass, Object owner) {
        this.rootComponentClass = rootComponentClass;
        this.owner = owner;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        String text = String.valueOf(ch, start, length);
        this.builder.appendText(text);
    }

    public void endDocument() throws SAXException {
        this.rootComponent = this.builder.build();
    }

    public void endElement(String nameSpaceURI, String localName, String rawName)
            throws SAXException {
        this.builder.endTag(nameSpaceURI, localName);
    }

    public void endPrefixMapping(String prefix) throws SAXException {
    }

    public T getRootComponent() {
        return rootComponent;
    }

    public void ignorableWhitespace(char[] ch, int start, int end) throws SAXException {
    }

    public void processingInstruction(String target, String data) throws SAXException {
    }

    public void setDocumentLocator(Locator locator) {

    }

    public void skippedEntity(String arg0) throws SAXException {
    }

    public void startDocument() throws SAXException {
        this.builder = UiTagBuilder.create(this.rootComponentClass, this.owner);
    }

    public void startElement(String nameSpaceURI, String localName, String rawName,
                             Attributes attributes) throws SAXException {
        this.builder.startTag(nameSpaceURI, localName, attributes);
    }

    public void startPrefixMapping(String prefix, String URI) throws SAXException {

    }

}

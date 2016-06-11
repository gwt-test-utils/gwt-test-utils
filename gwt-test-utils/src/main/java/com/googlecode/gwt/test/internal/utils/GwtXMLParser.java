package com.googlecode.gwt.test.internal.utils;

import com.google.gwt.dom.client.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.StringReader;

/**
 * XML parser used by gwt-test-utils. It relies on the SAX API. <strong>For internal use
 * only.</strong>
 *
 * @author Gael Lazzari
 */
public class GwtXMLParser {

    private static XMLReader PARSER;

    public static Document parse(String html) throws SAXException, IOException {
        XMLReader saxReader = getParser();
        GwtXmlContentHandler contentHandler = new GwtXmlContentHandler();
        saxReader.setContentHandler(contentHandler);
        saxReader.parse(new InputSource(new StringReader(html)));

        return contentHandler.getParsedDocument();
    }

    private static XMLReader getParser() {
        if (PARSER == null) {
            PARSER = XmlUtils.newXMLReader();
        }

        return PARSER;

    }

}

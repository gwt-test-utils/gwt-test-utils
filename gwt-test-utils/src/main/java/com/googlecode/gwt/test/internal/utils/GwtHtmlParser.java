package com.googlecode.gwt.test.internal.utils;

import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import com.googlecode.gwt.test.internal.AfterTestCallback;
import com.googlecode.gwt.test.internal.AfterTestCallbackManager;
import com.googlecode.html.filters.DefaultFilter;
import org.apache.xerces.xni.*;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.StringReader;
import java.util.Collections;

/**
 * HTML parser used by gwt-test-utils. It relies on htmlparser. <strong>For internal use
 * only.</strong>
 *
 * @author Gael Lazzari
 */
public class GwtHtmlParser implements AfterTestCallback {

    /**
     * Filter which keep "&nbsp;" and "&nbsp;" strings instead of converting them in a ' ' character.
     */
    private static class NbspRemover extends DefaultFilter {

        private static final String NBSP_ENTITY_NAME = "nbsp";

        boolean inNbspEntityRef;

        XMLString nbspXMLString;

        private NbspRemover() {
            nbspXMLString = new XMLString();
            char[] c = {'&', 'n', 'b', 's', 'p', ';'};
            nbspXMLString.setValues(c, 0, 6);
        }

        @Override
        public void characters(XMLString text, Augmentations augs) throws XNIException {

            if (!inNbspEntityRef) {
                super.characters(text, augs);
            }
        }

        @Override
        public void endGeneralEntity(String name, Augmentations augs) throws XNIException {

            inNbspEntityRef = false;
        }

        @Override
        public void startDocument(XMLLocator locator, String encoding, Augmentations augs)
                throws XNIException {

            super.startDocument(locator, encoding, augs);
            inNbspEntityRef = false;
        }

        @Override
        public void startGeneralEntity(String name, XMLResourceIdentifier id, String encoding,
                                       Augmentations augs) throws XNIException {

            if (NBSP_ENTITY_NAME.equals(name)) {
                inNbspEntityRef = true;
                super.characters(nbspXMLString, augs);
            } else {
                super.startGeneralEntity(name, id, encoding, augs);
            }
        }
    }

    private static GwtHtmlParser INSTANCE = new GwtHtmlParser();

    public static NodeList<Node> parse(String html) {
        return INSTANCE.parseInternal(html);
    }

    private XMLReader reader;

    private GwtHtmlParser() {
        AfterTestCallbackManager.get().registerCallback(this);
    }

    public void afterTest() throws Throwable {
        reader = null;
    }

    private XMLReader getXMLReader() throws SAXException {
        if (reader == null) {

            reader = XMLReaderFactory.createXMLReader("com.googlecode.html.parsers.SAXParser");

            // FIXME : this feature does not work with the NekoHTML version included in gwt-dev.jar
            // (1.9.13) that's why we had to copy neko 1.9.15 sources in gwt-test-utils
            reader.setFeature("http://cyberneko.org/html/features/balance-tags/document-fragment",
                    true);

            reader.setFeature("http://cyberneko.org/html/features/scanner/notify-builtin-refs", true);

            reader.setProperty("http://cyberneko.org/html/properties/default-encoding", "UTF-8");

            XMLDocumentFilter[] filters = {new NbspRemover()};

            reader.setProperty("http://cyberneko.org/html/properties/filters", filters);
        }
        return reader;

    }

    private NodeList<Node> parseInternal(String html) {
        if (html == null || html.trim().length() == 0) {
            return JsoUtils.newNodeList(Collections.<Node>emptyList());
        }

        try {
            XMLReader xmlReader = getXMLReader();
            GwtHtmlContentHandler contentHandler = new GwtHtmlContentHandler();
            xmlReader.setContentHandler(contentHandler);
            xmlReader.parse(new InputSource(new StringReader(html)));
            return contentHandler.getParsedNodes();
        } catch (Exception e) {
            throw new GwtTestPatchException("Error while parsing HTML '" + html + "'", e);
        }
    }

}

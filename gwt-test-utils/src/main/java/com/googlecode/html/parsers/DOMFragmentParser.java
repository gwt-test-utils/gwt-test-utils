/*
 * Copyright 2002-2009 Andy Clark, Marc Guillemot
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. ============================================================== This file contains
 * some code from Apache Xerces-J which is used in accordance with the Apache license.
 */

package com.googlecode.html.parsers;

import com.googlecode.html.HTMLConfiguration;
import org.apache.xerces.impl.Constants;
import org.apache.xerces.util.ErrorHandlerWrapper;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.xni.*;
import org.apache.xerces.xni.parser.*;
import org.w3c.dom.*;
import org.xml.sax.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * A DOM parser for HTML fragments.
 *
 * @author Andy Clark
 * @version $Id: DOMFragmentParser.java,v 1.8 2005/02/14 03:56:54 andyc Exp $
 */
public class DOMFragmentParser implements XMLDocumentHandler {

    //
    // Constants
    //

    // features

    /**
     * Current element node.
     */
    protected static final String CURRENT_ELEMENT_NODE = Constants.XERCES_PROPERTY_PREFIX
            + Constants.CURRENT_ELEMENT_NODE_PROPERTY;

    /**
     * Document fragment balancing only.
     */
    protected static final String DOCUMENT_FRAGMENT = "http://cyberneko.org/html/features/document-fragment";

    // properties

    /**
     * Property identifier: error handler.
     */
    protected static final String ERROR_HANDLER = Constants.XERCES_PROPERTY_PREFIX
            + Constants.ERROR_HANDLER_PROPERTY;

    /**
     * Recognized features.
     */
    protected static final String[] RECOGNIZED_FEATURES = {DOCUMENT_FRAGMENT,};

    /**
     * Recognized properties.
     */
    protected static final String[] RECOGNIZED_PROPERTIES = {ERROR_HANDLER, CURRENT_ELEMENT_NODE,};

    //
    // Data
    //

    /**
     * Current node.
     */
    protected Node fCurrentNode;

    /**
     * Document.
     */
    protected Document fDocument;

    /**
     * DOM document fragment.
     */
    protected DocumentFragment fDocumentFragment;

    /**
     * Document source.
     */
    protected XMLDocumentSource fDocumentSource;

    /**
     * True if within a CDATA section.
     */
    protected boolean fInCDATASection;

    /**
     * Parser configuration.
     */
    protected XMLParserConfiguration fParserConfiguration;

    //
    // Constructors
    //

    /**
     * Default constructor.
     */
    public DOMFragmentParser() {
        fParserConfiguration = new HTMLConfiguration();
        fParserConfiguration.addRecognizedFeatures(RECOGNIZED_FEATURES);
        fParserConfiguration.addRecognizedProperties(RECOGNIZED_PROPERTIES);
        fParserConfiguration.setFeature(DOCUMENT_FRAGMENT, true);
        fParserConfiguration.setDocumentHandler(this);
    } // <init>()

    //
    // Public methods
    //

    /**
     * Characters.
     */
    public void characters(XMLString text, Augmentations augs) throws XNIException {

        if (fInCDATASection) {
            Node node = fCurrentNode.getLastChild();
            if (node != null && node.getNodeType() == Node.CDATA_SECTION_NODE) {
                CDATASection cdata = (CDATASection) node;
                cdata.appendData(text.toString());
            } else {
                CDATASection cdata = fDocument.createCDATASection(text.toString());
                fCurrentNode.appendChild(cdata);
            }
        } else {
            Node node = fCurrentNode.getLastChild();
            if (node != null && node.getNodeType() == Node.TEXT_NODE) {
                Text textNode = (Text) node;
                textNode.appendData(text.toString());
            } else {
                Text textNode = fDocument.createTextNode(text.toString());
                fCurrentNode.appendChild(textNode);
            }
        }

    } // characters(XMLString,Augmentations)

    /**
     * Comment.
     */
    public void comment(XMLString text, Augmentations augs) throws XNIException {
        Comment comment = fDocument.createComment(text.toString());
        fCurrentNode.appendChild(comment);
    } // comment(XMLString,Augmentations)

    /**
     * Document type declaration.
     */
    public void doctypeDecl(String root, String pubid, String sysid, Augmentations augs)
            throws XNIException {
    } // doctypeDecl(String,String,String,Augmentations)

    /**
     * Empty element.
     */
    public void emptyElement(QName element, XMLAttributes attrs, Augmentations augs)
            throws XNIException {
        startElement(element, attrs, augs);
        endElement(element, augs);
    } // emptyElement(QName,XMLAttributes,Augmentations)

    /**
     * End CDATA section.
     */
    public void endCDATA(Augmentations augs) throws XNIException {
        fInCDATASection = false;
    } // endCDATA(Augmentations)

    /**
     * End document.
     */
    public void endDocument(Augmentations augs) throws XNIException {
    } // endDocument(Augmentations)

    /**
     * End element.
     */
    public void endElement(QName element, Augmentations augs) throws XNIException {
        fCurrentNode = fCurrentNode.getParentNode();
    } // endElement(QName,Augmentations)

    /**
     * End general entity.
     */
    public void endGeneralEntity(String name, Augmentations augs) throws XNIException {
        fCurrentNode = fCurrentNode.getParentNode();
    } // endGeneralEntity(String,Augmentations)

    //
    // XMLDocumentHandler methods
    //

    /**
     * End prefix mapping. @deprecated Since Xerces 2.2.0.
     */
    public void endPrefixMapping(String prefix, Augmentations augs) throws XNIException {
    } // endPrefixMapping(String,Augmentations)

    /**
     * Returns the document source.
     */
    public XMLDocumentSource getDocumentSource() {
        return fDocumentSource;
    } // getDocumentSource():XMLDocumentSource

    /**
     * Return the current error handler.
     *
     * @return The current error handler, or null if none has been registered.
     * @see #setErrorHandler
     */
    public ErrorHandler getErrorHandler() {

        ErrorHandler errorHandler = null;
        try {
            XMLErrorHandler xmlErrorHandler = (XMLErrorHandler) fParserConfiguration.getProperty(ERROR_HANDLER);
            if (xmlErrorHandler != null && xmlErrorHandler instanceof ErrorHandlerWrapper) {
                errorHandler = ((ErrorHandlerWrapper) xmlErrorHandler).getErrorHandler();
            }
        } catch (XMLConfigurationException e) {
            // do nothing
        }
        return errorHandler;

    } // getErrorHandler():ErrorHandler

    // since Xerces 2.2.0

    /**
     * Query the state of a feature.
     * <p>
     * Query the current state of any feature in a SAX2 parser. The parser might not recognize the
     * feature.
     *
     * @param featureId The unique identifier (URI) of the feature being set.
     * @return The current state of the feature.
     * @throws org.xml.sax.SAXNotRecognizedException If the requested feature is not known.
     * @throws SAXNotSupportedException              If the requested feature is known but not supported.
     */
    public boolean getFeature(String featureId) throws SAXNotRecognizedException,
            SAXNotSupportedException {

        try {
            return fParserConfiguration.getFeature(featureId);
        } catch (XMLConfigurationException e) {
            String message = e.getMessage();
            if (e.getType() == XMLConfigurationException.NOT_RECOGNIZED) {
                throw new SAXNotRecognizedException(message);
            } else {
                throw new SAXNotSupportedException(message);
            }
        }

    } // getFeature(String):boolean

    /**
     * Query the value of a property.
     * <p>
     * Return the current value of a property in a SAX2 parser. The parser might not recognize the
     * property.
     *
     * @param propertyId The unique identifier (URI) of the property being set.
     * @return The current value of the property.
     * @throws org.xml.sax.SAXNotRecognizedException If the requested property is not known.
     * @throws SAXNotSupportedException              If the requested property is known but not supported.
     */
    public Object getProperty(String propertyId) throws SAXNotRecognizedException,
            SAXNotSupportedException {

        if (propertyId.equals(CURRENT_ELEMENT_NODE)) {
            return (fCurrentNode != null && fCurrentNode.getNodeType() == Node.ELEMENT_NODE)
                    ? fCurrentNode : null;
        }

        try {
            return fParserConfiguration.getProperty(propertyId);
        } catch (XMLConfigurationException e) {
            String message = e.getMessage();
            if (e.getType() == XMLConfigurationException.NOT_RECOGNIZED) {
                throw new SAXNotRecognizedException(message);
            } else {
                throw new SAXNotSupportedException(message);
            }
        }

    } // getProperty(String):Object

    /**
     * Ignorable whitespace.
     */
    public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
        characters(text, augs);
    } // ignorableWhitespace(XMLString,Augmentations)

    /**
     * Parses a document fragment.
     */
    public void parse(InputSource source, DocumentFragment fragment) throws SAXException,
            IOException {

        fCurrentNode = fDocumentFragment = fragment;
        fDocument = fDocumentFragment.getOwnerDocument();

        try {
            String pubid = source.getPublicId();
            String sysid = source.getSystemId();
            String encoding = source.getEncoding();
            InputStream stream = source.getByteStream();
            Reader reader = source.getCharacterStream();

            XMLInputSource inputSource = new XMLInputSource(pubid, sysid, sysid);
            inputSource.setEncoding(encoding);
            inputSource.setByteStream(stream);
            inputSource.setCharacterStream(reader);

            fParserConfiguration.parse(inputSource);
        } catch (XMLParseException e) {
            Exception ex = e.getException();
            if (ex != null) {
                throw new SAXParseException(e.getMessage(), null, ex);
            }
            throw new SAXParseException(e.getMessage(), null);
        }

    } // parse(InputSource,DocumentFragment)

    /**
     * Parses a document fragment.
     */
    public void parse(String systemId, DocumentFragment fragment) throws SAXException, IOException {
        parse(new InputSource(systemId), fragment);
    } // parse(String,DocumentFragment)

    /**
     * Processing instruction.
     */
    public void processingInstruction(final String target, final XMLString data,
                                      final Augmentations augs) throws XNIException {

        final String s = data.toString();
        if (XMLChar.isValidName(s)) {
            final ProcessingInstruction pi = fDocument.createProcessingInstruction(target, s);
            fCurrentNode.appendChild(pi);
        }
    } // processingInstruction(String,XMLString,Augmentations)

    /**
     * Sets the document source.
     */
    public void setDocumentSource(XMLDocumentSource source) {
        fDocumentSource = source;
    } // setDocumentSource(XMLDocumentSource)

    /**
     * Allow an application to register an error event handler.
     * <p>
     * <p>
     * If the application does not register an error handler, all error events reported by the SAX
     * parser will be silently ignored; however, normal processing may not continue. It is highly
     * recommended that all SAX applications implement an error handler to avoid unexpected bugs.
     * </p>
     * <p>
     * <p>
     * Applications may register a new or different handler in the middle of a parse, and the SAX
     * parser must begin using the new handler immediately.
     * </p>
     *
     * @param errorHandler The error handler.
     * @throws java.lang.NullPointerException If the handler argument is null.
     * @see #getErrorHandler
     */
    public void setErrorHandler(ErrorHandler errorHandler) {
        fParserConfiguration.setErrorHandler(new ErrorHandlerWrapper(errorHandler));
    } // setErrorHandler(ErrorHandler)

    /**
     * Set the state of any feature in a SAX2 parser. The parser might not recognize the feature, and
     * if it does recognize it, it might not be able to fulfill the request.
     *
     * @param featureId The unique identifier (URI) of the feature.
     * @param state     The requested state of the feature (true or false).
     * @throws SAXNotRecognizedException If the requested feature is not known.
     * @throws SAXNotSupportedException  If the requested feature is known, but the requested state
     *                                   is not supported.
     */
    public void setFeature(String featureId, boolean state) throws SAXNotRecognizedException,
            SAXNotSupportedException {

        try {
            fParserConfiguration.setFeature(featureId, state);
        } catch (XMLConfigurationException e) {
            String message = e.getMessage();
            if (e.getType() == XMLConfigurationException.NOT_RECOGNIZED) {
                throw new SAXNotRecognizedException(message);
            } else {
                throw new SAXNotSupportedException(message);
            }
        }

    } // setFeature(String,boolean)

    /**
     * Set the value of any property in a SAX2 parser. The parser might not recognize the property,
     * and if it does recognize it, it might not support the requested value.
     *
     * @param propertyId The unique identifier (URI) of the property being set.
     * @param value      The value to which the property is being set.
     * @throws SAXNotRecognizedException If the requested property is not known.
     * @throws SAXNotSupportedException  If the requested property is known, but the requested
     *                                   value is not supported.
     */
    public void setProperty(String propertyId, Object value) throws SAXNotRecognizedException,
            SAXNotSupportedException {

        try {
            fParserConfiguration.setProperty(propertyId, value);
        } catch (XMLConfigurationException e) {
            String message = e.getMessage();
            if (e.getType() == XMLConfigurationException.NOT_RECOGNIZED) {
                throw new SAXNotRecognizedException(message);
            } else {
                throw new SAXNotSupportedException(message);
            }
        }

    } // setProperty(String,Object)

    /**
     * Start CDATA section.
     */
    public void startCDATA(Augmentations augs) throws XNIException {
        fInCDATASection = true;
    } // startCDATA(Augmentations)

    /**
     * Start document.
     */
    public void startDocument(XMLLocator locator, String encoding, Augmentations augs)
            throws XNIException {
        startDocument(locator, encoding, null, augs);
    } // startDocument(XMLLocator,String,Augmentations)

    /**
     * Start document.
     */
    public void startDocument(XMLLocator locator, String encoding, NamespaceContext nscontext,
                              Augmentations augs) throws XNIException {
        fInCDATASection = false;
    } // startDocument(XMLLocator,String,NamespaceContext,Augmentations)

    /**
     * Start element.
     */
    public void startElement(QName element, XMLAttributes attrs, Augmentations augs)
            throws XNIException {
        Element elementNode = fDocument.createElement(element.rawname);
        int count = attrs != null ? attrs.getLength() : 0;
        for (int i = 0; i < count; i++) {
            String aname = attrs.getQName(i);
            String avalue = attrs.getValue(i);
            if (XMLChar.isValidName(aname)) {
                elementNode.setAttribute(aname, avalue);
            }
        }
        fCurrentNode.appendChild(elementNode);
        fCurrentNode = elementNode;
    } // startElement(QName,XMLAttributes,Augmentations)

    /**
     * Start general entity.
     */
    public void startGeneralEntity(String name, XMLResourceIdentifier id, String encoding,
                                   Augmentations augs) throws XNIException {
        EntityReference entityRef = fDocument.createEntityReference(name);
        fCurrentNode.appendChild(entityRef);
        fCurrentNode = entityRef;
    } // startGeneralEntity(String,XMLResourceIdentifier,String,Augmentations)

    /**
     * Start prefix mapping. @deprecated Since Xerces 2.2.0.
     */
    public void startPrefixMapping(String prefix, String uri, Augmentations augs)
            throws XNIException {
    } // startPrefixMapping(String,String,Augmentations)

    /**
     * Text declaration.
     */
    public void textDecl(String version, String encoding, Augmentations augs) throws XNIException {
    } // textDecl(String,String,Augmentations)

    /**
     * XML declaration.
     */
    public void xmlDecl(String version, String encoding, String standalone, Augmentations augs)
            throws XNIException {
    } // xmlDecl(String,String,String,Augmentations)

} // class DOMFragmentParser

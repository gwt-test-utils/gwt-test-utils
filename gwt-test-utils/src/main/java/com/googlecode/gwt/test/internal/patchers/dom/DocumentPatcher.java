package com.googlecode.gwt.test.internal.patchers.dom;

import com.google.gwt.dom.client.*;
import com.googlecode.gwt.test.exceptions.GwtTestConfigurationException;
import com.googlecode.gwt.test.exceptions.GwtTestDomException;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.internal.AfterTestCallback;
import com.googlecode.gwt.test.internal.AfterTestCallbackManager;
import com.googlecode.gwt.test.internal.GwtConfig;
import com.googlecode.gwt.test.internal.utils.DoubleMap;
import com.googlecode.gwt.test.internal.utils.GwtHtmlParser;
import com.googlecode.gwt.test.internal.utils.JsoUtils;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import com.googlecode.gwt.test.utils.JavaScriptObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@PatchClass(Document.class)
class DocumentPatcher {

    private static class DocumentHolder implements AfterTestCallback {

        private Document document;

        private DocumentHolder() {
            AfterTestCallbackManager.get().registerFinalCallback(this);
        }

        public void afterTest() throws Throwable {
            document = null;

            Class<?> documentClass = Class.forName("com.google.gwt.dom.client.Document$");
            GwtReflectionUtils.setStaticField(documentClass, "doc", null);
        }
    }

    private static final String DOCUMENT_ELEMENT = "documentElement";

    private static DocumentHolder DOCUMENT_HOLDER = new DocumentHolder();

    private static final String EMPTY_HTML = "<html><head></head><body></body></html>";

    private static final DoubleMap<String, String, String> HTML_ELEMENT_PROTOTYPES = new DoubleMap<String, String, String>();

    private static int ID = 0;

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentPatcher.class);

    @PatchMethod
    static Text createTextNode(Document document, String data) {
        return JsoUtils.newText(data, document);
    }

    @PatchMethod
    static String createUniqueId(Document document) {
        ID++;
        return "elem_" + Long.toString(ID);
    }

    @PatchMethod
    static BodyElement getBody(Document document) {
        NodeList<Element> bodyList = getElementsByTagName(document, "body");
        if (bodyList.getLength() < 1) {
            return null;
        } else {
            return bodyList.getItem(0).cast();
        }
    }

    @PatchMethod
    static String getCompatMode(Document document) {
        return "toto";
    }

    @PatchMethod
    static String getDomain(Document document) {
        return null;
    }

    @PatchMethod
    static Element getElementById(Node document, String elementId) {
        List<Node> childs = JsoUtils.getChildNodeInnerList(document);

        for (Node n : childs) {
            if (Node.ELEMENT_NODE == n.getNodeType()) {
                Element currentElement = n.cast();
                if (elementId.equals(currentElement.getId())) {
                    return currentElement;
                }
            }
            Element result = getElementById(n, elementId);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    @PatchMethod
    static NodeList<Element> getElementsByTagName(Node node, String tagName) {
        List<Element> result = new ArrayList<>();
        inspectDomForTag(node, tagName, result);

        return JsoUtils.newNodeList(result);
    }

    @PatchMethod
    static String getReferrer(Document document) {
        return "";
    }

    @PatchMethod
    static Document nativeGet() {
        if (DOCUMENT_HOLDER.document == null) {
            try {
                DOCUMENT_HOLDER.document = JsoUtils.newDocument();
                Element e = parseHTMLElement(DOCUMENT_HOLDER.document);
                DOCUMENT_HOLDER.document.appendChild(e);
                JavaScriptObjects.setProperty(DOCUMENT_HOLDER.document, DOCUMENT_ELEMENT, e);
                return DOCUMENT_HOLDER.document;
            } catch (Exception e) {
                if (GwtTestException.class.isInstance(e)) {
                    throw (GwtTestException) e;
                } else {
                    throw new GwtTestDomException("Unable to create Document", e);
                }
            }
        }
        return DOCUMENT_HOLDER.document;
    }

    private static Element findHTMLElement(NodeList<Node> nodes) {
        int i = 0;
        while (i < nodes.getLength()) {
            Node node = nodes.getItem(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element e = node.cast();
                if ("html".equalsIgnoreCase(e.getTagName())) {
                    return e;
                }
            }
            i++;
        }

        return null;
    }

    private static String getHostPageHTML(String hostPagePath) {

        // try classpath relative path
        InputStream is = JavaScriptObjects.class.getClassLoader().getResourceAsStream(hostPagePath);

        if (is == null) {
            try {
                // try project relative or absolute path
                is = new FileInputStream(hostPagePath);
            } catch (FileNotFoundException e) {
                // handle just after
            }
        }

        if (is == null) {
            LOGGER.warn("Cannot find the host HTML file '"
                    + hostPagePath
                    + "', fallback to an empty HTML document instead. You may want to override "
                    + GwtConfig.get().getModuleRunner().getClass().getSimpleName()
                    + ".getHostPagePath(String moduleFullQualifiedName) method to specify the relative path of the your HTML file from the root directory of your java project");

            return EMPTY_HTML;
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } catch (IOException e) {
            throw new GwtTestConfigurationException("Error while reading module HTML host page '"
                    + hostPagePath + "'", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // don't care
                }
            }
        }

    }

    private static void inspectDomForTag(Node node, String tagName, List<Element> result) {
        List<Node> childs = JsoUtils.getChildNodeInnerList(node);

        for (Node n : childs) {
            if (Node.ELEMENT_NODE == n.getNodeType()) {
                Element childElem = n.cast();
                if ("*".equals(tagName) || tagName.equalsIgnoreCase(childElem.getTagName())) {
                    result.add(childElem);
                }
            }
            inspectDomForTag(n, tagName, result);
        }
    }

    private static Element parseHTMLElement(Document document) {
        String moduleName = GwtConfig.get().getModuleAlias();
        String hostPagePath = GwtConfig.get().getModuleRunner().getHostPagePath();

        if (hostPagePath == null) {
            // return a default empty HTML element
            Element defaultHTMLElement = JsoUtils.newElement("HTML", document);
            defaultHTMLElement.appendChild(JsoUtils.newElement("body", document));
            return defaultHTMLElement;
        }

        String html = HTML_ELEMENT_PROTOTYPES.get(moduleName, hostPagePath);

        if (html == null) {
            // parsing of the host page
            html = getHostPageHTML(hostPagePath);
            HTML_ELEMENT_PROTOTYPES.put(moduleName, hostPagePath, html);
        }

        NodeList<Node> list = GwtHtmlParser.parse(html);
        Element htmlElement = findHTMLElement(list);

        if (htmlElement == null) {
            throw new GwtTestDomException("Cannot find a root <html> element in file '" + hostPagePath
                    + "'");
        }

        return htmlElement;
    }

}

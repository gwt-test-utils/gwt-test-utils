package com.googlecode.gwt.test.xml;

import com.google.gwt.xml.client.*;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.internal.utils.XmlUtils;
import org.junit.Test;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.InputStream;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class XMLParserTest extends GwtTestTest {

    @Test
    public void parse() throws Exception {
        // Arrange
        String xmlContent = convertXMLFileToString("/someXML.xml");

        // Act
        Document document = XMLParser.parse(xmlContent);

        // Assert
        Element documentElement = document.getDocumentElement();
        assertEquals("beans", documentElement.getTagName());

        Element beans = (Element) document.getFirstChild();
        assertEquals("beans", beans.getTagName());
        assertNull(beans.getNextSibling());
        assertNull(beans.getPreviousSibling());

        Element testBean = document.getElementById("testBean");
        NodeList beanList = document.getElementsByTagName("bean");

        assertEquals(2, beanList.getLength());
        assertEquals("testBean", beanList.item(0).getAttributes().getNamedItem("id").getNodeValue());
        assertEquals("bean", testBean.getTagName());
        assertEquals("bean", testBean.getNodeName());
        assertEquals("http://www.springframework.org/schema/beans", testBean.getNamespaceURI());
        assertEquals("org.springframework.beans.TestBean", testBean.getAttribute("class"));
        assertTrue(testBean.hasAttribute("class"));
        assertFalse(testBean.hasAttribute("fooAttr"));
        Attr classAttr = testBean.getAttributeNode("class");
        assertEquals("class", classAttr.getName());
        assertEquals("org.springframework.beans.TestBean", classAttr.getValue());
        assertEquals("http://www.springframework.org/schema/beans", classAttr.getNamespaceURI());
        assertEquals("class", classAttr.getNodeName());
        // CDATA attribute
        Element ageProperty = (Element) testBean.getChildNodes().item(0);
        assertEquals(1, ageProperty.getAttributes().getLength());
        assertEquals("age", ageProperty.getAttribute("name"));
        assertEquals("<10>", ageProperty.getNodeValue());
        // TODO : pass this assertion
        // assertEquals("#cdata-section",
        // ageProperty.getFirstChild().getNodeName());

        // "spouse" child bean assertions
        NamedNodeMap innerBeanAgePropertyAttributes = beanList.item(1).getChildNodes().item(0).getAttributes();
        assertEquals("age", innerBeanAgePropertyAttributes.getNamedItem("name").getNodeValue());
        assertEquals("11", innerBeanAgePropertyAttributes.getNamedItem("value").getNodeValue());

        // bean from "util" namespace
        Element name = (Element) testBean.getNextSibling();
        assertEquals("property-path", name.getTagName());
        assertEquals("property-path", name.getNodeName());
        assertEquals("http://www.springframework.org/schema/util", name.getNamespaceURI());

    }

    @Test
    public void parseSimple() {
        // Arrange
        String simpleXML = "<tags><tag>value</tag></tags>";

        // Act
        Document doc = XMLParser.parse(simpleXML);

        // Assert
        NodeList tags = doc.getElementsByTagName("tag");
        assertEquals("<tag>value</tag>", tags.item(0).toString());
        Text text = (Text) tags.item(0).getChildNodes().item(0);
        assertEquals("value", text.getData());
        assertEquals("#text", tags.item(0).getFirstChild().getNodeName());
        assertEquals("<tags><tag>value</tag></tags>", doc.getDocumentElement().toString());

        assertEquals(doc.getDocumentElement(), tags.item(0).getOwnerDocument().getDocumentElement());
    }

    @Test
    public void removeWhitespace() throws Exception {
        // Arrange
        Document document = XMLParser.createDocument();
        Element child = document.createElement("child");
        child.setNodeValue("     ");
        document.appendChild(child);
        Element child2 = document.createElement("child");
        child2.appendChild(document.createCDATASection("    "));
        document.appendChild(child2);

        // Pre-Assert : empty TextNode should exists
        assertEquals(1, child.getChildNodes().getLength());
        assertEquals(1, child2.getChildNodes().getLength());

        // Act
        XMLParser.removeWhitespace(document);

        // Assert
        assertEquals(0, child.getChildNodes().getLength());
        // empty cdata is not removed
        assertEquals(1, child2.getChildNodes().getLength());
    }

    private String convertXMLFileToString(String fileName) {
        try {
            InputStream inputStream = this.getClass().getResourceAsStream(fileName);
            org.w3c.dom.Document doc = XmlUtils.newDocumentBuilder().parse(inputStream);
            StringWriter stw = new StringWriter();
            Transformer serializer = TransformerFactory.newInstance().newTransformer();
            serializer.transform(new DOMSource(doc), new StreamResult(stw));
            return stw.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

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

import static org.assertj.core.api.Assertions.assertThat;

public class XMLParserTest extends GwtTestTest {

    @Test
    public void parse() throws Exception {
        // Given
        String xmlContent = convertXMLFileToString("/someXML.xml");

        // When
        Document document = XMLParser.parse(xmlContent);

        // Then
        Element documentElement = document.getDocumentElement();
        assertThat(documentElement.getTagName()).isEqualTo("beans");

        Element beans = (Element) document.getFirstChild();
        assertThat(beans.getTagName()).isEqualTo("beans");
        assertThat(beans.getNextSibling()).isNull();
        assertThat(beans.getPreviousSibling()).isNull();

        Element testBean = document.getElementById("testBean");
        NodeList beanList = document.getElementsByTagName("bean");

        assertThat(beanList.getLength()).isEqualTo(2);
        assertThat(beanList.item(0).getAttributes().getNamedItem("id").getNodeValue()).isEqualTo("testBean");
        assertThat(testBean.getTagName()).isEqualTo("bean");
        assertThat(testBean.getNodeName()).isEqualTo("bean");
        assertThat(testBean.getNamespaceURI()).isEqualTo("http://www.springframework.org/schema/beans");
        assertThat(testBean.getAttribute("class")).isEqualTo("org.springframework.beans.TestBean");
        assertThat(testBean.hasAttribute("class")).isTrue();
        assertThat(testBean.hasAttribute("fooAttr")).isFalse();
        Attr classAttr = testBean.getAttributeNode("class");
        assertThat(classAttr.getName()).isEqualTo("class");
        assertThat(classAttr.getValue()).isEqualTo("org.springframework.beans.TestBean");
        assertThat(classAttr.getNamespaceURI()).isEqualTo("http://www.springframework.org/schema/beans");
        assertThat(classAttr.getNodeName()).isEqualTo("class");
        // CDATA attribute
        Element ageProperty = (Element) testBean.getChildNodes().item(0);
        assertThat(ageProperty.getAttributes().getLength()).isEqualTo(1);
        assertThat(ageProperty.getAttribute("name")).isEqualTo("age");
        assertThat(ageProperty.getNodeValue()).isEqualTo("<10>");
        // TODO : pass this assertion
        // ThenEquals("#cdata-section",
        // ageProperty.getFirstChild().getNodeName());

        // "spouse" child bean assertions
        NamedNodeMap innerBeanAgePropertyAttributes = beanList.item(1).getChildNodes().item(0).getAttributes();
        assertThat(innerBeanAgePropertyAttributes.getNamedItem("name").getNodeValue()).isEqualTo("age");
        assertThat(innerBeanAgePropertyAttributes.getNamedItem("value").getNodeValue()).isEqualTo("11");

        // bean from "util" namespace
        Element name = (Element) testBean.getNextSibling();
        assertThat(name.getTagName()).isEqualTo("property-path");
        assertThat(name.getNodeName()).isEqualTo("property-path");
        assertThat(name.getNamespaceURI()).isEqualTo("http://www.springframework.org/schema/util");

    }

    @Test
    public void parseSimple() {
        // Given
        String simpleXML = "<tags><tag>value</tag></tags>";

        // When
        Document doc = XMLParser.parse(simpleXML);

        // Then
        NodeList tags = doc.getElementsByTagName("tag");
        assertThat(tags.item(0).toString()).isEqualTo("<tag>value</tag>");
        Text text = (Text) tags.item(0).getChildNodes().item(0);
        assertThat(text.getData()).isEqualTo("value");
        assertThat(tags.item(0).getFirstChild().getNodeName()).isEqualTo("#text");
        assertThat(doc.getDocumentElement().toString()).isEqualTo("<tags><tag>value</tag></tags>");

        assertThat(tags.item(0).getOwnerDocument().getDocumentElement()).isEqualTo(doc.getDocumentElement());
    }

    @Test
    public void removeWhitespace() throws Exception {
        // Given
        Document document = XMLParser.createDocument();
        Element child = document.createElement("child");
        child.setNodeValue("     ");
        document.appendChild(child);
        Element child2 = document.createElement("child");
        child2.appendChild(document.createCDATASection("    "));
        document.appendChild(child2);

        // Preconditions : empty TextNode should exists
        assertThat(child.getChildNodes().getLength()).isEqualTo(1);
        assertThat(child2.getChildNodes().getLength()).isEqualTo(1);

        // When
        XMLParser.removeWhitespace(document);

        // Then
        assertThat(child.getChildNodes().getLength()).isEqualTo(0);
        // empty cdata is not removed
        assertThat(child2.getChildNodes().getLength()).isEqualTo(1);
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

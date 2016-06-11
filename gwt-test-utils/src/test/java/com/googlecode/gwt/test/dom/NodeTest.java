package com.googlecode.gwt.test.dom;

import com.google.gwt.dom.client.*;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.internal.utils.JsoUtils;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class NodeTest extends GwtTestTest {

    private Node n;

    @Test
    public void appendChilds() {
        // Arrange
        BaseElement c0 = Document.get().createBaseElement();
        ButtonElement c1 = Document.get().createPushButtonElement();

        // Act
        n.appendChild(c0);
        n.appendChild(c1);

        // Assert
        assertEquals(2, n.getChildNodes().getLength());
        assertEquals(c0, n.getChildNodes().getItem(0));
        assertEquals(c1, n.getChildNodes().getItem(1));
    }

    @Test
    public void as() {
        // Act & Assert 1
        assertEquals(n, Node.as(n));
    }

    @Before
    public void beforeNodeTest() {
        n = Document.get().createDivElement();
    }

    @Test
    public void clone_Deep() {
        // Arrange
        AnchorElement child = Document.get().createAnchorElement();
        child.setInnerText("child inner text");
        child.getStyle().setBackgroundColor("black");
        n.appendChild(child);

        // Act
        DivElement newNode = n.cloneNode(true).cast();

        // Assert
        assertEquals(Node.ELEMENT_NODE, newNode.getNodeType());
        DivElement source = n.cast();
        assertEquals(source.getInnerText(), newNode.getInnerText());
        assertEquals(source.getInnerHTML(), newNode.getInnerHTML());
        assertEquals(source.toString(), newNode.toString());

        assertNull(newNode.getParentNode());
        assertEquals(n.getChildNodes().getLength(), newNode.getChildNodes().getLength());

        assertEquals(Node.ELEMENT_NODE, newNode.getChildNodes().getItem(0).getNodeType());
        AnchorElement childElement = newNode.getChildNodes().getItem(0).cast();
        assertEquals("child inner text", childElement.getInnerText());

        Style newStyle = childElement.getStyle();
        assertTrue(newStyle != child.getStyle());
        assertEquals("black", newStyle.getBackgroundColor());
    }

    @Test
    public void clone_NotDeep() {
        // Arrange
        Element e = n.cast();
        e.setInnerText("text");
        e.getStyle().setBackgroundColor("black");

        AnchorElement child = Document.get().createAnchorElement();
        child.setInnerText("child inner text");
        n.appendChild(child);

        // Act
        DivElement newNode = n.cloneNode(false).cast();

        // Assert
        assertEquals(Node.ELEMENT_NODE, newNode.getNodeType());
        assertTrue(e.getStyle() != newNode.getStyle());
        assertEquals("black", newNode.getStyle().getBackgroundColor());
        assertEquals("text", newNode.getInnerText());
        assertNull(newNode.getParentNode());
        assertEquals(2, n.getChildNodes().getLength());
        assertEquals(1, newNode.getChildNodes().getLength());
    }

    @Test
    public void getFirstChild() {
        // Pre-Assert
        assertNull(n.getFirstChild());

        // Arrange
        ButtonElement be0 = Document.get().createPushButtonElement();
        ButtonElement be1 = Document.get().createPushButtonElement();
        n.appendChild(be0);
        n.appendChild(be1);

        // Act & Assert
        assertEquals(be0, n.getFirstChild());
    }

    @Test
    public void getLastChild() {
        // Pre-Assert
        assertNull(n.getLastChild());

        // Arrange
        ButtonElement be0 = Document.get().createPushButtonElement();
        ButtonElement be1 = Document.get().createPushButtonElement();
        n.appendChild(be0);
        n.appendChild(be1);

        // Act & Assert
        assertEquals(be1, n.getLastChild());
    }

    @Test
    public void getNextSibling() {
        // Pre-Assert
        assertNull(n.getNextSibling());

        // Arrange
        ButtonElement be0 = Document.get().createPushButtonElement();
        ButtonElement be1 = Document.get().createPushButtonElement();
        n.appendChild(be0);
        n.appendChild(be1);

        // Act & Assert
        assertEquals(be1, be0.getNextSibling());
    }

    @Test
    public void getOwnerDocument() {
        // Act & Assert
        assertEquals(Document.get(), n.getOwnerDocument());
    }

    @Test
    public void getParentNode() {
        // Pre-Assert
        assertNull(n.getParentNode());

        // Arrange
        BaseElement be = Document.get().createBaseElement();
        n.appendChild(be);

        // Act & assert
        assertEquals(n, be.getParentNode());
    }

    @Test
    public void getPreviousSibling() {
        // Pre-Assert
        assertNull(n.getPreviousSibling());

        // Arrange
        ButtonElement be0 = Document.get().createPushButtonElement();
        ButtonElement be1 = Document.get().createPushButtonElement();
        n.appendChild(be0);
        n.appendChild(be1);

        // Act & Assert
        assertEquals(be0, be1.getPreviousSibling());
    }

    @Test
    public void hasChildNodes() {
        // Pre-Assert
        assertFalse("New element should not have child nodes", n.hasChildNodes());

        // Arrange
        BaseElement be = Document.get().createBaseElement();
        n.appendChild(be);

        // Act & Assert
        assertTrue("Element should have a child node", n.hasChildNodes());
    }

    @Test
    public void insertBefore() {
        // Arrange
        ButtonElement be0 = Document.get().createPushButtonElement();
        ButtonElement be1 = Document.get().createPushButtonElement();
        ButtonElement be2 = Document.get().createPushButtonElement();
        ButtonElement be3 = Document.get().createPushButtonElement();
        ButtonElement be4 = Document.get().createPushButtonElement();
        ButtonElement be5 = Document.get().createPushButtonElement();
        n.appendChild(be0);
        n.appendChild(be2);

        // Act & Assert
        n.insertBefore(be1, be2);
        n.insertBefore(be3, null);
        n.insertBefore(be4, be5);

        assertEquals(be0, n.getChildNodes().getItem(0));
        assertEquals(be1, n.getChildNodes().getItem(1));
        assertEquals(be2, n.getChildNodes().getItem(2));
        assertEquals(be3, n.getChildNodes().getItem(3));
        assertEquals(be4, n.getChildNodes().getItem(4));
    }

    @Test
    public void is() {
        // Arrange
        NodeList<OptionElement> list = JsoUtils.newNodeList();

        // Act & Assert
        assertFalse("null is not a DOM node", Node.is(null));
        assertFalse("NodeList is not a DOM node", Node.is(list));
        assertTrue("AnchorElement is a DOM node", Node.is(Document.get().createAnchorElement()));
    }

    @Test
    public void isOrHasChild_hasChild() {
        // Arrange
        Element a = Document.get().createAnchorElement();
        Element div = Document.get().createDivElement();

        div.appendChild(a);
        n.appendChild(div);

        // Act & Assert
        assertTrue(n.isOrHasChild(a));
    }

    @Test
    public void isOrHasChild_is() {
        // Act & Assert
        assertTrue(n.isOrHasChild(n));
    }

    @Test
    public void isOrHasChild_notChild() {
        // Arrange
        Element a = Document.get().createAnchorElement();

        // Act & Assert
        assertFalse(n.isOrHasChild(a));

    }

    @Test
    public void nodeName() {
        // Act & Assert
        assertEquals("#document", Document.get().getNodeName());
        assertEquals("HTML", Document.get().getDocumentElement().getNodeName());
        assertEquals("a", Document.get().createAnchorElement().getNodeName());
        assertEquals("#text", JsoUtils.newText("test", Document.get()).getNodeName());
    }

    @Test
    public void nodeType() {
        // Act & Assert
        assertEquals(Node.DOCUMENT_NODE, Document.get().getNodeType());
        assertEquals(Node.ELEMENT_NODE, Document.get().getDocumentElement().getNodeType());
        assertEquals(Node.ELEMENT_NODE, Document.get().createAnchorElement().getNodeType());
        assertEquals(Node.TEXT_NODE, JsoUtils.newText("test", Document.get()).getNodeType());
    }

    @Test
    public void nodeValue_Document() {
        // Arrange
        Node documentNode = Document.get();
        // Pre-Assert
        assertNull(documentNode.getNodeValue());

        // Act
        documentNode.setNodeValue("node");

        // Assert
        assertNull(documentNode.getNodeValue());
    }

    @Test
    public void nodeValue_Element() {
        // Arrange
        Node doucmentNode = Document.get().getDocumentElement();
        // Pre-Assert
        assertNull(doucmentNode.getNodeValue());

        // Act
        doucmentNode.setNodeValue("node");

        // Assert
        assertNull(doucmentNode.getNodeValue());
    }

    @Test
    public void nodeValue_Text() {
        // Arrange
        Text textNode = Document.get().createTextNode("data");
        // Pre-Assert
        assertEquals("data", textNode.getNodeValue());

        // Act
        textNode.setNodeValue("node");

        // Assert
        assertEquals("node", textNode.getNodeValue());
        assertEquals("node", textNode.getData());
    }

    @Test
    public void removeAllChildren() {
        // Given
        n.appendChild(Document.get().createAnchorElement());
        n.appendChild(Document.get().createDivElement());

        // When
        n.removeAllChildren();

        // Then
        assertThat(n.getChildCount()).isEqualTo(0);
    }

    @Test
    public void removeChild() {
        // Arrange
        BaseElement c0 = Document.get().createBaseElement();
        ButtonElement c1 = Document.get().createPushButtonElement();
        n.appendChild(c0);
        n.appendChild(c1);

        // Act
        n.removeChild(c1);

        // Assert
        assertEquals(1, n.getChildNodes().getLength());
        assertEquals(c0, n.getChildNodes().getItem(0));
    }

    @Test
    public void replaceChild() {
        // Arrange
        BaseElement c0 = Document.get().createBaseElement();
        ButtonElement c1 = Document.get().createPushButtonElement();
        AnchorElement c2 = Document.get().createAnchorElement();
        n.appendChild(c0);
        n.appendChild(c1);

        // Act
        Node replaced = n.replaceChild(c2, c1);
        Node nullReplaced = n.replaceChild(c2, c1);
        Node nullReplaced2 = n.replaceChild(c2, null);

        // Assert
        assertEquals(2, n.getChildNodes().getLength());
        assertEquals(c0, n.getChildNodes().getItem(0));
        assertEquals(c2, n.getChildNodes().getItem(1));
        assertEquals(c1, replaced);
        assertNull(nullReplaced);
        assertNull(nullReplaced2);
    }

}

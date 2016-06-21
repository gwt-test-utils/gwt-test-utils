package com.googlecode.gwt.test.dom;

import com.google.gwt.dom.client.*;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.internal.utils.JsoUtils;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NodeTest extends GwtTestTest {

    private Node n;

    @Test
    public void appendChilds() {
        // Given
        BaseElement c0 = Document.get().createBaseElement();
        ButtonElement c1 = Document.get().createPushButtonElement();

        // When
        n.appendChild(c0);
        n.appendChild(c1);

        // Then
        assertThat(n.getChildNodes().getLength()).isEqualTo(2);
        assertThat(n.getChildNodes().getItem(0)).isEqualTo(c0);
        assertThat(n.getChildNodes().getItem(1)).isEqualTo(c1);
    }

    @Test
    public void as() {
        // When & Then 1
        assertThat(Node.as(n)).isEqualTo(n);
    }

    @Before
    public void beforeNodeTest() {
        n = Document.get().createDivElement();
    }

    @Test
    public void clone_Deep() {
        // Given
        AnchorElement child = Document.get().createAnchorElement();
        child.setInnerText("child inner text");
        child.getStyle().setBackgroundColor("black");
        n.appendChild(child);

        // When
        DivElement newNode = n.cloneNode(true).cast();

        // Then
        assertThat(newNode.getNodeType()).isEqualTo(Node.ELEMENT_NODE);
        DivElement source = n.cast();
        assertThat(newNode.getInnerText()).isEqualTo(source.getInnerText());
        assertThat(newNode.getInnerHTML()).isEqualTo(source.getInnerHTML());
        assertThat(newNode.toString()).isEqualTo(source.toString());

        assertThat(newNode.getParentNode()).isNull();
        assertThat(newNode.getChildNodes().getLength()).isEqualTo(n.getChildNodes().getLength());

        assertThat(newNode.getChildNodes().getItem(0).getNodeType()).isEqualTo(Node.ELEMENT_NODE);
        AnchorElement childElement = newNode.getChildNodes().getItem(0).cast();
        assertThat(childElement.getInnerText()).isEqualTo("child inner text");

        Style newStyle = childElement.getStyle();
        assertThat(newStyle != child.getStyle()).isTrue();
        assertThat(newStyle.getBackgroundColor()).isEqualTo("black");
    }

    @Test
    public void clone_NotDeep() {
        // Given
        Element e = n.cast();
        e.setInnerText("text");
        e.getStyle().setBackgroundColor("black");

        AnchorElement child = Document.get().createAnchorElement();
        child.setInnerText("child inner text");
        n.appendChild(child);

        // When
        DivElement newNode = n.cloneNode(false).cast();

        // Then
        assertThat(newNode.getNodeType()).isEqualTo(Node.ELEMENT_NODE);
        assertThat(e.getStyle() != newNode.getStyle()).isTrue();
        assertThat(newNode.getStyle().getBackgroundColor()).isEqualTo("black");
        assertThat(newNode.getInnerText()).isEqualTo("text");
        assertThat(newNode.getParentNode()).isNull();
        assertThat(n.getChildNodes().getLength()).isEqualTo(2);
        assertThat(newNode.getChildNodes().getLength()).isEqualTo(1);
    }

    @Test
    public void getFirstChild() {
        // Preconditions
        assertThat(n.getFirstChild()).isNull();

        // Given
        ButtonElement be0 = Document.get().createPushButtonElement();
        ButtonElement be1 = Document.get().createPushButtonElement();
        n.appendChild(be0);
        n.appendChild(be1);

        // When & Then
        assertThat(n.getFirstChild()).isEqualTo(be0);
    }

    @Test
    public void getLastChild() {
        // Preconditions
        assertThat(n.getLastChild()).isNull();

        // Given
        ButtonElement be0 = Document.get().createPushButtonElement();
        ButtonElement be1 = Document.get().createPushButtonElement();
        n.appendChild(be0);
        n.appendChild(be1);

        // When & Then
        assertThat(n.getLastChild()).isEqualTo(be1);
    }

    @Test
    public void getNextSibling() {
        // Preconditions
        assertThat(n.getNextSibling()).isNull();

        // Given
        ButtonElement be0 = Document.get().createPushButtonElement();
        ButtonElement be1 = Document.get().createPushButtonElement();
        n.appendChild(be0);
        n.appendChild(be1);

        // When & Then
        assertThat(be0.getNextSibling()).isEqualTo(be1);
    }

    @Test
    public void getOwnerDocument() {
        // When & Then
        assertThat(n.getOwnerDocument()).isEqualTo(Document.get());
    }

    @Test
    public void getParentNode() {
        // Preconditions
        assertThat(n.getParentNode()).isNull();

        // Given
        BaseElement be = Document.get().createBaseElement();
        n.appendChild(be);

        // When & Then
        assertThat(be.getParentNode()).isEqualTo(n);
    }

    @Test
    public void getPreviousSibling() {
        // Preconditions
        assertThat(n.getPreviousSibling()).isNull();

        // Given
        ButtonElement be0 = Document.get().createPushButtonElement();
        ButtonElement be1 = Document.get().createPushButtonElement();
        n.appendChild(be0);
        n.appendChild(be1);

        // When & Then
        assertThat(be1.getPreviousSibling()).isEqualTo(be0);
    }

    @Test
    public void hasChildNodes() {
        // Preconditions
        assertThat(n.hasChildNodes()).as("New element should not have child nodes").isFalse();

        // Given
        BaseElement be = Document.get().createBaseElement();
        n.appendChild(be);

        // When & Then
        assertThat(n.hasChildNodes()).as("Element should have a child node").isTrue();
    }

    @Test
    public void insertBefore() {
        // Given
        ButtonElement be0 = Document.get().createPushButtonElement();
        ButtonElement be1 = Document.get().createPushButtonElement();
        ButtonElement be2 = Document.get().createPushButtonElement();
        ButtonElement be3 = Document.get().createPushButtonElement();
        ButtonElement be4 = Document.get().createPushButtonElement();
        ButtonElement be5 = Document.get().createPushButtonElement();
        n.appendChild(be0);
        n.appendChild(be2);

        // When & Then
        n.insertBefore(be1, be2);
        n.insertBefore(be3, null);
        n.insertBefore(be4, be5);

        assertThat(n.getChildNodes().getItem(0)).isEqualTo(be0);
        assertThat(n.getChildNodes().getItem(1)).isEqualTo(be1);
        assertThat(n.getChildNodes().getItem(2)).isEqualTo(be2);
        assertThat(n.getChildNodes().getItem(3)).isEqualTo(be3);
        assertThat(n.getChildNodes().getItem(4)).isEqualTo(be4);
    }

    @Test
    public void is() {
        // Given
        NodeList<OptionElement> list = JsoUtils.newNodeList();

        // When & Then
        assertThat(Node.is(null)).as("null is not a DOM node").isFalse();
        assertThat(Node.is(list)).as("NodeList is not a DOM node").isFalse();
        assertThat(Node.is(Document.get().createAnchorElement())).as("AnchorElement is a DOM node").isTrue();
    }

    @Test
    public void isOrHasChild_hasChild() {
        // Given
        Element a = Document.get().createAnchorElement();
        Element div = Document.get().createDivElement();

        div.appendChild(a);
        n.appendChild(div);

        // When & Then
        assertThat(n.isOrHasChild(a)).isTrue();
    }

    @Test
    public void isOrHasChild_is() {
        // When & Then
        assertThat(n.isOrHasChild(n)).isTrue();
    }

    @Test
    public void isOrHasChild_notChild() {
        // Given
        Element a = Document.get().createAnchorElement();

        // When & Then
        assertThat(n.isOrHasChild(a)).isFalse();

    }

    @Test
    public void nodeName() {
        // When & Then
        assertThat(Document.get().getNodeName()).isEqualTo("#document");
        assertThat(Document.get().getDocumentElement().getNodeName()).isEqualTo("HTML");
        assertThat(Document.get().createAnchorElement().getNodeName()).isEqualTo("a");
        assertThat(JsoUtils.newText("test", Document.get()).getNodeName()).isEqualTo("#text");
    }

    @Test
    public void nodeType() {
        // When & Then
        assertThat(Document.get().getNodeType()).isEqualTo(Node.DOCUMENT_NODE);
        assertThat(Document.get().getDocumentElement().getNodeType()).isEqualTo(Node.ELEMENT_NODE);
        assertThat(Document.get().createAnchorElement().getNodeType()).isEqualTo(Node.ELEMENT_NODE);
        assertThat(JsoUtils.newText("test", Document.get()).getNodeType()).isEqualTo(Node.TEXT_NODE);
    }

    @Test
    public void nodeValue_Document() {
        // Given
        Node documentNode = Document.get();
        // Preconditions
        assertThat(documentNode.getNodeValue()).isNull();

        // When
        documentNode.setNodeValue("node");

        // Then
        assertThat(documentNode.getNodeValue()).isNull();
    }

    @Test
    public void nodeValue_Element() {
        // Given
        Node doucmentNode = Document.get().getDocumentElement();
        // Preconditions
        assertThat(doucmentNode.getNodeValue()).isNull();

        // When
        doucmentNode.setNodeValue("node");

        // Then
        assertThat(doucmentNode.getNodeValue()).isNull();
    }

    @Test
    public void nodeValue_Text() {
        // Given
        Text textNode = Document.get().createTextNode("data");
        // Preconditions
        assertThat(textNode.getNodeValue()).isEqualTo("data");

        // When
        textNode.setNodeValue("node");

        // Then
        assertThat(textNode.getNodeValue()).isEqualTo("node");
        assertThat(textNode.getData()).isEqualTo("node");
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
        // Given
        BaseElement c0 = Document.get().createBaseElement();
        ButtonElement c1 = Document.get().createPushButtonElement();
        n.appendChild(c0);
        n.appendChild(c1);

        // When
        n.removeChild(c1);

        // Then
        assertThat(n.getChildNodes().getLength()).isEqualTo(1);
        assertThat(n.getChildNodes().getItem(0)).isEqualTo(c0);
    }

    @Test
    public void replaceChild() {
        // Given
        BaseElement c0 = Document.get().createBaseElement();
        ButtonElement c1 = Document.get().createPushButtonElement();
        AnchorElement c2 = Document.get().createAnchorElement();
        n.appendChild(c0);
        n.appendChild(c1);

        // When
        Node replaced = n.replaceChild(c2, c1);
        Node nullReplaced = n.replaceChild(c2, c1);
        Node nullReplaced2 = n.replaceChild(c2, null);

        // Then
        assertThat(n.getChildNodes().getLength()).isEqualTo(2);
        assertThat(n.getChildNodes().getItem(0)).isEqualTo(c0);
        assertThat(n.getChildNodes().getItem(1)).isEqualTo(c2);
        assertThat(replaced).isEqualTo(c1);
        assertThat(nullReplaced).isNull();
        assertThat(nullReplaced2).isNull();
    }

}

package com.googlecode.gwt.test.dom;

import com.google.gwt.dom.client.*;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DocumentTest extends GwtTestTest {

    private Document d;

    @Test
    public void checkToString() {
        // Given
        d.getBody().appendChild(d.createAnchorElement());
        d.getBody().appendChild(d.createAreaElement());

        // When
        String toString = Document.get().toString();

        // Then
        assertThat(toString).isEqualTo("<html><body><a></a><area></area></body></html>");

    }

    @Test
    public void createElement() {
        // When & Then
        assertThat(d.createAnchorElement().getTagName()).isEqualTo("a");
        assertThat(d.createAreaElement().getTagName()).isEqualTo("area");
        assertThat(d.createBaseElement().getTagName()).isEqualTo("base");
        assertThat(d.createElement("body").getTagName()).isEqualTo("body");
        assertThat(d.createBRElement().getTagName()).isEqualTo("br");
        assertThat(d.createDivElement().getTagName()).isEqualTo("div");
        assertThat(d.createDLElement().getTagName()).isEqualTo("dl");
        assertThat(d.createFieldSetElement().getTagName()).isEqualTo("fieldset");
        assertThat(d.createFormElement().getTagName()).isEqualTo("form");
        assertThat(d.createFrameElement().getTagName()).isEqualTo("frame");
        assertThat(d.createFrameSetElement().getTagName()).isEqualTo("frameset");
        assertThat(d.createHeadElement().getTagName()).isEqualTo("head");
        assertThat(d.createHElement(1).getTagName()).isEqualTo("h1");
        assertThat(d.createHElement(2).getTagName()).isEqualTo("h2");
        assertThat(d.createHElement(3).getTagName()).isEqualTo("h3");
        assertThat(d.createHElement(4).getTagName()).isEqualTo("h4");
        assertThat(d.createHElement(5).getTagName()).isEqualTo("h5");
        assertThat(d.createHElement(6).getTagName()).isEqualTo("h6");
        assertThat(d.createHRElement().getTagName()).isEqualTo("hr");
        assertThat(d.createIFrameElement().getTagName()).isEqualTo("iframe");
        assertThat(d.createImageElement().getTagName()).isEqualTo("img");
        assertThat(d.createElement("input").getTagName()).isEqualTo("input");
        assertThat(d.createLabelElement().getTagName()).isEqualTo("label");
        assertThat(d.createLegendElement().getTagName()).isEqualTo("legend");
        assertThat(d.createLIElement().getTagName()).isEqualTo("li");
        assertThat(d.createLinkElement().getTagName()).isEqualTo("link");
        assertThat(d.createMapElement().getTagName()).isEqualTo("map");
        assertThat(d.createMetaElement().getTagName()).isEqualTo("meta");
        assertThat(d.createInsElement().getTagName()).isEqualTo("ins");
        assertThat(d.createDelElement().getTagName()).isEqualTo("del");
        assertThat(d.createObjectElement().getTagName()).isEqualTo("object");
        assertThat(d.createOLElement().getTagName()).isEqualTo("ol");
        assertThat(d.createOptGroupElement().getTagName()).isEqualTo("optgroup");
        assertThat(d.createOptionElement().getTagName()).isEqualTo("option");
        assertThat(d.createPElement().getTagName()).isEqualTo("p");
        assertThat(d.createParamElement().getTagName()).isEqualTo("param");
        assertThat(d.createPreElement().getTagName()).isEqualTo("pre");
        assertThat(d.createQElement().getTagName()).isEqualTo("q");
        assertThat(d.createBlockQuoteElement().getTagName()).isEqualTo("blockquote");
        assertThat(d.createScriptElement().getTagName()).isEqualTo("script");
        assertThat(d.createSelectElement().getTagName()).isEqualTo("select");
        assertThat(d.createSpanElement().getTagName()).isEqualTo("span");
        assertThat(d.createStyleElement().getTagName()).isEqualTo("style");
        assertThat(d.createCaptionElement().getTagName()).isEqualTo("caption");
        assertThat(d.createTDElement().getTagName()).isEqualTo("td");
        assertThat(d.createTHElement().getTagName()).isEqualTo("th");
        assertThat(d.createColElement().getTagName()).isEqualTo("col");
        assertThat(d.createColGroupElement().getTagName()).isEqualTo("colgroup");
        assertThat(d.createTableElement().getTagName()).isEqualTo("table");
        assertThat(d.createTBodyElement().getTagName()).isEqualTo("tbody");
        assertThat(d.createTFootElement().getTagName()).isEqualTo("tfoot");
        assertThat(d.createTHeadElement().getTagName()).isEqualTo("thead");
        assertThat(d.createTextAreaElement().getTagName()).isEqualTo("textarea");
        assertThat(d.createTitleElement().getTagName()).isEqualTo("title");
        assertThat(d.createULElement().getTagName()).isEqualTo("ul");
    }

    @Test
    public void createImageInputElement() {
        // When
        InputElement e = d.createImageInputElement();

        // Then
        assertThat(e.getType()).isEqualTo("image");
    }

    @Test
    public void createPushButtonElement() {
        // When
        ButtonElement e = d.createPushButtonElement();

        // Then
        assertThat(e.getTagName()).isEqualTo("button");
        assertThat(e.getType()).isEqualTo("button");
    }

    @Test
    public void createRadioInputElement() {
        // When
        InputElement e = d.createRadioInputElement("test");

        // Then
        assertThat(e.getType()).isEqualTo("RADIO");
        assertThat(e.getName()).isEqualTo("test");
    }

    @Test
    public void createResetButtonElement() {
        // When
        ButtonElement e = d.createResetButtonElement();

        // Then
        assertThat(e.getTagName()).isEqualTo("button");
        assertThat(e.getType()).isEqualTo("reset");
    }

    @Test
    public void createSubmitButtonElement() {
        // When
        ButtonElement e = d.createSubmitButtonElement();

        // Then
        assertThat(e.getTagName()).isEqualTo("button");
        assertThat(e.getType()).isEqualTo("submit");
    }

    @Test
    public void createTextNode() {
        // When
        String data = "myData";
        Text text = d.createTextNode(data);

        // Then
        assertThat(text.getNodeType()).isEqualTo(Node.TEXT_NODE);
        assertThat(text.getData()).isEqualTo(data);
    }

    @Test
    public void getBodyOffsetLeft() {
        // When
        int result = d.getBodyOffsetLeft();

        // Then
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void getBodyOffsetTop() {
        // When
        int result = d.getBodyOffsetTop();

        // Then
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void getDocumentElement() {
        // When
        Element e = d.getDocumentElement();

        // Then
        assertThat(e.getTagName()).isEqualTo("HTML");
        assertThat(e.getNodeName()).isEqualTo("HTML");
        assertThat(e.getNodeType()).isEqualTo(Node.ELEMENT_NODE);
        assertThat(e).isEqualTo(d.getChild(0));
    }

    @Test
    public void getDomain() {
        // When & Then
        assertThat(d.getDomain()).isNull();
    }

    @Test
    public void getElementById_InBody() {
        // Given
        AnchorElement a = Document.get().createAnchorElement();
        a.setId("myId");
        DivElement div = Document.get().createDivElement();
        div.appendChild(a);
        d.getBody().appendChild(div);

        // When
        Element result = d.getElementById("myId");

        // Then
        assertThat(result).isEqualTo(a);
    }

    @Test
    public void getElementById_NotFound() {
        // Given
        AnchorElement a1 = Document.get().createAnchorElement();
        AnchorElement a2 = Document.get().createAnchorElement();
        AnchorElement a3 = Document.get().createAnchorElement();
        DivElement d1 = Document.get().createDivElement();
        d.appendChild(a1);
        d.appendChild(a1);
        d.appendChild(a2);
        a2.appendChild(a3);
        d.appendChild(d1);

        // When
        Element result = d.getElementById("myId");

        // Then
        assertThat(result).isNull();
    }

    @Test
    public void getElementByIdFound() {
        // Given
        AnchorElement a1 = Document.get().createAnchorElement();
        AnchorElement a2 = Document.get().createAnchorElement();
        AnchorElement a3 = Document.get().createAnchorElement();
        a3.setId("myId");
        DivElement d1 = Document.get().createDivElement();
        d.appendChild(a1);
        d.appendChild(a1);
        d.appendChild(a2);
        a2.appendChild(a3);
        d.appendChild(d1);

        // When
        Element result = d.getElementById("myId");

        // Then
        assertThat(result).isEqualTo(a3);
    }

    @Test
    public void getElementByTagName() {
        // Given
        AnchorElement a1 = Document.get().createAnchorElement();
        AnchorElement a2 = Document.get().createAnchorElement();
        AnchorElement a3 = Document.get().createAnchorElement();
        DivElement d1 = Document.get().createDivElement();
        d.appendChild(a1);
        d.appendChild(a1);
        d.appendChild(a2);
        a2.appendChild(a3);
        d.appendChild(d1);

        // When
        NodeList<Element> nodes = d.getElementsByTagName("a");

        // Then
        assertThat(nodes.getLength()).isEqualTo(3);
        assertThat(nodes.getItem(0)).isEqualTo(a1);
        assertThat(nodes.getItem(1)).isEqualTo(a2);
        assertThat(nodes.getItem(2)).isEqualTo(a3);
    }

    @Test
    public void getReferrer() {
        assertThat(d.getReferrer()).isEqualTo("");
    }

    @Before
    public void initDocument() {
        d = Document.get();
    }

    @Test
    public void isCSS1Compat() {
        // When
        boolean result = d.isCSS1Compat();

        // Then
        assertThat(result).isEqualTo(false);
    }

    @Test
    public void scrollLeft() {
        // Preconditions
        assertThat(d.getScrollLeft()).isEqualTo(0);

        // When
        d.setScrollLeft(3);

        // Then
        assertThat(d.getScrollLeft()).isEqualTo(3);
    }

    @Test
    public void scrollTop() {
        // Preconditions
        assertThat(d.getScrollTop()).isEqualTo(0);

        // When
        d.setScrollTop(3);

        // Then
        assertThat(d.getScrollTop()).isEqualTo(3);
    }

}

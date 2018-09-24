package com.googlecode.gwt.test.dom;

import com.google.gwt.dom.client.*;
import com.google.gwt.dom.client.Style.Float;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.internal.utils.JsoUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ElementTest extends GwtTestTest {

    private Element e;

    @Test
    public void attribute() {
        // Preconditions
        assertThat(e.getAttribute("input")).isEqualTo("");

        // When
        e.setAttribute("input", "text");

        // Then
        assertThat(e.getAttribute("input")).isEqualTo("text");
    }

    @Before
    public void beforeElementTest() {
        e = Document.get().createDivElement();
    }

    @Test
    public void cast_OK() {
        // When
        DivElement casted = e.cast();

        // Then
        assertThat(casted).isNotNull();
    }

    @Test
    public void checkHashCode() {
        // Given
        Map<Element, String> map = new HashMap<>();

        // When
        map.put(e, "a string value");
        map.put(e, "this value should have overrided the first one");

        // Then
        assertThat(map.get(e)).isEqualTo("this value should have overrided the first one");
    }

    @Test
    public void checkToString() {
        // Given
        DivElement div = Document.get().createDivElement();
        div.setAttribute("someAttr", "myVal");
        div.getStyle().setBackgroundColor("black");
        div.getStyle().setFloat(Float.LEFT);
        div.getStyle().setProperty("backgroundColor", "white");
        div.setInnerHTML("<span>in span</span> out span");

        // When
        String html = div.toString();

        // Then
        assertThat(html).isEqualTo("<div someattr=\"myVal\" style=\"float: left; background-color: white; \"><span>in span</span> out span</div>");

    }

    @Test
    public void className() {
        // When 1
        e.setClassName("testClass");

        // Then 1
        assertThat(e.getClassName()).isEqualTo("testClass");
        assertThat(e.getAttribute("class")).isEqualTo("testClass");
        assertThat(e.getAttribute("CLASS")).isEqualTo("testClass");
        assertThat(e.getAttribute("className")).isEqualTo("");
        assertThat(e.getAttribute("CLASSNAME")).isEqualTo("");
        assertThat(e.getPropertyString("class")).isNull();
        assertThat(e.getPropertyString("CLASS")).isNull();
        assertThat(e.getPropertyString("className")).isEqualTo("testClass");
        assertThat(e.getPropertyString("CLASSNAME")).isNull();

        // When 2
        e.addClassName("addon");

        // Then 2
        assertThat(e.getClassName()).isEqualTo("testClass addon");
        assertThat(e.getAttribute("class")).isEqualTo("testClass addon");

        // When 3
        e.setAttribute("class", "override");

        // Then 3
        assertThat(e.getClassName()).isEqualTo("override");
        assertThat(e.getAttribute("class")).isEqualTo("override");
    }

    @Test
    public void clone_Deep() {
        // Given
        e.setTitle("title");
        e.setPropertyBoolean("bool", true);

        Element child = Document.get().createAnchorElement();
        child.setTitle("child");
        e.appendChild(child);

        // When
        DivElement newNode = e.cloneNode(true).cast();

        // Then
        assertThat(newNode.getTitle()).isEqualTo("title");
        assertThat(newNode.getParentNode()).as("Cloned element's parent should be null").isNull();
        assertThat(newNode.getPropertyBoolean("bool")).isEqualTo(true);
        assertThat(newNode.getChildNodes().getLength()).isEqualTo(1);
        assertThat(child != newNode.getChildNodes().getItem(0)).isTrue();
        assertThat(e.getChildNodes().getLength()).isEqualTo(1);
    }

    @Test
    public void clone_NotDeep() {
        // Given
        e.setTitle("title");
        e.setPropertyBoolean("bool", true);

        AnchorElement child = Document.get().createAnchorElement();
        child.setTitle("child");
        e.appendChild(child);

        // When
        DivElement newNode = e.cloneNode(false).cast();

        // Then
        assertThat(newNode.getTitle()).isEqualTo("title");
        assertThat(newNode.getParentNode()).as("Cloned element's parent should be null").isNull();
        assertThat(newNode.getPropertyBoolean("bool")).isEqualTo(true);
        assertThat(newNode.getChildNodes().getLength()).isEqualTo(0);
        assertThat(e.getChildNodes().getLength()).isEqualTo(1);
    }

    @Test
    public void dir() {
        // When
        e.setDir("dir");

        // Then
        assertThat(e.getDir()).isEqualTo("dir");
    }

    @Test
    public void domImplementation() {
        // Given
        e.setAttribute("test", "testAttr");

        // Then getAttribute() is case insensitive
        assertThat(e.getAttribute("test")).isEqualTo("testAttr");
        assertThat(e.getAttribute("Test")).isEqualTo("testAttr");

        // Then hasAttribute is case insensitive
        assertThat(e.hasAttribute("teST")).isTrue();

        // Then removeAttribute is case insensitve
        e.removeAttribute("tEst");
        assertThat(e.getAttribute("test")).isEqualTo("");
        assertThat(e.hasAttribute("teST")).isFalse();

        // Then "non standard" DOM properties returns 'undefined' for String,
        // Object and JSO
        assertThat(e.getPropertyString("test")).isNull();
        assertThat(e.getPropertyBoolean("test")).isFalse();
        assertThat(e.getPropertyInt("test")).isEqualTo(0);
        assertThat((Double) e.getPropertyDouble("test")).isEqualTo(new Double(0.0));
        assertThat(e.getPropertyObject("test")).isNull();
        assertThat(e.getPropertyJSO("test")).isNull();

        // Then "standard" DOM properties returns "" for String
        assertThat(e.getPropertyString("className")).isEqualTo("");
        assertThat(e.getPropertyString("classnamE")).isNull();

        e.setPropertyString("className", "testClass");
        assertThat(e.getPropertyString("className")).isEqualTo("testClass");
        // Special case "class" and "className"
        assertThat(e.getPropertyString("class")).isNull();
        assertThat(e.getAttribute("class")).isEqualTo("testClass");
        assertThat(e.getAttribute("CLASSNAME")).isEqualTo("");
        assertThat(e.getPropertyString("CLASSNAME")).isNull();

        // Then on Style JSO
        assertThat(e.getAttribute("style")).isEqualTo(""); // prints ""
        assertThat(e.getPropertyString("style")).isEqualTo("");
    }

    @Test
    public void getElementByTagName() {
        // Given
        AnchorElement ae0 = Document.get().createAnchorElement();
        AnchorElement ae1 = Document.get().createAnchorElement();
        ButtonElement be = Document.get().createPushButtonElement();
        e.appendChild(ae0);
        e.appendChild(ae1);
        e.appendChild(be);

        // When
        NodeList<Element> anchorList = e.getElementsByTagName("a");
        NodeList<Element> buttonList = e.getElementsByTagName("button");
        NodeList<Element> allList = e.getElementsByTagName("*");

        // Then
        assertThat(anchorList.getLength()).isEqualTo(2);
        assertThat(anchorList.getItem(0)).isEqualTo(ae0);
        assertThat(anchorList.getItem(1)).isEqualTo(ae1);

        assertThat(buttonList.getLength()).isEqualTo(1);
        assertThat(buttonList.getItem(0)).isEqualTo(be);

        assertThat(allList.getLength()).isEqualTo(3);
        assertThat(allList.getItem(0)).isEqualTo(ae0);
        assertThat(allList.getItem(1)).isEqualTo(ae1);
        assertThat(allList.getItem(2)).isEqualTo(be);
    }

    @Test
    public void getFirstChildElement() {
        // Preconditions
        assertThat(e.getFirstChildElement()).isNull();

        // Given
        Node node = Document.get().createTextNode("test");
        ButtonElement be0 = Document.get().createPushButtonElement();
        ButtonElement be1 = Document.get().createPushButtonElement();
        e.appendChild(node);
        e.appendChild(be0);
        e.appendChild(be1);

        // When & Then
        assertThat(e.getFirstChildElement()).isEqualTo(be0);
    }

    @Test
    public void getNextSiblingElement() {
        // Preconditions
        assertThat(e.getNextSiblingElement()).isNull();

        // Given
        ButtonElement be0 = Document.get().createPushButtonElement();
        ButtonElement be1 = Document.get().createPushButtonElement();
        e.appendChild(be0);
        e.appendChild(JsoUtils.newText("test1", Document.get()));
        e.appendChild(be1);
        e.appendChild(JsoUtils.newText("test2", Document.get()));

        // When & Then
        assertThat(be0.getNextSiblingElement()).isEqualTo(be1);
        assertThat(be1.getNextSiblingElement()).isNull();
    }

    @Test
    public void getOffset() {
        // Given
        Element parent = Document.get().createElement("a");
        parent.appendChild(e);

        // When & Then
        assertThat(e.getOffsetHeight()).isEqualTo(0);
        assertThat(e.getOffsetLeft()).isEqualTo(0);
        assertThat(e.getOffsetTop()).isEqualTo(0);
        assertThat(e.getOffsetWidth()).isEqualTo(0);
        assertThat(e.getOffsetParent()).isEqualTo(parent);
    }

    @Test
    public void getParentElement() {
        // Given
        Element otherParent = Document.get().createDivElement();
        Element child = Document.get().createBaseElement();
        e.appendChild(child);

        // When and assert
        assertThat(child.getParentElement()).isEqualTo(e);

        // When 2
        otherParent.appendChild(child);

        // Then 2
        assertThat(e.hasChildNodes()).isFalse();
    }

    @Test
    public void hasAttribute() {
        // Given
        e.setAttribute("myAttr", "value");

        // When & Then
        assertThat(e.hasAttribute("myAttr")).isTrue();
    }

    @Test
    public void id() {
        // When 1
        e.setId("myId");

        // Then 1
        assertThat(e.getId()).isEqualTo("myId");
        assertThat(e.getAttribute("id")).isEqualTo("myId");

        // When 2
        e.setAttribute("id", "updatedId");

        // Then 2
        assertThat(e.getId()).isEqualTo("updatedId");
        assertThat(e.getAttribute("id")).isEqualTo("updatedId");
    }

    @Test
    public void innerHTML() {
        // When
        e.setInnerHTML("<h1>test</h1>");

        // Then
        assertThat(e.getInnerHTML()).isEqualTo("<h1>test</h1>");
        assertThat(e.getChildCount()).isEqualTo(1);
        HeadingElement h1 = e.getChild(0).cast();
        assertThat(h1.getTagName()).isEqualTo("H1");
        assertThat(h1.getInnerText()).isEqualTo("test");
    }

    @Test
    public void innerText() {
        // When
        e.setInnerText("myText");

        // Then
        assertThat(e.getInnerText()).isEqualTo("myText");
    }

    @Test
    public void isOrHasChild() {
        // Given
        AnchorElement child = Document.get().createAnchorElement();
        e.appendChild(child);
        AnchorElement notAChild = Document.get().createAnchorElement();

        // When & Then
        assertThat(e.isOrHasChild(e)).isTrue();
        assertThat(e.isOrHasChild(child)).isTrue();
        assertThat(e.isOrHasChild(notAChild)).isFalse();
    }

    @Test
    public void lang() {
        // When
        e.setLang("myLang");

        // Then
        assertThat(e.getLang()).isEqualTo("myLang");
    }

    @Test
    public void propertyBoolean_False() {
        // When
        e.setPropertyBoolean("prop", false);

        // Then
        assertThat(e.getPropertyBoolean("prop")).isFalse();
    }

    @Test
    public void propertyBoolean_True() {
        // Preconditions
        assertThat(e.getPropertyBoolean("prop")).isFalse();
        // When
        e.setPropertyBoolean("prop", true);

        // Then
        assertThat(e.getPropertyBoolean("prop")).isTrue();
    }

    @Test
    public void propertyDouble() {
        // Preconditions
        assertThat((Double) e.getPropertyDouble("prop")).isEqualTo(new Double(0));

        // When
        e.setPropertyDouble("prop", 23);

        // Then
        assertThat((Double) e.getPropertyDouble("prop")).isEqualTo(new Double(23));
    }

    @Test
    public void propertyInt() {
        // Preconditions
        assertThat(e.getPropertyInt("prop")).isEqualTo(0);

        // When
        e.setPropertyInt("prop", 2);

        // Then
        assertThat(e.getPropertyInt("prop")).isEqualTo(2);
    }

    @Test
    public void propertyString() {
        // Preconditions
        assertThat(e.getPropertyString("prop")).isNull();

        // When
        e.setPropertyString("prop", "test");

        // Then
        assertThat(e.getPropertyString("prop")).isEqualTo("test");
    }

    @Test
    public void removeAttribute() {
        // Given
        e.setAttribute("test", "value");

        // When
        e.removeAttribute("Test");

        // Then
        assertThat(e.getAttribute("test")).as("Removed attribute should return emptyString").isEqualTo("");
    }

    @Test
    public void scrollLeft() {
        // Preconditions
        assertThat(e.getScrollLeft()).isEqualTo(0);

        // When
        e.setScrollLeft(3);

        // Then
        assertThat(e.getScrollLeft()).isEqualTo(3);
    }

    @Test
    public void scrollTop() {
        // Preconditions
        assertThat(e.getScrollTop()).isEqualTo(0);

        // When
        e.setScrollTop(3);
        assertThat(e.getScrollTop()).isEqualTo(3);
    }

    @Test
    public void style() {
        // When
        e.getStyle().setProperty("test", "value");

        // Then
        assertThat(e.getStyle().getProperty("test")).isEqualTo("value");
    }

    @Test
    public void tagName() {
        // When & Then
        assertThat(e.getTagName()).isEqualTo("div");
        assertThat(e.getAttribute("tagName")).isEqualTo("");
        assertThat(e.getAttribute("TAGNAME")).isEqualTo("");
        assertThat(e.getPropertyString("tagName")).isEqualTo("DIV");
        assertThat(e.getPropertyString("TAGNAME")).isNull();
    }

    @Test
    public void title() {
        // Preconditions
        assertThat(e.getTitle()).isEqualTo("");
        assertThat(e.getAttribute("title")).isEqualTo("");
        assertThat(e.getAttribute("titLe")).isEqualTo("");
        assertThat(e.getPropertyString("title")).isEqualTo("");
        assertThat(e.getPropertyString("titLe")).isNull();
        assertThat(e.getPropertyObject("title")).isNull();
        assertThat(e.getPropertyObject("titLe")).isNull();

        // When
        e.setTitle("MyTitle");

        // Then
        assertThat(e.getTitle()).isEqualTo("MyTitle");
        assertThat(e.getAttribute("title")).isEqualTo("MyTitle");
        assertThat(e.getAttribute("titLe")).isEqualTo("MyTitle");
        assertThat(e.getPropertyString("title")).isEqualTo("MyTitle");
        assertThat(e.getPropertyString("titLe")).isNull();
        assertThat(e.getPropertyObject("title")).isEqualTo("MyTitle");
        assertThat(e.getPropertyObject("titLe")).isNull();
    }

}

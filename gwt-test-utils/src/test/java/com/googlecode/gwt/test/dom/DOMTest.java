package com.googlecode.gwt.test.dom;

import com.google.gwt.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Image;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.junit.Assert.*;

public class DOMTest extends GwtTestTest {

    @Test
    public void checkImageSrc() {
        // Arrange
        Image img = new Image();
        ImageElement elem = img.getElement().cast();

        // Act
        DOM.setImgSrc(img.getElement(), "http://test/image.gif");
        String imageSrc = DOM.getImgSrc(img.getElement());

        // Assert
        assertEquals("http://test/image.gif", elem.getSrc());
        assertEquals("http://test/image.gif", imageSrc);
    }

    @Test
    public void createAnchor() {
        // Act
        AnchorElement elem = AnchorElement.as(DOM.createAnchor());

        // Assert
        assertEquals("a", elem.getTagName());
    }

    @Test
    public void createButton() {
        // Act
        ButtonElement elem = ButtonElement.as(DOM.createButton());

        // Assert
        assertEquals("button", elem.getTagName());
    }

    @Test
    public void createCaption() {
        // Act
        TableCaptionElement elem = TableCaptionElement.as(DOM.createCaption());

        // Assert
        assertEquals("caption", elem.getTagName());
    }

    @Test
    public void createCol() {
        // Act
        TableColElement elem = TableColElement.as(DOM.createCol());

        // Assert
        assertEquals("col", elem.getTagName());
    }

    @Test
    public void createColGroup() {
        // Act
        TableColElement elem = TableColElement.as(DOM.createColGroup());

        // Assert
        assertEquals("colgroup", elem.getTagName());
    }

    @Test
    public void createDiv() {
        // Act
        DivElement elem = DivElement.as(DOM.createDiv());

        // Assert
        assertEquals("div", elem.getTagName());
    }

    @Test
    public void createElement() {
        // Act
        DivElement elem = DivElement.as(DOM.createElement("div"));

        // Assert
        assertEquals("div", elem.getTagName());
    }

    @Test
    public void createFieldSet() {
        // Act
        FieldSetElement elem = FieldSetElement.as(DOM.createFieldSet());

        // Assert
        assertEquals("fieldset", elem.getTagName());
    }

    @Test
    public void createForm() {
        // Act
        FormElement elem = FormElement.as(DOM.createForm());

        // Assert
        assertEquals("form", elem.getTagName());
    }

    @Test
    public void createIFrame() {
        // Act
        IFrameElement elem = IFrameElement.as(DOM.createIFrame());

        // Assert
        assertEquals("iframe", elem.getTagName());
    }

    @Test
    public void createImg() {
        // Act
        ImageElement elem = ImageElement.as(DOM.createImg());

        // Assert
        assertEquals("img", elem.getTagName());
    }

    @Test
    public void createInputCheck() {
        // Act
        InputElement elem = InputElement.as(DOM.createInputCheck());

        // Assert
        assertEquals("input", elem.getTagName());
        assertEquals("checkbox", elem.getType());
    }

    @Test
    public void createInputPassword() {
        // Act
        InputElement elem = InputElement.as(DOM.createInputPassword());

        // Assert
        assertEquals("input", elem.getTagName());
        assertEquals("password", elem.getType());
    }

    @Test
    public void createInputRadio() {
        // Act
        InputElement elem = InputElement.as(DOM.createInputRadio("test"));

        // Assert
        assertEquals("input", elem.getTagName());
        assertEquals("test", elem.getName());
    }

    @Test
    public void createInputText() {
        // Act
        InputElement elem = InputElement.as(DOM.createInputText());

        // Assert
        assertEquals("input", elem.getTagName());
        assertEquals("text", elem.getType());
    }

    @Test
    public void createLabel() {
        // Act
        LabelElement elem = LabelElement.as(DOM.createLabel());

        // Assert
        assertEquals("label", elem.getTagName());
    }

    @Test
    public void createLegend() {
        // Act
        LegendElement elem = LegendElement.as(DOM.createLegend());

        // Assert
        assertEquals("legend", elem.getTagName());
    }

    @Test
    public void createOption() {
        // Act
        OptionElement elem = OptionElement.as(DOM.createOption());

        // Assert
        assertEquals("option", elem.getTagName());
    }

    @Test
    public void createSelect() {
        // Act
        SelectElement elem = SelectElement.as(DOM.createSelect());

        // Assert
        assertEquals("select", elem.getTagName());
        assertFalse("Simple SelectElement should not be multiple", elem.isMultiple());
    }

    @Test
    public void createSelectMultiple() {
        // Act
        SelectElement elem = SelectElement.as(DOM.createSelect(true));

        // Assert
        assertEquals("select", elem.getTagName());
        assertTrue("SelectElement should be multiple", elem.isMultiple());
    }

    @Test
    public void createSpan() {
        // Act
        SpanElement elem = SpanElement.as(DOM.createSpan());

        // Assert
        assertEquals("span", elem.getTagName());
    }

    @Test
    public void createTable() {
        // Act
        TableElement elem = TableElement.as(DOM.createTable());

        // Assert
        assertEquals("table", elem.getTagName());
    }

    @Test
    public void createTBody() {
        // Act
        TableSectionElement elem = TableSectionElement.as(DOM.createTBody());

        // Assert
        assertEquals("tbody", elem.getTagName());
    }

    @Test
    public void createTD() {
        // Act
        TableCellElement elem = TableCellElement.as(DOM.createTD());

        // Assert
        assertEquals("td", elem.getTagName());
    }

    @Test
    public void createTextArea() {
        // Act
        TextAreaElement elem = TextAreaElement.as(DOM.createTextArea());

        // Assert
        assertEquals("textarea", elem.getTagName());
    }

    @Test
    public void createTFoot() {
        // Act
        TableSectionElement elem = TableSectionElement.as(DOM.createTFoot());

        // Assert
        assertEquals("tfoot", elem.getTagName());
    }

    @Test
    public void createTH() {
        // Act
        TableCellElement elem = TableCellElement.as(DOM.createTH());

        // Assert
        assertEquals("th", elem.getTagName());
    }

    @Test
    public void createTHead() {
        // Act
        TableSectionElement elem = TableSectionElement.as(DOM.createTHead());

        // Assert
        assertEquals("thead", elem.getTagName());
    }

    @Test
    public void createTR() {
        // Act
        TableRowElement elem = TableRowElement.as(DOM.createTR());

        // Assert
        assertEquals("tr", elem.getTagName());
    }

}

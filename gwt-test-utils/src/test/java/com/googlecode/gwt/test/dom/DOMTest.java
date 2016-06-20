package com.googlecode.gwt.test.dom;

import com.google.gwt.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Image;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DOMTest extends GwtTestTest {

    @Test
    public void checkImageSrc() {
        // Given
        Image img = new Image();
        ImageElement elem = img.getElement().cast();

        // When
        DOM.setImgSrc(img.getElement(), "http://test/image.gif");
        String imageSrc = DOM.getImgSrc(img.getElement());

        // Then
        assertThat(elem.getSrc()).isEqualTo("http://test/image.gif");
        assertThat(imageSrc).isEqualTo("http://test/image.gif");
    }

    @Test
    public void createAnchor() {
        // When
        AnchorElement elem = AnchorElement.as(DOM.createAnchor());

        // Then
        assertThat(elem.getTagName()).isEqualTo("a");
    }

    @Test
    public void createButton() {
        // When
        ButtonElement elem = ButtonElement.as(DOM.createButton());

        // Then
        assertThat(elem.getTagName()).isEqualTo("button");
    }

    @Test
    public void createCaption() {
        // When
        TableCaptionElement elem = TableCaptionElement.as(DOM.createCaption());

        // Then
        assertThat(elem.getTagName()).isEqualTo("caption");
    }

    @Test
    public void createCol() {
        // When
        TableColElement elem = TableColElement.as(DOM.createCol());

        // Then
        assertThat(elem.getTagName()).isEqualTo("col");
    }

    @Test
    public void createColGroup() {
        // When
        TableColElement elem = TableColElement.as(DOM.createColGroup());

        // Then
        assertThat(elem.getTagName()).isEqualTo("colgroup");
    }

    @Test
    public void createDiv() {
        // When
        DivElement elem = DivElement.as(DOM.createDiv());

        // Then
        assertThat(elem.getTagName()).isEqualTo("div");
    }

    @Test
    public void createElement() {
        // When
        DivElement elem = DivElement.as(DOM.createElement("div"));

        // Then
        assertThat(elem.getTagName()).isEqualTo("div");
    }

    @Test
    public void createFieldSet() {
        // When
        FieldSetElement elem = FieldSetElement.as(DOM.createFieldSet());

        // Then
        assertThat(elem.getTagName()).isEqualTo("fieldset");
    }

    @Test
    public void createForm() {
        // When
        FormElement elem = FormElement.as(DOM.createForm());

        // Then
        assertThat(elem.getTagName()).isEqualTo("form");
    }

    @Test
    public void createIFrame() {
        // When
        IFrameElement elem = IFrameElement.as(DOM.createIFrame());

        // Then
        assertThat(elem.getTagName()).isEqualTo("iframe");
    }

    @Test
    public void createImg() {
        // When
        ImageElement elem = ImageElement.as(DOM.createImg());

        // Then
        assertThat(elem.getTagName()).isEqualTo("img");
    }

    @Test
    public void createInputCheck() {
        // When
        InputElement elem = InputElement.as(DOM.createInputCheck());

        // Then
        assertThat(elem.getTagName()).isEqualTo("input");
        assertThat(elem.getType()).isEqualTo("checkbox");
    }

    @Test
    public void createInputPassword() {
        // When
        InputElement elem = InputElement.as(DOM.createInputPassword());

        // Then
        assertThat(elem.getTagName()).isEqualTo("input");
        assertThat(elem.getType()).isEqualTo("password");
    }

    @Test
    public void createInputRadio() {
        // When
        InputElement elem = InputElement.as(DOM.createInputRadio("test"));

        // Then
        assertThat(elem.getTagName()).isEqualTo("input");
        assertThat(elem.getName()).isEqualTo("test");
    }

    @Test
    public void createInputText() {
        // When
        InputElement elem = InputElement.as(DOM.createInputText());

        // Then
        assertThat(elem.getTagName()).isEqualTo("input");
        assertThat(elem.getType()).isEqualTo("text");
    }

    @Test
    public void createLabel() {
        // When
        LabelElement elem = LabelElement.as(DOM.createLabel());

        // Then
        assertThat(elem.getTagName()).isEqualTo("label");
    }

    @Test
    public void createLegend() {
        // When
        LegendElement elem = LegendElement.as(DOM.createLegend());

        // Then
        assertThat(elem.getTagName()).isEqualTo("legend");
    }

    @Test
    public void createOption() {
        // When
        OptionElement elem = OptionElement.as(DOM.createOption());

        // Then
        assertThat(elem.getTagName()).isEqualTo("option");
    }

    @Test
    public void createSelect() {
        // When
        SelectElement elem = SelectElement.as(DOM.createSelect());

        // Then
        assertThat(elem.getTagName()).isEqualTo("select");
        assertThat(elem.isMultiple()).as("Simple SelectElement should not be multiple").isFalse();
    }

    @Test
    public void createSelectMultiple() {
        // When
        SelectElement elem = SelectElement.as(DOM.createSelect(true));

        // Then
        assertThat(elem.getTagName()).isEqualTo("select");
        assertThat(elem.isMultiple()).as("SelectElement should be multiple").isTrue();
    }

    @Test
    public void createSpan() {
        // When
        SpanElement elem = SpanElement.as(DOM.createSpan());

        // Then
        assertThat(elem.getTagName()).isEqualTo("span");
    }

    @Test
    public void createTable() {
        // When
        TableElement elem = TableElement.as(DOM.createTable());

        // Then
        assertThat(elem.getTagName()).isEqualTo("table");
    }

    @Test
    public void createTBody() {
        // When
        TableSectionElement elem = TableSectionElement.as(DOM.createTBody());

        // Then
        assertThat(elem.getTagName()).isEqualTo("tbody");
    }

    @Test
    public void createTD() {
        // When
        TableCellElement elem = TableCellElement.as(DOM.createTD());

        // Then
        assertThat(elem.getTagName()).isEqualTo("td");
    }

    @Test
    public void createTextArea() {
        // When
        TextAreaElement elem = TextAreaElement.as(DOM.createTextArea());

        // Then
        assertThat(elem.getTagName()).isEqualTo("textarea");
    }

    @Test
    public void createTFoot() {
        // When
        TableSectionElement elem = TableSectionElement.as(DOM.createTFoot());

        // Then
        assertThat(elem.getTagName()).isEqualTo("tfoot");
    }

    @Test
    public void createTH() {
        // When
        TableCellElement elem = TableCellElement.as(DOM.createTH());

        // Then
        assertThat(elem.getTagName()).isEqualTo("th");
    }

    @Test
    public void createTHead() {
        // When
        TableSectionElement elem = TableSectionElement.as(DOM.createTHead());

        // Then
        assertThat(elem.getTagName()).isEqualTo("thead");
    }

    @Test
    public void createTR() {
        // When
        TableRowElement elem = TableRowElement.as(DOM.createTR());

        // Then
        assertThat(elem.getTagName()).isEqualTo("tr");
    }

}

package com.googlecode.gwt.test;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.googlecode.gwt.test.utils.events.Browser;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("deprecation")
public class FlexTableTest extends GwtTestTest {

    private boolean clicked = false;

    @Test
    public void click_ClickHandler_NestedWidget() {
        // Given
        clicked = false;
        FlexTable t = new FlexTable();

        Button b = new Button("Wide Button");
        b.addClickHandler(event -> clicked = !clicked);
        // add the button
        t.setWidget(0, 0, b);

        // Preconditions
        assertThat(clicked).isEqualTo(false);

        // When
        Browser.click(t.getWidget(0, 0));

        // Then
        assertThat(clicked).isEqualTo(true);
    }

    @Test
    public void click_ClickkListener_NestedWidget() {
        // Given
        clicked = false;
        FlexTable t = new FlexTable();

        Button b = new Button("Wide Button");
        b.addClickListener(sender -> clicked = !clicked);
        // add the button
        t.setWidget(0, 0, b);

        // Preconditions
        assertThat(clicked).isEqualTo(false);

        // When
        Browser.click(t.getWidget(0, 0));

        // Then
        assertThat(clicked).isEqualTo(true);
    }

    @Test
    public void html() {
        // Given
        FlexTable t = new FlexTable();

        // When
        t.setHTML(1, 1, "<h1>test</h1>");

        // Then
        assertThat(t.getHTML(1, 1)).isEqualTo("<h1>test</h1>");
        Element e = t.getCellFormatter().getElement(1, 1);
        assertThat(e.getChildCount()).isEqualTo(1);
        HeadingElement h1 = e.getChild(0).cast();
        assertThat(h1.getTagName()).isEqualTo("H1");
        assertThat(h1.getInnerText()).isEqualTo("test");
    }

    @Test
    public void innerFlexTable() {
        FlexTable t = new FlexTable();
        Label label1 = new Label("1st label");
        t.setWidget(0, 0, label1);
        FlexTable innerTable = new FlexTable();
        innerTable.setWidget(0, 4, new Label());
        innerTable.setWidget(0, 6, new TextBox());
        t.setWidget(0, 1, innerTable);
        Label label2 = new Label("2nd label");
        t.setWidget(0, 2, label2);

        assertThat(t.getWidget(0, 0)).isEqualTo(label1);
        assertThat(t.getWidget(0, 2)).isEqualTo(label2);
    }

    @Test
    public void setText_setWidget() {
        // Tables have no explicit size -- they resize automatically on demand.
        FlexTable t = new FlexTable();

        // Put some text at the table's extremes. This forces the table to be
        // 3 by 3.
        t.setText(0, 0, "upper-left corner");
        t.setText(2, 2, "bottom-right corner");

        // Let's put a button in the middle...
        Button b = new Button("Wide Button");
        t.setWidget(1, 0, b);

        // ...and set it's column span so that it takes up the whole row.
        t.getFlexCellFormatter().setColSpan(1, 0, 3);

        // Then
        assertThat(t.getRowCount()).isEqualTo(3);
        assertThat(t.getText(2, 2)).isEqualTo("bottom-right corner");
        assertThat(t.getWidget(1, 0)).isSameAs(b);
    }

    @Test
    public void text() {
        // Given
        FlexTable t = new FlexTable();

        // When
        t.setText(1, 1, "text");

        // Then
        assertThat(t.getText(1, 1)).isEqualTo("text");
    }

    @Test
    public void title() {
        // Given
        FlexTable t = new FlexTable();

        // When
        t.setTitle("title");

        // Then
        assertThat(t.getTitle()).isEqualTo("title");
    }

    @Test
    public void visible() {
        // Given
        FlexTable t = new FlexTable();
        // Preconditions
        assertThat(t.isVisible()).isEqualTo(true);

        // When
        t.setVisible(false);

        // Then
        assertThat(t.isVisible()).isEqualTo(false);
    }

}

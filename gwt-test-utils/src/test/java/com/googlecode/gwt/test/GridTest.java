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
public class GridTest extends GwtTestTest {

    private boolean clicked = false;

    @Test
    public void addStyleName() {
        // Given
        // Grids must be sized explicitly, though they can be resized later.
        Grid g = new Grid(1, 1);

        // When
        g.getRowFormatter().addStyleName(0, "style");

        // Then
        assertThat(g.getRowFormatter().getStyleName(0)).isEqualTo("style");
    }

    @Test
    public void click_ClickHander() {
        // Given
        clicked = false;
        final Grid g = new Grid(1, 1);
        final Button b = new Button("Does nothing, but could");
        g.setWidget(0, 0, b);
        g.addClickHandler(event -> {
            clicked = !clicked;
            assertThat(((Grid) event.getSource()).getWidget(0, 0)).isSameAs(b);
        });

        // When
        Browser.click(g, 0, 0);

        // Then
        assertThat(clicked).as("TableListener should have been notified").isTrue();

    }

    @Test
    public void click_ClickHandler_NestedWidget() {
        // Given
        // Grids must be sized explicitly, though they can be resized later.
        Grid g = new Grid(1, 1);

        Button b = new Button();

        b.addClickHandler(event -> clicked = !clicked);
        // add the button
        g.setWidget(0, 0, b);

        // Preconditions
        assertThat(clicked).isEqualTo(false);

        // When
        Browser.click(g.getWidget(0, 0));

        // Then
        assertThat(clicked).isEqualTo(true);
    }

    @Test
    public void click_ClickListener_NestedWidget() {
        // Given
        // Grids must be sized explicitly, though they can be resized later.
        Grid g = new Grid(1, 1);

        Button b = new Button();

        b.addClickListener(sender -> clicked = !clicked);
        // add the button
        g.setWidget(0, 0, b);

        // Preconditions
        assertThat(clicked).isEqualTo(false);

        // When
        Browser.click(g.getWidget(0, 0));

        // Then
        assertThat(clicked).isEqualTo(true);
    }

    @Test
    public void click_TableListner() {
        // Given
        clicked = false;
        Grid g = new Grid(1, 1);
        Button b = new Button("Does nothing, but could");
        g.setWidget(0, 0, b);
        g.addTableListener((sender, row, cell) -> clicked = !clicked);

        // When
        Browser.click(g, 0, 0);

        // Then
        assertThat(clicked).as("TableListener should have been notified").isTrue();
    }

    @Test
    public void html() {
        // Given
        Grid g = new Grid(1, 1);

        // When
        g.setHTML(0, 0, "<h1>test</h1>");

        // Then
        assertThat(g.getHTML(0, 0)).isEqualTo("<h1>test</h1>");
        Element e = g.getCellFormatter().getElement(0, 0);
        assertThat(e.getChildCount()).isEqualTo(1);
        HeadingElement h1 = e.getChild(0).cast();
        assertThat(h1.getTagName()).isEqualTo("H1");
        assertThat(h1.getInnerText()).isEqualTo("test");
    }

    @Test
    public void removeFromGrid() {
        // Given
        // Grids must be sized explicitly, though they can be resized later.
        Grid g = new Grid(1, 1);
        Button b = new Button("Does nothing, but could");
        g.setWidget(0, 0, b);

        // When & Then
        assertThat(g.remove(b)).as("The button has not been removed from grid").isTrue();
    }

    @Test
    public void resizeRow() {
        // Given
        Grid g = new Grid(1, 1);
        g.setWidget(0, 0, new Label("first"));
        // Preconditions
        assertThat(g.getHTML(0, 0)).isEqualTo("<div class=\"gwt-Label\">first</div>");

        // When
        g.resize(2, 2);

        // Then
        assertThat(g.getHTML(0, 0)).isEqualTo("<div class=\"gwt-Label\">first</div>");
        assertThat(g.getHTML(0, 1)).isEqualTo("&nbsp;");
        assertThat(g.getHTML(1, 0)).isEqualTo("&nbsp;");
        assertThat(g.getHTML(1, 1)).isEqualTo("&nbsp;");
    }

    @Test
    public void setText() {
        // Given
        // Grids must be sized explicitly, though they can be resized later.
        Grid g = new Grid(5, 5);

        // Put some values in the grid cells.
        for (int row = 0; row < 5; ++row) {
            for (int col = 0; col < 5; ++col)
                // When
                g.setText(row, col, "" + row + ", " + col);
        }

        // Then
        assertThat(g.getText(0, 0)).isEqualTo("0, 0");
        assertThat(g.getText(3, 2)).isEqualTo("3, 2");
        assertThat(g.getText(4, 4)).isEqualTo("4, 4");
    }

    @Test
    public void setWidget() {
        // Given
        Grid g = new Grid(3, 3);
        Button b = new Button("Does nothing, but could");

        // When
        g.setWidget(2, 2, b);

        // Then
        assertThat(g.getWidget(2, 2)).isSameAs(b);
    }

    @Test
    public void text() {
        // Given
        Grid g = new Grid(1, 1);

        // When
        g.setText(0, 0, "text");

        // Then
        assertThat(g.getText(0, 0)).isEqualTo("text");
    }

    @Test
    public void title() {
        // Given
        Grid g = new Grid(1, 1);

        // When
        g.setTitle("title");

        // Then
        assertThat(g.getTitle()).isEqualTo("title");
    }

    @Test
    public void visible() {
        // Given
        Grid g = new Grid(1, 1);
        // Preconditions
        assertThat(g.isVisible()).isEqualTo(true);

        // When
        g.setVisible(false);

        // Then
        assertThat(g.isVisible()).isEqualTo(false);
    }

}

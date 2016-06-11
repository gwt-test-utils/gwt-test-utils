package com.googlecode.gwt.test;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Hyperlink;
import com.googlecode.gwt.test.utils.events.Browser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HyperlinkTest extends GwtTestTest {

    private Boolean bool = false;

    @Test
    public void checkVisible() {
        // Act
        Hyperlink link = new Hyperlink();
        // Pre-Assert
        assertEquals(true, link.isVisible());

        // Act
        link.setVisible(false);

        // Assert
        assertEquals(false, link.isVisible());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void click_ClickHandler() {
        // Arrange
        bool = false;
        Hyperlink link = new Hyperlink();
        link.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                bool = true;
            }

        });

        // Act
        Browser.click(link);

        // Assert
        assertEquals(true, bool);
    }

    @Test
    public void constructor_HTML_Token() {
        // Act
        Hyperlink link = new Hyperlink("<h1>foo</h1>", true, "test-history-token");

        // Assert
        assertEquals("test-history-token", link.getTargetHistoryToken());
        assertEquals("<h1>foo</h1>", link.getHTML());

    }

    @Test
    public void constructor_Text_Token() {
        // Act
        Hyperlink link = new Hyperlink("test-text", "test-history-token");

        // Assert
        assertEquals("test-text", link.getText());
        assertEquals("test-history-token", link.getTargetHistoryToken());
    }

    @Test
    public void html() {
        // Arrange
        Hyperlink link = new Hyperlink();

        // Act
        link.setHTML("<h1>test</h1>");

        // Assert
        assertEquals("<h1>test</h1>", link.getHTML());
        assertEquals(1, link.getElement().getChild(0).getChildCount());
        HeadingElement h1 = link.getElement().getChild(0).getChild(0).cast();
        assertEquals("H1", h1.getTagName());
        assertEquals("test", h1.getInnerText());
    }

    @Test
    public void title() {
        // Act
        Hyperlink link = new Hyperlink();

        // Act
        link.setTitle("title");

        // Assert
        assertEquals("title", link.getTitle());
    }

}

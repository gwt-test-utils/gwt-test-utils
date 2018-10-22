package com.googlecode.gwt.test;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Hyperlink;
import com.googlecode.gwt.test.utils.events.Browser;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HyperlinkTest extends GwtTestTest {

    private Boolean bool = false;

    @Test
    public void checkVisible() {
        // When
        Hyperlink link = new Hyperlink();
        // Preconditions
        assertThat(link.isVisible()).isEqualTo(true);

        // When
        link.setVisible(false);

        // Then
        assertThat(link.isVisible()).isEqualTo(false);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void click_ClickHandler() {
        // Given
        bool = false;
        Hyperlink link = new Hyperlink();
        link.addClickHandler(event -> bool = true);

        // When
        Browser.click(link);

        // Then
        assertThat(bool).isEqualTo(true);
    }

    @Test
    public void constructor_HTML_Token() {
        // When
        Hyperlink link = new Hyperlink("<h1>foo</h1>", true, "test-history-token");

        // Then
        assertThat(link.getTargetHistoryToken()).isEqualTo("test-history-token");
        assertThat(link.getHTML()).isEqualTo("<h1>foo</h1>");

    }

    @Test
    public void constructor_Text_Token() {
        // When
        Hyperlink link = new Hyperlink("test-text", "test-history-token");

        // Then
        assertThat(link.getText()).isEqualTo("test-text");
        assertThat(link.getTargetHistoryToken()).isEqualTo("test-history-token");
    }

    @Test
    public void html() {
        // Given
        Hyperlink link = new Hyperlink();

        // When
        link.setHTML("<h1>test</h1>");

        // Then
        assertThat(link.getHTML()).isEqualTo("<h1>test</h1>");
        assertThat(link.getElement().getChild(0).getChildCount()).isEqualTo(1);
        HeadingElement h1 = link.getElement().getChild(0).getChild(0).cast();
        assertThat(h1.getTagName()).isEqualTo("H1");
        assertThat(h1.getInnerText()).isEqualTo("test");
    }

    @Test
    public void title() {
        // When
        Hyperlink link = new Hyperlink();

        // When
        link.setTitle("title");

        // Then
        assertThat(link.getTitle()).isEqualTo("title");
    }

}

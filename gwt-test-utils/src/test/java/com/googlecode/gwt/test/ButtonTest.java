package com.googlecode.gwt.test;

import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("deprecation")
public class ButtonTest extends GwtTestTest {

    private Button b;

    @Before
    public void beforeButtonTest() {
        // create the button in a standard JVM
        b = new Button();

        // needs to be attached
        RootPanel.get().add(b);
    }

    @Test
    public void checkToString() {
        // Arrange
        b.setHTML("test button");
        b.setEnabled(false);
        b.setFocus(false);
        b.setAccessKey('h');
        b.setStyleName("my-style");

        // Act
        String toString = b.toString();

        // Assert
        assertEquals(
                "<button type=\"button\" class=\"my-style\" disabled=\"\" accessKey=\"h\">test button</button>",
                toString);
    }

    @Test
    public void click_ClickHandler() {
        // Arrange
        // add a handler to test the click
        b.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                b.setHTML("clicked");
            }

        });

        // Pre-Assert
        assertEquals("", b.getHTML());

        // Act
        b.click();

        // Assert
        assertEquals("clicked", b.getHTML());
    }

    @Test
    public void click_ClickListener() {
        // Arrange
        b.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                b.setHTML("clicked");

            }
        });

        // Pre-Assert
        assertEquals("", b.getHTML());

        // Act
        b.click();

        // Assert
        assertEquals("clicked", b.getHTML());
    }

    @Test
    public void enabled() {
        // Pre-Assert
        assertEquals(true, b.isEnabled());

        // Act
        b.setEnabled(false);

        // Assert
        assertEquals(false, b.isEnabled());
    }

    @Test
    public void html() {
        // Pre-Assert
        assertEquals("", b.getHTML());

        // Act
        b.setHTML("test-html");

        // Assert
        assertEquals("test-html", b.getHTML());
    }

    @Test
    public void styleName() {
        // Pre-Assert
        assertEquals("gwt-Button", b.getStyleName());

        // Act
        b.setStyleName("test-button-style");

        // Assert
        assertEquals("test-button-style", b.getStyleName());
    }

    @Test
    public void stylePrimaryName() {
        // Act
        b.setStylePrimaryName("test-button-styleP");

        // Assert
        assertEquals("test-button-styleP", b.getStylePrimaryName());
    }

    @Test
    public void text() {
        // Act
        b.setText("toto");

        // Assert
        assertEquals("toto", b.getText());
    }

    @Test
    public void title() {
        // Act
        b.setTitle("title");

        // Assert
        assertEquals("title", b.getTitle());
    }

    @Test
    public void visible() {
        // Pre-Assert
        assertEquals(true, b.isVisible());

        // Act
        b.setVisible(false);

        // Assert
        assertEquals(false, b.isVisible());
    }

    @Test
    public void wrap() {
        // Arrange
        ButtonElement element = Document.get().createPushButtonElement();
        element.setTabIndex(3);
        Document.get().getBody().appendChild(element);

        // Act
        Button b = Button.wrap(element);

        // Assert 1
        assertEquals(3, b.getTabIndex());

        // Act 2
        b.setTabIndex(1);

        // Assert 2
        assertEquals(1, element.getTabIndex());
    }

}

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

import static org.assertj.core.api.Assertions.assertThat;

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
        // Given
        b.setHTML("test button");
        b.setEnabled(false);
        b.setFocus(false);
        b.setAccessKey('h');
        b.setStyleName("my-style");

        // When
        String toString = b.toString();

        // Then
        assertThat(toString).isEqualTo("<button type=\"button\" class=\"my-style\" disabled=\"\" accessKey=\"h\">test button</button>");
    }

    @Test
    public void click_ClickHandler() {
        // Given
        // add a handler to test the click
        b.addClickHandler(event -> b.setHTML("clicked"));

        // Preconditions
        assertThat(b.getHTML()).isEqualTo("");

        // When
        b.click();

        // Then
        assertThat(b.getHTML()).isEqualTo("clicked");
    }

    @Test
    public void click_ClickListener() {
        // Given
        b.addClickListener(sender -> b.setHTML("clicked"));

        // Preconditions
        assertThat(b.getHTML()).isEqualTo("");

        // When
        b.click();

        // Then
        assertThat(b.getHTML()).isEqualTo("clicked");
    }

    @Test
    public void enabled() {
        // Preconditions
        assertThat(b.isEnabled()).isEqualTo(true);

        // When
        b.setEnabled(false);

        // Then
        assertThat(b.isEnabled()).isEqualTo(false);
    }

    @Test
    public void html() {
        // Preconditions
        assertThat(b.getHTML()).isEqualTo("");

        // When
        b.setHTML("test-html");

        // Then
        assertThat(b.getHTML()).isEqualTo("test-html");
    }

    @Test
    public void styleName() {
        // Preconditions
        assertThat(b.getStyleName()).isEqualTo("gwt-Button");

        // When
        b.setStyleName("test-button-style");

        // Then
        assertThat(b.getStyleName()).isEqualTo("test-button-style");
    }

    @Test
    public void stylePrimaryName() {
        // When
        b.setStylePrimaryName("test-button-styleP");

        // Then
        assertThat(b.getStylePrimaryName()).isEqualTo("test-button-styleP");
    }

    @Test
    public void text() {
        // When
        b.setText("toto");

        // Then
        assertThat(b.getText()).isEqualTo("toto");
    }

    @Test
    public void title() {
        // When
        b.setTitle("title");

        // Then
        assertThat(b.getTitle()).isEqualTo("title");
    }

    @Test
    public void visible() {
        // Preconditions
        assertThat(b.isVisible()).isEqualTo(true);

        // When
        b.setVisible(false);

        // Then
        assertThat(b.isVisible()).isEqualTo(false);
    }

    @Test
    public void wrap() {
        // Given
        ButtonElement element = Document.get().createPushButtonElement();
        element.setTabIndex(3);
        Document.get().getBody().appendChild(element);

        // When
        Button b = Button.wrap(element);

        // Then 1
        assertThat(b.getTabIndex()).isEqualTo(3);

        // When 2
        b.setTabIndex(1);

        // Then 2
        assertThat(element.getTabIndex()).isEqualTo(1);
    }

}

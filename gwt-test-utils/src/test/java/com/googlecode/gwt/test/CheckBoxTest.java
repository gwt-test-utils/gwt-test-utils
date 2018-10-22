package com.googlecode.gwt.test;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.test.utils.events.Browser;
import com.googlecode.gwt.test.utils.events.Browser.BrowserErrorHandler;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckBoxTest extends GwtTestTest {

    private final StringBuilder errorStringBuilder = new StringBuilder();

    @Before
    public void beforeCheckBoxTest() {
        errorStringBuilder.delete(0, errorStringBuilder.length());

        setBrowserErrorHandler(errorStringBuilder::append);
    }

    @Test
    public void checked() {
        // Given
        CheckBox cb = new CheckBox();
        // Preconditions
        assertThat(cb.getValue()).isEqualTo(false);

        // When
        cb.setValue(true);

        // Then
        assertThat(cb.getValue()).isEqualTo(true);
    }

    @Test
    public void click() {
        // Given
        CheckBox cb = new CheckBox();
        cb.setEnabled(true);
        // Preconditions
        assertThat(cb.getValue()).isFalse();

        // When 1
        Browser.click(cb);

        // Then 1
        assertThat(cb.getValue()).isTrue();
        assertThat(errorStringBuilder.toString()).isEqualTo("");

        // When 2
        Browser.click(cb);

        // Then 2
        assertThat(cb.getValue()).isFalse();
        assertThat(errorStringBuilder.toString()).isEqualTo("");
    }

    @Test
    public void click_disabled() {
        // Given
        CheckBox cb = new CheckBox();
        cb.setEnabled(false);

        // When
        Browser.click(cb);

        // Then
        assertThat(cb.getValue()).isFalse();
        assertThat(errorStringBuilder).startsWith("Cannot dispatch 'mouseover' event : the targeted element has to be enabled : <input type=\"checkbox\"");
    }

    @Test
    public void formValue() {
        // Given
        CheckBox cb = new CheckBox();
        // Preconditions
        assertThat(cb.getFormValue()).isEqualTo("on");

        // When
        cb.setFormValue("whatever");

        // Then
        assertThat(cb.getFormValue()).isEqualTo("whatever");

    }

    @Test
    public void html() {
        // Given
        CheckBox cb = new CheckBox("<h1>foo</h1>", true);
        // Preconditions
        assertThat(cb.getHTML()).isEqualTo("<h1>foo</h1>");

        // When
        cb.setHTML("<h1>test</h1>");

        // Then
        assertThat(cb.getHTML()).isEqualTo("<h1>test</h1>");
        assertThat(cb.getElement().getChild(1).getChildCount()).isEqualTo(1);
        HeadingElement h1 = cb.getElement().getChild(1).getChild(0).cast();
        assertThat(h1.getTagName()).isEqualTo("H1");
        assertThat(h1.getInnerText()).isEqualTo("test");
    }

    @Test
    public void name() {
        // Given
        CheckBox cb = new CheckBox();
        // Preconditions
        assertThat(cb.getName()).isEqualTo("");

        // When
        cb.setName("name");

        // Then
        assertThat(cb.getName()).isEqualTo("name");
    }

    @Test
    public void text() {
        // Given
        CheckBox cb = new CheckBox("foo");
        // Preconditions
        assertThat(cb.getText()).isEqualTo("foo");

        // When
        cb.setText("text");

        // Then
        assertThat(cb.getText()).isEqualTo("text");
    }

    @Test
    public void title() {
        // Given
        CheckBox cb = new CheckBox();
        // Preconditions
        assertThat(cb.getTitle()).isEqualTo("");

        // When
        cb.setTitle("title");
        assertThat(cb.getTitle()).isEqualTo("title");
    }

    @Test
    public void visible() {
        // Given
        CheckBox cb = new CheckBox();
        // Preconditions
        assertThat(cb.isVisible()).isEqualTo(true);

        // When
        cb.setVisible(false);

        // Then
        assertThat(cb.isVisible()).isEqualTo(false);
    }

}

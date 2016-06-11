package com.googlecode.gwt.test;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.user.client.ui.CheckBox;
import com.googlecode.gwt.test.utils.events.Browser;
import com.googlecode.gwt.test.utils.events.Browser.BrowserErrorHandler;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CheckBoxTest extends GwtTestTest {

    private final StringBuilder errorStringBuilder = new StringBuilder();

    @Before
    public void beforeCheckBoxTest() {
        errorStringBuilder.delete(0, errorStringBuilder.length());

        setBrowserErrorHandler(new BrowserErrorHandler() {

            public void onError(String errorMessage) {
                errorStringBuilder.append(errorMessage);
            }
        });
    }

    @Test
    public void checked() {
        // Arrange
        CheckBox cb = new CheckBox();
        // Pre-Assert
        assertEquals(false, cb.getValue());

        // Act
        cb.setValue(true);

        // Assert
        assertEquals(true, cb.getValue());
    }

    @Test
    public void click() {
        // Arrange
        CheckBox cb = new CheckBox();
        // Pre-Assert
        assertFalse(cb.getValue());

        // Act 1
        Browser.click(cb);

        // Assert 1
        assertTrue(cb.getValue());
        assertEquals("", errorStringBuilder.toString());

        // Act 2
        Browser.click(cb);

        // Assert 2
        assertFalse(cb.getValue());
        assertEquals("", errorStringBuilder.toString());
    }

    @Test
    public void click_disabled() {
        // Arrange
        CheckBox cb = new CheckBox();
        cb.setEnabled(false);

        // Act
        Browser.click(cb);

        // Assert
        assertFalse(cb.getValue());
        assertTrue(errorStringBuilder.toString().startsWith(
                "Cannot dispatch 'mouseover' event : the targeted element has to be enabled : <span class=\"gwt-CheckBox gwt-CheckBox-disabled\"><input type=\"checkbox\""));
    }

    @Test
    public void formValue() {
        // Arrange
        CheckBox cb = new CheckBox();
        // Pre-Assert
        assertEquals("on", cb.getFormValue());

        // Act
        cb.setFormValue("whatever");

        // Assert
        assertEquals("whatever", cb.getFormValue());

    }

    @Test
    public void html() {
        // Arrange
        CheckBox cb = new CheckBox("<h1>foo</h1>", true);
        // Pre-Assert
        assertEquals("<h1>foo</h1>", cb.getHTML());

        // Act
        cb.setHTML("<h1>test</h1>");

        // Assert
        assertEquals("<h1>test</h1>", cb.getHTML());
        assertEquals(1, cb.getElement().getChild(1).getChildCount());
        HeadingElement h1 = cb.getElement().getChild(1).getChild(0).cast();
        assertEquals("H1", h1.getTagName());
        assertEquals("test", h1.getInnerText());
    }

    @Test
    public void name() {
        // Arrange
        CheckBox cb = new CheckBox();
        // Pre-Assert
        assertEquals("", cb.getName());

        // Act
        cb.setName("name");

        // Assert
        assertEquals("name", cb.getName());
    }

    @Test
    public void text() {
        // Arrange
        CheckBox cb = new CheckBox("foo");
        // Pre-Assert
        assertEquals("foo", cb.getText());

        // Act
        cb.setText("text");

        // Assert
        assertEquals("text", cb.getText());
    }

    @Test
    public void title() {
        // Arrange
        CheckBox cb = new CheckBox();
        // Pre-Assert
        assertEquals("", cb.getTitle());

        // Act
        cb.setTitle("title");
        assertEquals("title", cb.getTitle());
    }

    @Test
    public void visible() {
        // Arrange
        CheckBox cb = new CheckBox();
        // Pre-Assert
        assertEquals(true, cb.isVisible());

        // Act
        cb.setVisible(false);

        // Assert
        assertEquals(false, cb.isVisible());
    }

}

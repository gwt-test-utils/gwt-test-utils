package com.googlecode.gwt.test;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.SuggestBox.SuggestionDisplay;
import com.googlecode.gwt.MockValueChangeHandler;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import com.googlecode.gwt.test.utils.WidgetUtils;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class WidgetUtilsTest extends GwtTestTest {

    @Test
    public void assertListBoxDataDoNotMatchDifferentElement() {
        // Arrange
        ListBox lb = new ListBox();
        lb.addItem("item0");
        lb.addItem("item1");
        lb.addItem("iTem2");

        String[] content = new String[]{"item0", "item1", "item2"};

        // Act & Assert
        assertFalse(WidgetUtils.assertListBoxDataMatch(lb, content));
    }

    @Test
    public void assertListBoxDataDoNotMatchMissingElement() {
        // Arrange
        ListBox lb = new ListBox();
        lb.addItem("item0");
        lb.addItem("item1");

        String[] content = new String[]{"item0", "item1", "item2"};

        // Act & Assert
        assertFalse(WidgetUtils.assertListBoxDataMatch(lb, content));
    }

    @Test
    public void assertListBoxDataMatch() {
        // Arrange
        ListBox lb = new ListBox();
        lb.addItem("item0");
        lb.addItem("item1");
        lb.addItem("item2");

        String[] content = new String[]{"item0", "item1", "item2"};

        // Act & Assert
        assertTrue(WidgetUtils.assertListBoxDataMatch(lb, content));
    }

    @Test
    public void getListBoxContentToString() {
        // Arrange
        ListBox lb = new ListBox();
        lb.addItem("item0");
        lb.addItem("item1");
        lb.addItem("item2");

        String expected = "item0 | item1 | item2 |";

        // Act
        String actual = WidgetUtils.getListBoxContentToString(lb);

        assertEquals(expected, actual);
    }

    @Test
    public void itemIsNotVisibleWhenParentIsNotVisible() {
        MenuBar bar = new MenuBar();
        bar.setVisible(false);
        MenuItem item0 = bar.addItem("test0", (Command) null);
        item0.setVisible(true);

        // Act
        Boolean isVisible = WidgetUtils.isWidgetVisible(item0);

        // Assert
        assertFalse(isVisible);
    }

    @Test
    public void listBoxIndex() {
        // Arrange
        ListBox lb = new ListBox();
        lb.addItem("item0");
        lb.addItem("item1");
        lb.addItem("item2");

        // Act & Assert
        assertEquals(0, WidgetUtils.getIndexInListBox(lb, "item0"));
        assertEquals(1, WidgetUtils.getIndexInListBox(lb, "item1"));
        assertEquals(2, WidgetUtils.getIndexInListBox(lb, "item2"));
        assertEquals(-1, WidgetUtils.getIndexInListBox(lb, "item3"));
    }

    @Test
    public void menuBarItems() {
        // Arrange
        MenuBar bar = new MenuBar();

        Command cmd = new Command() {
            public void execute() {
            }

        };

        MenuItem item0 = bar.addItem("item0", cmd);
        MenuItem item1 = bar.addItem("item1", cmd);

        // Act
        List<MenuItem> items = WidgetUtils.getMenuItems(bar);

        // Assert
        assertEquals(2, items.size());
        assertEquals(item0, items.get(0));
        assertEquals(item1, items.get(1));
    }

    @Test
    public void newWidgetIsNotVisibleWhenParentIsNotVisible() {
        // Arrange
        SimplePanel sp = new SimplePanel();
        sp.setVisible(false);
        Button b = new Button();
        sp.add(b);

        // Act
        Boolean isVisible = WidgetUtils.isWidgetVisible(b);

        // Assert
        assertFalse(isVisible);
    }

    @Test
    public void newWidgetIsVisible() {
        // Arrange
        Button b = new Button();

        // Act
        Boolean isVisible = WidgetUtils.isWidgetVisible(b);

        // Assert
        assertTrue(isVisible);
    }

    @Test
    public void setCheckBoxValueSilent() {
        // Arrange
        CheckBox cb = new CheckBox();
        cb.setValue(true);
        MockValueChangeHandler<Boolean> mockChangeHandler = new MockValueChangeHandler<Boolean>();
        cb.addValueChangeHandler(mockChangeHandler);

        // Pre-Assert
        assertTrue(cb.getValue());
        assertEquals(0, mockChangeHandler.getCallCount());

        // Act
        WidgetUtils.setCheckBoxValueSilent(cb, false);

        // Assert
        assertEquals(false, cb.getValue());
        assertEquals(0, mockChangeHandler.getCallCount());
    }

    @Test
    public void suggestBoxItems() {
        // Arrange
        SuggestBox box = new SuggestBox();
        SuggestionDisplay display = GwtReflectionUtils.getPrivateFieldValue(box, "display");
        MenuBar bar = GwtReflectionUtils.getPrivateFieldValue(display, "suggestionMenu");

        Command cmd = new Command() {
            public void execute() {
            }

        };

        MenuItem item0 = bar.addItem("item0", cmd);
        MenuItem item1 = bar.addItem("item1", cmd);

        // Act
        List<MenuItem> items = WidgetUtils.getMenuItems(box);

        // Assert
        assertEquals(2, items.size());
        assertEquals(item0, items.get(0));
        assertEquals(item1, items.get(1));
    }

}

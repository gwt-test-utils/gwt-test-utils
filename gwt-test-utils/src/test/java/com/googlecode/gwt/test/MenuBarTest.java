package com.googlecode.gwt.test;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;
import com.googlecode.gwt.test.utils.events.Browser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MenuBarTest extends GwtTestTest {

    Command cmd = new Command() {

        public void execute() {
            called = true;
        }

    };

    private boolean called = false;

    @Test
    public void addItem() {
        // Arrange
        MenuBar bar = new MenuBar();

        // Act
        MenuItem item0 = bar.addItem("test0", cmd);
        MenuItem item1 = bar.addItem("test1", cmd);

        // Assert
        assertEquals(0, bar.getItemIndex(item0));
        assertEquals(1, bar.getItemIndex(item1));
        assertEquals(bar, item0.getParentMenu());
        assertEquals(bar, item1.getParentMenu());
    }

    @Test
    public void addSeparator() {
        // Arrange
        MenuBar bar = new MenuBar();
        bar.addItem("test0", cmd);

        // Act
        MenuItemSeparator separator = bar.addSeparator();
        bar.addItem("test1", cmd);

        // Assert
        assertEquals(1, bar.getSeparatorIndex(separator));
    }

    @Test
    public void animationEnabled() {
        // Arrange
        MenuBar bar = new MenuBar();

        // Act
        bar.setAnimationEnabled(true);

        // Assert
        assertEquals(true, bar.isAnimationEnabled());
    }

    @Test
    public void autoOpen() {
        // Arrange
        MenuBar bar = new MenuBar();

        // Act
        bar.setAutoOpen(false);

        // Assert
        assertEquals(false, bar.getAutoOpen());
    }

    @Test
    public void click() {
        // Arrange
        MenuBar bar = new MenuBar();
        MenuItem item = bar.addItem("item", cmd);
        // Pre-Assert
        assertEquals(false, called);

        // Act
        Browser.click(bar, item);

        // Assert
        assertEquals(true, called);
    }

    @Test
    public void constructor_Complex() {
        // Arrange
        MenuBar bar = new MenuBar();
        MenuBar subMenuBar = new MenuBar();
        MenuItem item = new MenuItem("item", false, subMenuBar);
        bar.addItem(item);
        item.setCommand(cmd);
        // Pre-Assert
        assertEquals(false, called);

        // Act
        Browser.click(bar, item);

        // Assert
        assertEquals(true, called);
    }

    @Test
    public void removeItem() {
        // Arrange
        MenuBar bar = new MenuBar();
        MenuItem item0 = bar.addItem("test0", cmd);
        MenuItem item1 = bar.addItem("test1", cmd);

        // Act
        bar.removeItem(item0);

        // Assert
        assertEquals(0, bar.getItemIndex(item1));
    }

    @Test
    public void title() {
        // Arrange
        MenuBar bar = new MenuBar();
        // Pre-Assert
        assertEquals("", bar.getTitle());

        // Act
        bar.setTitle("title");

        // Assert
        assertEquals("title", bar.getTitle());
    }

    @Test
    public void visible() {
        // Arrange
        MenuBar bar = new MenuBar();
        // Pre-Assert
        assertEquals(true, bar.isVisible());

        // Act
        bar.setVisible(false);

        // Assert
        assertEquals(false, bar.isVisible());
    }

}

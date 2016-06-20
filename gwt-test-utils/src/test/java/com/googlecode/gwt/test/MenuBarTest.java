package com.googlecode.gwt.test;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;
import com.googlecode.gwt.test.utils.events.Browser;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuBarTest extends GwtTestTest {

    private boolean called = false;
    Command cmd = new Command() {

        public void execute() {
            called = true;
        }

    };

    @Test
    public void addItem() {
        // Given
        MenuBar bar = new MenuBar();

        // When
        MenuItem item0 = bar.addItem("test0", cmd);
        MenuItem item1 = bar.addItem("test1", cmd);

        // Then
        assertThat(bar.getItemIndex(item0)).isEqualTo(0);
        assertThat(bar.getItemIndex(item1)).isEqualTo(1);
        assertThat(item0.getParentMenu()).isEqualTo(bar);
        assertThat(item1.getParentMenu()).isEqualTo(bar);
    }

    @Test
    public void addSeparator() {
        // Given
        MenuBar bar = new MenuBar();
        bar.addItem("test0", cmd);

        // When
        MenuItemSeparator separator = bar.addSeparator();
        bar.addItem("test1", cmd);

        // Then
        assertThat(bar.getSeparatorIndex(separator)).isEqualTo(1);
    }

    @Test
    public void animationEnabled() {
        // Given
        MenuBar bar = new MenuBar();

        // When
        bar.setAnimationEnabled(true);

        // Then
        assertThat(bar.isAnimationEnabled()).isEqualTo(true);
    }

    @Test
    public void autoOpen() {
        // Given
        MenuBar bar = new MenuBar();

        // When
        bar.setAutoOpen(false);

        // Then
        assertThat(bar.getAutoOpen()).isEqualTo(false);
    }

    @Test
    public void click() {
        // Given
        MenuBar bar = new MenuBar();
        MenuItem item = bar.addItem("item", cmd);
        // Preconditions
        assertThat(called).isEqualTo(false);

        // When
        Browser.click(bar, item);

        // Then
        assertThat(called).isEqualTo(true);
    }

    @Test
    public void constructor_Complex() {
        // Given
        MenuBar bar = new MenuBar();
        MenuBar subMenuBar = new MenuBar();
        MenuItem item = new MenuItem("item", false, subMenuBar);
        bar.addItem(item);
        item.setCommand(cmd);
        // Preconditions
        assertThat(called).isEqualTo(false);

        // When
        Browser.click(bar, item);

        // Then
        assertThat(called).isEqualTo(true);
    }

    @Test
    public void removeItem() {
        // Given
        MenuBar bar = new MenuBar();
        MenuItem item0 = bar.addItem("test0", cmd);
        MenuItem item1 = bar.addItem("test1", cmd);

        // When
        bar.removeItem(item0);

        // Then
        assertThat(bar.getItemIndex(item1)).isEqualTo(0);
    }

    @Test
    public void title() {
        // Given
        MenuBar bar = new MenuBar();
        // Preconditions
        assertThat(bar.getTitle()).isEqualTo("");

        // When
        bar.setTitle("title");

        // Then
        assertThat(bar.getTitle()).isEqualTo("title");
    }

    @Test
    public void visible() {
        // Given
        MenuBar bar = new MenuBar();
        // Preconditions
        assertThat(bar.isVisible()).isEqualTo(true);

        // When
        bar.setVisible(false);

        // Then
        assertThat(bar.isVisible()).isEqualTo(false);
    }

}

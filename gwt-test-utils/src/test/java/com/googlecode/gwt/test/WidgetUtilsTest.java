package com.googlecode.gwt.test;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.SuggestBox.SuggestionDisplay;
import com.googlecode.gwt.MockValueChangeHandler;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import com.googlecode.gwt.test.utils.WidgetUtils;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WidgetUtilsTest extends GwtTestTest {

    @Test
    public void assertListBoxDataDoNotMatchDifferentElement() {
        // Given
        ListBox lb = new ListBox();
        lb.addItem("item0");
        lb.addItem("item1");
        lb.addItem("iTem2");

        String[] content = new String[]{"item0", "item1", "item2"};

        // When & Then
        assertThat(WidgetUtils.assertListBoxDataMatch(lb, content)).isFalse();
    }

    @Test
    public void assertListBoxDataDoNotMatchMissingElement() {
        // Given
        ListBox lb = new ListBox();
        lb.addItem("item0");
        lb.addItem("item1");

        String[] content = new String[]{"item0", "item1", "item2"};

        // When & Then
        assertThat(WidgetUtils.assertListBoxDataMatch(lb, content)).isFalse();
    }

    @Test
    public void assertListBoxDataMatch() {
        // Given
        ListBox lb = new ListBox();
        lb.addItem("item0");
        lb.addItem("item1");
        lb.addItem("item2");

        String[] content = new String[]{"item0", "item1", "item2"};

        // When & Then
        assertThat(WidgetUtils.assertListBoxDataMatch(lb, content)).isTrue();
    }

    @Test
    public void getListBoxContentToString() {
        // Given
        ListBox lb = new ListBox();
        lb.addItem("item0");
        lb.addItem("item1");
        lb.addItem("item2");

        String expected = "item0 | item1 | item2 |";

        // When
        String actual = WidgetUtils.getListBoxContentToString(lb);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void itemIsNotVisibleWhenParentIsNotVisible() {
        MenuBar bar = new MenuBar();
        bar.setVisible(false);
        MenuItem item0 = bar.addItem("test0", (Command) null);
        item0.setVisible(true);

        // When
        Boolean isVisible = WidgetUtils.isWidgetVisible(item0);

        // Then
        assertThat(isVisible).isFalse();
    }

    @Test
    public void listBoxIndex() {
        // Given
        ListBox lb = new ListBox();
        lb.addItem("item0");
        lb.addItem("item1");
        lb.addItem("item2");

        // When & Then
        assertThat(WidgetUtils.getIndexInListBox(lb, "item0")).isEqualTo(0);
        assertThat(WidgetUtils.getIndexInListBox(lb, "item1")).isEqualTo(1);
        assertThat(WidgetUtils.getIndexInListBox(lb, "item2")).isEqualTo(2);
        assertThat(WidgetUtils.getIndexInListBox(lb, "item3")).isEqualTo(-1);
    }

    @Test
    public void menuBarItems() {
        // Given
        MenuBar bar = new MenuBar();

        Command cmd = new Command() {
            public void execute() {
            }

        };

        MenuItem item0 = bar.addItem("item0", cmd);
        MenuItem item1 = bar.addItem("item1", cmd);

        // When
        List<MenuItem> items = WidgetUtils.getMenuItems(bar);

        // Then
        assertThat(items).hasSize(2);
        assertThat(items.get(0)).isEqualTo(item0);
        assertThat(items.get(1)).isEqualTo(item1);
    }

    @Test
    public void newWidgetIsNotVisibleWhenParentIsNotVisible() {
        // Given
        SimplePanel sp = new SimplePanel();
        sp.setVisible(false);
        Button b = new Button();
        sp.add(b);

        // When
        Boolean isVisible = WidgetUtils.isWidgetVisible(b);

        // Then
        assertThat(isVisible).isFalse();
    }

    @Test
    public void newWidgetIsVisible() {
        // Given
        Button b = new Button();

        // When
        Boolean isVisible = WidgetUtils.isWidgetVisible(b);

        // Then
        assertThat(isVisible).isTrue();
    }

    @Test
    public void setCheckBoxValueSilent() {
        // Given
        CheckBox cb = new CheckBox();
        cb.setValue(true);
        MockValueChangeHandler<Boolean> mockChangeHandler = new MockValueChangeHandler<Boolean>();
        cb.addValueChangeHandler(mockChangeHandler);

        // Preconditions
        assertThat(cb.getValue()).isTrue();
        assertThat(mockChangeHandler.getCallCount()).isEqualTo(0);

        // When
        WidgetUtils.setCheckBoxValueSilent(cb, false);

        // Then
        assertThat(cb.getValue()).isEqualTo(false);
        assertThat(mockChangeHandler.getCallCount()).isEqualTo(0);
    }

    @Test
    public void suggestBoxItems() {
        // Given
        SuggestBox box = new SuggestBox();
        SuggestionDisplay display = GwtReflectionUtils.getPrivateFieldValue(box, "display");
        MenuBar bar = GwtReflectionUtils.getPrivateFieldValue(display, "suggestionMenu");

        Command cmd = new Command() {
            public void execute() {
            }

        };

        MenuItem item0 = bar.addItem("item0", cmd);
        MenuItem item1 = bar.addItem("item1", cmd);

        // When
        List<MenuItem> items = WidgetUtils.getMenuItems(box);

        // Then
        assertThat(items).hasSize(2);
        assertThat(items.get(0)).isEqualTo(item0);
        assertThat(items.get(1)).isEqualTo(item1);
    }

}

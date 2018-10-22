package com.googlecode.gwt.test;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TabPanelTest extends GwtTestTest {

    int selectedTabIndex = -1;

    @Test
    public void deck() {
        // Given
        TabPanel tp = createTabPanel();

        // When
        tp.selectTab(2);

        // Then
        assertThat(tp.getDeckPanel().getVisibleWidget()).isEqualTo(2);
    }

    @Test
    public void selection() {
        // Given
        TabPanel tp = createTabPanel();

        tp.addSelectionHandler(event -> selectedTabIndex = event.getSelectedItem());
        // Preconditions
        assertThat(selectedTabIndex).isEqualTo(-1);

        // When
        tp.selectTab(1);

        // Then
        assertThat(selectedTabIndex).isEqualTo(1);
    }

    @Test
    public void tabPanel() {
        // Given
        TabPanel tp = createTabPanel();

        // When
        Widget w = tp.getWidget(1);

        // Then
        assertThat(w instanceof HTML).isTrue();
        HTML html = (HTML) w;
        assertThat(html.getHTML()).isEqualTo("Bar");
    }

    @Test
    public void title() {
        // Given
        TabPanel tp = new TabPanel();
        // Preconditions
        assertThat(tp.getTitle()).isEqualTo("");

        // When
        tp.setTitle("title");

        // Then
        assertThat(tp.getTitle()).isEqualTo("title");
    }

    @Test
    public void visible() {
        // Given
        TabPanel tp = new TabPanel();
        // Preconditions
        assertThat(tp.isVisible()).isEqualTo(true);

        // When
        tp.setVisible(false);

        // Then
        assertThat(tp.isVisible()).isEqualTo(false);
    }

    @Test
    public void widgetIndex() {
        // Given
        TabPanel tp = new TabPanel();
        Widget widget0 = new HTML("Foo");
        tp.add(widget0, "foo");
        Widget widget1 = new HTML("Bar");
        tp.add(widget1, "bar");

        // When & Then
        assertThat(tp.getWidgetIndex(widget1)).isEqualTo(1);
    }

    private TabPanel createTabPanel() {
        TabPanel tp = new TabPanel();
        tp.add(new HTML("Foo"), "foo");
        tp.add(new HTML("Bar"), "bar");
        tp.add(new HTML("Baz"), "baz");

        return tp;
    }

}

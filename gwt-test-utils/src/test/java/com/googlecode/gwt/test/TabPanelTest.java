package com.googlecode.gwt.test;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TabPanelTest extends GwtTestTest {

    int selectedTabIndex = -1;

    @Test
    public void deck() {
        // Arrange
        TabPanel tp = createTabPanel();

        // Act
        tp.selectTab(2);

        // Assert
        assertEquals(2, tp.getDeckPanel().getVisibleWidget());
    }

    @Test
    public void selection() {
        // Arrange
        TabPanel tp = createTabPanel();

        tp.addSelectionHandler(new SelectionHandler<Integer>() {

            public void onSelection(SelectionEvent<Integer> event) {
                selectedTabIndex = event.getSelectedItem();
            }
        });
        // Pre-Assert
        assertEquals(-1, selectedTabIndex);

        // Act
        tp.selectTab(1);

        // Assert
        assertEquals(1, selectedTabIndex);
    }

    @Test
    public void tabPanel() {
        // Arrange
        TabPanel tp = createTabPanel();

        // Act
        Widget w = tp.getWidget(1);

        // Assert
        assertTrue(w instanceof HTML);
        HTML html = (HTML) w;
        assertEquals("Bar", html.getHTML());
    }

    @Test
    public void title() {
        // Arrange
        TabPanel tp = new TabPanel();
        // Pre-Assert
        assertEquals("", tp.getTitle());

        // Act
        tp.setTitle("title");

        // Assert
        assertEquals("title", tp.getTitle());
    }

    @Test
    public void visible() {
        // Arrange
        TabPanel tp = new TabPanel();
        // Pre-Assert
        assertEquals(true, tp.isVisible());

        // Act
        tp.setVisible(false);

        // Assert
        assertEquals(false, tp.isVisible());
    }

    @Test
    public void widgetIndex() {
        // Arrange
        TabPanel tp = new TabPanel();
        Widget widget0 = new HTML("Foo");
        tp.add(widget0, "foo");
        Widget widget1 = new HTML("Bar");
        tp.add(widget1, "bar");

        // Act & Assert
        assertEquals(1, tp.getWidgetIndex(widget1));
    }

    private TabPanel createTabPanel() {
        TabPanel tp = new TabPanel();
        tp.add(new HTML("Foo"), "foo");
        tp.add(new HTML("Bar"), "bar");
        tp.add(new HTML("Baz"), "baz");

        return tp;
    }

}

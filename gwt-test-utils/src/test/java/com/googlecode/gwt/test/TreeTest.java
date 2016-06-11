package com.googlecode.gwt.test;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TreeTest extends GwtTestTest {

    private TreeItem clickedTreeItem;
    private TreeItem item0;
    private TreeItem item1;
    private TreeItem item2;
    private TreeItem parent;

    private Tree tree;

    @Test
    public void addItem() {
        // Act
        tree.addItem(new SafeHtmlBuilder().appendEscaped("parent2").toSafeHtml());

        // Assert
        assertEquals(2, tree.getItemCount());
        assertEquals("parent2", tree.getItem(1).getHTML());
    }

    @Test
    public void addSubItem() {
        // Act
        tree.getItem(0).addItem(new SafeHtmlBuilder().appendEscaped("item3").toSafeHtml());

        // Assert
        assertEquals(4, tree.getItem(0).getChildCount());
        assertEquals(item0, tree.getItem(0).getChild(0));
        assertEquals(item1, tree.getItem(0).getChild(1));
        assertEquals(item2, tree.getItem(0).getChild(2));
        assertEquals("item3", tree.getItem(0).getChild(3).getHTML());
    }

    @Test
    public void animationEnabled() {
        // Act
        tree.setAnimationEnabled(true);

        // Assert
        assertEquals(true, tree.isAnimationEnabled());
    }

    @Before
    public void beforeTreeTest() {
        // Create a tree with a few items in it.
        parent = new TreeItem(new SafeHtmlBuilder().appendEscaped("parent").toSafeHtml());
        item0 = parent.addItem(new SafeHtmlBuilder().appendEscaped("item0").toSafeHtml());
        item1 = parent.addItem(new SafeHtmlBuilder().appendEscaped("item1").toSafeHtml());

        // Add a CheckBox to the tree
        item2 = new TreeItem(new CheckBox("item2"));
        parent.addItem(item2);

        tree = new Tree();
        tree.addItem(parent);

        // Add it to the root panel.
        RootPanel.get().add(tree);

        assertTrue(tree.isVisible());

        clickedTreeItem = null;

    }

    @Test
    public void removeItem() {
        // Act
        tree.removeItem(parent);

        // Assert
        assertEquals(0, tree.getItemCount());
    }

    @Test
    public void removeSubItem() {
        // Act
        tree.getItem(0).removeItem(item0);

        // Assert
        assertEquals(2, tree.getItem(0).getChildCount());
        assertEquals(item1, tree.getItem(0).getChild(0));
        assertEquals(item2, tree.getItem(0).getChild(1));
    }

    @Test
    public void selected() {
        // Arrange
        tree.addSelectionHandler(new SelectionHandler<TreeItem>() {

            public void onSelection(SelectionEvent<TreeItem> event) {
                clickedTreeItem = event.getSelectedItem();
            }
        });

        // Act
        tree.setSelectedItem(item1);
        TreeItem selected = tree.getSelectedItem();

        // Assert
        assertEquals(item1, clickedTreeItem);
        assertEquals(item1, selected);
    }

    @Test
    public void selectedOnFocusWidget() {
        // Arrange
        tree.addSelectionHandler(new SelectionHandler<TreeItem>() {

            public void onSelection(SelectionEvent<TreeItem> event) {
                clickedTreeItem = event.getSelectedItem();
            }
        });

        // Act on item2 which wrap a Checkbox
        tree.setSelectedItem(item2);
        TreeItem selected = tree.getSelectedItem();

        // Assert
        assertEquals(item2, clickedTreeItem);
        assertEquals(item2, selected);
    }

    @Test
    public void title() {
        // Act
        tree.setTitle("title");

        // Assert
        assertEquals("title", tree.getTitle());
    }

    @Test
    public void visible() {
        // Pre-Assert
        assertEquals(true, tree.isVisible());

        // Act
        tree.setVisible(false);

        // Assert
        assertEquals(false, tree.isVisible());
    }

}

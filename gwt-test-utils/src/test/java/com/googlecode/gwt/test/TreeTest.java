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

import static org.assertj.core.api.Assertions.assertThat;

public class TreeTest extends GwtTestTest {

    private TreeItem clickedTreeItem;
    private TreeItem item0;
    private TreeItem item1;
    private TreeItem item2;
    private TreeItem parent;

    private Tree tree;

    @Test
    public void addItem() {
        // When
        tree.addItem(new SafeHtmlBuilder().appendEscaped("parent2").toSafeHtml());

        // Then
        assertThat(tree.getItemCount()).isEqualTo(2);
        assertThat(tree.getItem(1).getHTML()).isEqualTo("parent2");
    }

    @Test
    public void addSubItem() {
        // When
        tree.getItem(0).addItem(new SafeHtmlBuilder().appendEscaped("item3").toSafeHtml());

        // Then
        assertThat(tree.getItem(0).getChildCount()).isEqualTo(4);
        assertThat(tree.getItem(0).getChild(0)).isEqualTo(item0);
        assertThat(tree.getItem(0).getChild(1)).isEqualTo(item1);
        assertThat(tree.getItem(0).getChild(2)).isEqualTo(item2);
        assertThat(tree.getItem(0).getChild(3).getHTML()).isEqualTo("item3");
    }

    @Test
    public void animationEnabled() {
        // When
        tree.setAnimationEnabled(true);

        // Then
        assertThat(tree.isAnimationEnabled()).isEqualTo(true);
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

        assertThat(tree.isVisible()).isTrue();

        clickedTreeItem = null;

    }

    @Test
    public void removeItem() {
        // When
        tree.removeItem(parent);

        // Then
        assertThat(tree.getItemCount()).isEqualTo(0);
    }

    @Test
    public void removeSubItem() {
        // When
        tree.getItem(0).removeItem(item0);

        // Then
        assertThat(tree.getItem(0).getChildCount()).isEqualTo(2);
        assertThat(tree.getItem(0).getChild(0)).isEqualTo(item1);
        assertThat(tree.getItem(0).getChild(1)).isEqualTo(item2);
    }

    @Test
    public void selected() {
        // Given
        tree.addSelectionHandler(event -> clickedTreeItem = event.getSelectedItem());

        // When
        tree.setSelectedItem(item1);
        TreeItem selected = tree.getSelectedItem();

        // Then
        assertThat(clickedTreeItem).isEqualTo(item1);
        assertThat(selected).isEqualTo(item1);
    }

    @Test
    public void selectedOnFocusWidget() {
        // Given
        tree.addSelectionHandler(event -> clickedTreeItem = event.getSelectedItem());

        // When on item2 which wrap a Checkbox
        tree.setSelectedItem(item2);
        TreeItem selected = tree.getSelectedItem();

        // Then
        assertThat(clickedTreeItem).isEqualTo(item2);
        assertThat(selected).isEqualTo(item2);
    }

    @Test
    public void title() {
        // When
        tree.setTitle("title");

        // Then
        assertThat(tree.getTitle()).isEqualTo("title");
    }

    @Test
    public void visible() {
        // Preconditions
        assertThat(tree.isVisible()).isEqualTo(true);

        // When
        tree.setVisible(false);

        // Then
        assertThat(tree.isVisible()).isEqualTo(false);
    }

}

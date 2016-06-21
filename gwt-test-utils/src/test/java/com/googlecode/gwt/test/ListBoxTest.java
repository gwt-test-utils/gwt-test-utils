package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.ListBox;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ListBoxTest extends GwtTestTest {

    @Test
    public void clear() {
        // Given
        ListBox listBox = getListBox();
        assertThat(listBox.getVisibleItemCount()).isEqualTo(2);
        listBox.setSelectedIndex(1);

        // When
        listBox.clear();

        // Then
        assertThat(listBox.getSelectedIndex()).isEqualTo(-1);
        assertThat(listBox.getItemCount()).isEqualTo(0);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void isMultipleSelect() {
        // Given
        ListBox listBox = new ListBox(false);
        // Preconditions
        assertThat(listBox.isMultipleSelect()).isEqualTo(false);

        // When
        listBox.setMultipleSelect(true);

        // Then
        assertThat(listBox.isMultipleSelect()).isEqualTo(true);
    }

    @Test
    public void listBox() {
        // Given
        ListBox listBox = getListBox();

        // When & Then
        assertThat(listBox.getVisibleItemCount()).isEqualTo(2);
        assertThat(listBox.getItemCount()).isEqualTo(2);
        assertThat(listBox.getItemText(0)).isEqualTo("item 0");
        assertThat(listBox.getItemText(1)).isEqualTo("item 1");
    }

    @Test
    public void name() {
        // Given
        ListBox listBox = new ListBox(false);

        // When
        listBox.setName("name");

        // Then
        assertThat(listBox.getName()).isEqualTo("name");
    }

    @Test
    public void removeItem() {
        // Given
        ListBox listBox = getListBox();
        // Preconditions
        assertThat(listBox.getVisibleItemCount()).isEqualTo(2);

        // When
        listBox.removeItem(0);

        // Then
        assertThat(listBox.getVisibleItemCount()).isEqualTo(1);
        assertThat(listBox.getItemText(0)).isEqualTo("item 1");
    }

    @Test
    public void selected() {
        // Given
        ListBox listBox = getListBox();

        // When
        listBox.setSelectedIndex(1);

        // Then
        assertThat(listBox.getItemText(listBox.getSelectedIndex())).isEqualTo("item 1");
    }

    @Test
    public void selectedIndex() {
        // Given
        ListBox listBox = getListBox();
        // Preconditions
        assertThat(listBox.getSelectedIndex()).isEqualTo(-1);

        // When
        listBox.setSelectedIndex(1);

        // Then
        assertThat(listBox.getSelectedIndex()).isEqualTo(1);
    }

    @Test
    public void tabIndex() {
        // Given
        ListBox listBox = new ListBox(false);

        // When
        listBox.setTabIndex(2);

        // Then
        assertThat(listBox.getTabIndex()).isEqualTo(2);
    }

    @Test
    public void title() {
        // Given
        ListBox listBox = new ListBox(false);

        // When
        listBox.setTitle("title");

        // Then
        assertThat(listBox.getTitle()).isEqualTo("title");
    }

    @Test
    public void visible() {
        // Given
        ListBox listBox = new ListBox(false);
        // Preconditions
        assertThat(listBox.isVisible()).isEqualTo(true);

        // When
        listBox.setVisible(false);

        // Then
        assertThat(listBox.isVisible()).isEqualTo(false);
    }

    private ListBox getListBox() {
        ListBox listBox = new ListBox(false);
        listBox.setVisibleItemCount(2);
        listBox.addItem("item 0");
        listBox.addItem("item 1");

        return listBox;
    }

}

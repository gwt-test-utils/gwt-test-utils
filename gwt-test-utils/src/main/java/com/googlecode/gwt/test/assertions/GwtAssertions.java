package com.googlecode.gwt.test.assertions;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.*;
import com.googlecode.gwt.test.finder.GwtInstance;

/**
 * Entry point for assertion methods for different GWT types. Each method in this class is a static
 * factory for the type-specific assertion objects. The purpose of this class is to make test code
 * more readable, following <strong>assertj</strong> principles.
 *
 * @author Gael Lazzari
 */
public class GwtAssertions {

    /**
     * Creates a new instance of <code>{@link CheckBoxAssert}</code>.
     *
     * @param actual the checkbox to be the target of the assertions methods.
     * @return the created assertion object.
     */
    public static CheckBoxAssert assertThat(CheckBox actual) {
        return new CheckBoxAssert(actual);
    }

    /**
     * Creates a new instance of <code>{@link DoubleBoxAssert}</code>.
     *
     * @param actual the doubleBox to be the target of the assertions methods.
     * @return the created assertion object.
     */
    public static DoubleBoxAssert assertThat(DoubleBox actual) {
        return new DoubleBoxAssert(actual);
    }

    /**
     * Creates a new instance of <code>{@link ElementAssert}</code>.
     *
     * @param actual the element to be the target of the assertions methods.
     * @return the created assertion object.
     */
    public static ElementAssert assertThat(Element actual) {
        return new ElementAssert(actual);
    }

    /**
     * Creates a new instance of <code>{@link FocusWidgetAssert}</code>.
     *
     * @param actual the generic focus widget to be the target of the assertions methods.
     * @return the created assertion object.
     */
    public static FocusWidgetAssert assertThat(FocusWidget actual) {
        return new FocusWidgetAssert(actual);
    }

    /**
     * Creates a new instance of <code>{@link WidgetAssert}</code>.
     *
     * @param actual the generic widget to be the target of the assertions methods.
     * @return the created assertion object.
     */
    public static GwtInstanceAssert assertThat(GwtInstance actual) {
        return new GwtInstanceAssert(actual);
    }

    /**
     * Creates a new instance of <code>{@link IntegerBoxAssert}</code>.
     *
     * @param actual the integerBox to be the target of the assertions methods.
     * @return the created assertion object.
     */
    public static IntegerBoxAssert assertThat(IntegerBox actual) {
        return new IntegerBoxAssert(actual);
    }

    /**
     * Creates a new instance of <code>{@link ListBoxAssert}</code>.
     *
     * @param actual the listbox to be the target of the assertions methods.
     * @return the created assertion object.
     */
    public static ListBoxAssert assertThat(ListBox actual) {
        return new ListBoxAssert(actual);
    }

    /**
     * Creates a new instance of <code>{@link LongBoxAssert}</code>.
     *
     * @param actual the longBox to be the target of the assertions methods.
     * @return the created assertion object.
     */
    public static LongBoxAssert assertThat(LongBox actual) {
        return new LongBoxAssert(actual);
    }

    /**
     * Creates a new instance of <code>{@link MenuItemAssert}</code>.
     *
     * @param actual the menu item to be the target of the assertions methods.
     * @return the created assertion object.
     */
    public static MenuItemAssert assertThat(MenuItem actual) {
        return new MenuItemAssert(actual);
    }

    /**
     * Creates a new instance of <code>{@link PanelAssert}</code>.
     *
     * @param actual the generic panel to be the target of the assertions methods.
     * @return the created assertion object.
     */
    public static PanelAssert assertThat(Panel actual) {
        return new PanelAssert(actual);
    }

    /**
     * Creates a new instance of <code>{@link PopupPanelAssert}</code>.
     *
     * @param actual the popupPanel to be the target of the assertions methods.
     * @return the created assertion object.
     */
    public static PopupPanelAssert assertThat(PopupPanel actual) {
        return new PopupPanelAssert(actual);
    }

    /**
     * Creates a new instance of <code>{@link SuggestBoxAssert}</code>.
     *
     * @param actual the suggestBox to be the target of the assertions methods.
     * @return the created assertion object.
     */
    public static SuggestBoxAssert assertThat(SuggestBox actual) {
        return new SuggestBoxAssert(actual);
    }

    /**
     * Creates a new instance of <code>{@link TabPanelAssert}</code>.
     *
     * @param actual the tabPanel to be the target of the assertions methods.
     * @return the created assertion object.
     */
    public static TabPanelAssert assertThat(TabPanel actual) {
        return new TabPanelAssert(actual);
    }

    /**
     * Creates a new instance of <code>{@link TextAreaAssert}</code>.
     *
     * @param actual the textArea to be the target of the assertions methods.
     * @return the created assertion object.
     */
    public static TextAreaAssert assertThat(TextArea actual) {
        return new TextAreaAssert(actual);
    }

    /**
     * Creates a new instance of <code>{@link TextBoxAssert}</code>.
     *
     * @param actual the textBox to be the target of the assertions methods.
     * @return the created assertion object.
     */
    public static TextBoxAssert assertThat(TextBox actual) {
        return new TextBoxAssert(actual);
    }

    /**
     * Creates a new instance of <code>{@link TreeItemAssert}</code>.
     *
     * @param actual the tree item to be the target of the assertions methods.
     * @return the created assertion object.
     */
    public static TreeItemAssert assertThat(TreeItem actual) {
        return new TreeItemAssert(actual);
    }

    /**
     * Creates a new instance of <code>{@link UIObjectAssert}</code>.
     *
     * @param actual the generic UiObject to be the target of the assertions methods.
     * @return the created assertion object.
     */
    public static UIObjectAssert assertThat(UIObject actual) {
        return new UIObjectAssert(actual);
    }

    /**
     * Creates a new instance of <code>{@link WidgetAssert}</code>.
     *
     * @param actual the generic widget to be the target of the assertions methods.
     * @return the created assertion object.
     */
    public static WidgetAssert assertThat(Widget actual) {
        return new WidgetAssert(actual);
    }

}

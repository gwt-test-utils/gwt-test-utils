package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.TreeItem;

/**
 * Assertions for {@link TreeItem}.
 *
 * @author Gael Lazzari
 */
public class TreeItemAssert extends BaseUIObjectAssert<TreeItemAssert, TreeItem> {

    /**
     * Creates a new <code>{@link TreeItemAssert}</code>.
     *
     * @param actual the actual value to verify.
     */
    protected TreeItemAssert(TreeItem actual) {
        super(actual, TreeItemAssert.class);
    }

    /**
     * Verifies that the actual {@link TreeItem} is not currently opened.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link TreeItem} is opened.
     */
    public TreeItemAssert isNotOpened() {
        if (actual.getState())
            failWithMessage("should not be opened");

        return this;
    }

    /**
     * Verifies that the actual {@link TreeItem} is not currently selected.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link TreeItem} is selected.
     */
    public TreeItemAssert isNotSelected() {
        if (actual.isSelected())
            failWithMessage("should not be selected");

        return this;
    }

    /**
     * Verifies that the actual {@link TreeItem} is currently opened.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link TreeItem} is not opened.
     */
    public TreeItemAssert isOpened() {
        if (!actual.getState())
            failWithMessage("should be opened");

        return this;
    }

    /**
     * Verifies that the actual {@link TreeItem} is currently selected.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link TreeItem} is not selected.
     */
    public TreeItemAssert isSelected() {
        if (!actual.isSelected())
            failWithMessage("should be selected");

        return this;
    }

}

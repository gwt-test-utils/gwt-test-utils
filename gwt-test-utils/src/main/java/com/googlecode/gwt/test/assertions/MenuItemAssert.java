package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * Assertions for {@link MenuItem}.
 *
 * @author Gael Lazzari
 */
public class MenuItemAssert extends BaseUIObjectAssert<MenuItemAssert, MenuItem> {

    /**
     * Creates a new <code>{@link MenuItemAssert}</code>.
     *
     * @param actual the actual value to verify.
     */
    protected MenuItemAssert(MenuItem actual) {
        super(actual, MenuItemAssert.class);
    }

    /**
     * Verifies that the actual {@link MenuItem} does not have an associated {@link Command}.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link MenuItem} has an associated {@link Command}.
     */
    public MenuItemAssert doesNotHaveCommand() {
        if (actual.getCommand() != null)
            failWithMessage("should not have an associated command");

        return this;
    }

    /**
     * Verifies that the actual {@link MenuItem} does not have an associated parent menu.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link MenuItem} has an associated parent menu.
     */
    public MenuItemAssert doesNotHaveParentMenu() {
        if (actual.getParentMenu() != null)
            failWithMessage("should not have an associated parent menu");

        return this;
    }

    /**
     * Verifies that the actual {@link MenuItem} does not have an associated sub-menu.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link MenuItem} has an associated sub-menu.
     */
    public MenuItemAssert doesNotHaveSubMenu() {
        if (actual.getSubMenu() != null)
            failWithMessage("should not have an associated sub-menu");

        return this;
    }

    /**
     * Verifies that the actual {@link MenuItem} has an associated {@link Command}.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link MenuItem} does not have an associated
     *                        {@link Command}.
     */
    public MenuItemAssert hasCommand() {
        if (actual.getCommand() == null)
            failWithMessage("should have an associated command");

        return this;
    }

    /**
     * Verifies that the actual {@link MenuItem} has an associated parent menu.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link MenuItem} does not have an associated parent menu.
     */
    public MenuItemAssert hasParentMenu() {
        if (actual.getParentMenu() == null)
            failWithMessage("should have an associated parent menu");

        return this;
    }

    /**
     * Verifies that the actual {@link MenuItem} has an associated sub-menu.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link MenuItem} does not have an associated sub-menu.
     */
    public MenuItemAssert hasSubMenu() {
        if (actual.getSubMenu() == null)
            failWithMessage("should have an associated sub-menu");

        return this;
    }

    /**
     * Verifies that the actual {@link MenuItem} is currently enabled.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link MenuItem} is not enabled.
     */
    public MenuItemAssert isEnabled() {
        if (!actual.isEnabled())
            failWithMessage("should be enabled");

        return this;
    }

    /**
     * Verifies that the actual {@link MenuItem} is not currently enabled.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link MenuItem} is enabled.
     */
    public MenuItemAssert isNotEnabled() {
        if (actual.isEnabled())
            failWithMessage("should not be enabled");

        return this;
    }

}

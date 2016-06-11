package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.ListBox;

/**
 * Assertions for {@link ListBox} instances.
 *
 * @author Gael Lazzari
 */
public class ListBoxAssert extends BaseListBoxAssert<ListBoxAssert, ListBox> {

    /**
     * Creates a new <code>{@link ListBoxAssert}</code>.
     *
     * @param actual the actual value to verify.
     */
    protected ListBoxAssert(ListBox actual) {
        super(actual, ListBoxAssert.class);
    }

}

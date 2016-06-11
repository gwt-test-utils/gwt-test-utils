package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.CheckBox;

/**
 * Assertions for {@link CheckBox} instances.
 *
 * @author Gael Lazzari
 */
public class CheckBoxAssert extends BaseCheckBoxAssert<CheckBoxAssert, CheckBox> {

    /**
     * Creates a new <code>{@link CheckBoxAssert}</code>.
     *
     * @param actual the actual value to verify.
     */
    protected CheckBoxAssert(CheckBox actual) {
        super(actual, CheckBoxAssert.class);
    }

}

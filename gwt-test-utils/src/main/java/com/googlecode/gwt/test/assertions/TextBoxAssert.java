package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.TextBox;

/**
 * Assertions for {@link TextBox} instances.
 *
 * @author Gael Lazzari
 */
public class TextBoxAssert extends BaseTextBoxAssert<TextBoxAssert, TextBox> {

    /**
     * Creates a new <code>{@link TextBoxAssert}</code>.
     *
     * @param actual the actual value to verify.
     */
    protected TextBoxAssert(TextBox actual) {
        super(actual, TextBoxAssert.class);
    }

}

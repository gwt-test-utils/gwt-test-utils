package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.TextArea;

/**
 * Assertions for {@link TextArea} instances.
 *
 * @author Gael Lazzari
 */
public class TextAreaAssert extends BaseTextAreaAssert<TextAreaAssert, TextArea> {

    /**
     * Creates a new <code>{@link TextAreaAssert}</code>.
     *
     * @param actual the actual value to verify.
     */
    protected TextAreaAssert(TextArea actual) {
        super(actual, TextAreaAssert.class);
    }

}
package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.SuggestBox;

/**
 * Assertions for {@link SuggestBox} instances.
 *
 * @author Gael Lazzari
 */
public class SuggestBoxAssert extends BaseSuggestBoxAssert<SuggestBoxAssert, SuggestBox> {

    /**
     * Creates a new <code>{@link SuggestBoxAssert}</code>.
     *
     * @param actual the actual value to verify.
     */
    protected SuggestBoxAssert(SuggestBox actual) {
        super(actual, SuggestBoxAssert.class);
    }

}

package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.LongBox;

/**
 * Assertions for {@link LongBox} instances.
 *
 * @author Gael Lazzari
 */
public class LongBoxAssert extends BaseValueBoxAssert<LongBoxAssert, LongBox, Long> {

    /**
     * Creates a new <code>{@link LongBoxAssert}</code>.
     *
     * @param actual the actual value to verify.
     */
    protected LongBoxAssert(LongBox actual) {
        super(actual, LongBoxAssert.class);
    }

}

package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.IntegerBox;

/**
 * Assertions for {@link IntegerBox} instances.
 *
 * @author Gael Lazzari
 */
public class IntegerBoxAssert extends BaseValueBoxAssert<IntegerBoxAssert, IntegerBox, Integer> {

    /**
     * Creates a new <code>{@link IntegerBoxAssert}</code>.
     *
     * @param actual the actual value to verify.
     */
    protected IntegerBoxAssert(IntegerBox actual) {
        super(actual, IntegerBoxAssert.class);
    }

}

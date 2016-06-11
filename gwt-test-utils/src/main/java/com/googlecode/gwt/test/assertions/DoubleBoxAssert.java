package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.DoubleBox;

/**
 * Assertions for {@link DoubleBox} instances.
 *
 * @author Gael Lazzari
 */
public class DoubleBoxAssert extends BaseValueBoxAssert<DoubleBoxAssert, DoubleBox, Double> {

    /**
     * Creates a new <code>{@link DoubleBoxAssert}</code>.
     *
     * @param actual the actual value to verify.
     */
    protected DoubleBoxAssert(DoubleBox actual) {
        super(actual, DoubleBoxAssert.class);
    }

}

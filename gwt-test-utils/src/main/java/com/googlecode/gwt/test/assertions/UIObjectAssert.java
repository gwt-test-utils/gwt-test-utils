package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.UIObject;

/**
 * Assertions for generic {@link UIObject} instances.
 *
 * @author Gael Lazzari
 */
public class UIObjectAssert extends BaseUIObjectAssert<UIObjectAssert, UIObject> {

    /**
     * Creates a new <code>{@link UIObjectAssert}</code>.
     *
     * @param actual the actual value to verify.
     */
    protected UIObjectAssert(UIObject actual) {
        super(actual, UIObjectAssert.class);
    }

}

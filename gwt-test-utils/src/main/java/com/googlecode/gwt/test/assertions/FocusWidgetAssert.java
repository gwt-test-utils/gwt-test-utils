package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.FocusWidget;

/**
 * Assertions for generic {@link FocusWidget} instances.
 *
 * @author Gael Lazzari
 */
public class FocusWidgetAssert extends BaseFocusWidgetAssert<FocusWidgetAssert, FocusWidget> {

    /**
     * Creates a new <code>{@link FocusWidgetAssert}</code>.
     *
     * @param actual the actual value to verify.
     */
    protected FocusWidgetAssert(FocusWidget actual) {
        super(actual, FocusWidgetAssert.class);
    }

}

package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.Widget;

/**
 * Assertions for generic {@link Widget} instances.
 *
 * @author Gael Lazzari
 */
public class WidgetAssert extends BaseWidgetAssert<WidgetAssert, Widget> {

    /**
     * Creates a new <code>{@link WidgetAssert}</code>.
     *
     * @param actual the actual value to verify.
     */
    protected WidgetAssert(Widget actual) {
        super(actual, WidgetAssert.class);
    }

}

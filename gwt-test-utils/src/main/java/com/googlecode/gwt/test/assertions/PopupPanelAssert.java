package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Assertions for {@link PopupPanel} instances.
 *
 * @author Gael Lazzari
 */
public class PopupPanelAssert extends BasePopupPanelAssert<PopupPanelAssert, PopupPanel> {

    /**
     * Creates a new <code>{@link PopupPanelAssert}</code>.
     *
     * @param actual the actual value to verify.
     */
    protected PopupPanelAssert(PopupPanel actual) {
        super(actual, PopupPanelAssert.class);
    }

}

package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.Panel;

/**
 * Assertions for generic {@link Panel} instances.
 *
 * @author Gael Lazzari
 */
public class PanelAssert extends BasePanelAssert<PanelAssert, Panel> {

    /**
     * Creates a new <code>{@link PanelAssert}</code>.
     *
     * @param actual the actual value to verify.
     */
    protected PanelAssert(Panel actual) {
        super(actual, PanelAssert.class);
    }

}

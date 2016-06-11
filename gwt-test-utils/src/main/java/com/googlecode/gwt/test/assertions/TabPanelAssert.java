package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.TabPanel;

/**
 * Assertions for {@link TabPanel} instances.
 *
 * @author Gael Lazzari
 */
public class TabPanelAssert extends BaseTabPanelAssert<TabPanelAssert, TabPanel> {

    /**
     * Creates a new <code>{@link TabPanelAssert}</code>.
     *
     * @param actual the actual value to verify.
     */
    protected TabPanelAssert(TabPanel actual) {
        super(actual, TabPanelAssert.class);
    }

}

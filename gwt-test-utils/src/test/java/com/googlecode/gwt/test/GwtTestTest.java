package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.TreeItem;

/**
 * {@link GwtTest} class to test gwt-test-utils.
 *
 * @author Gael Lazzari
 */
@GwtModule("com.googlecode.gwt.test.GwtTestUtils")
public abstract class GwtTestTest extends GwtTest {

    static {
        TreeItem ti = new TreeItem();
    }

    @Override
    protected String getHostPagePath(String moduleFullQualifiedName) {
        return null;
    }

}

package com.googlecode.gwt.test;

/**
 * {@link GwtTest} class to test gwt-test-utils.
 *
 * @author Gael Lazzari
 */
@GwtModule("com.googlecode.gwt.test.GwtTestUtils")
public abstract class GwtTestTest extends GwtTest {

    @Override
    protected String getHostPagePath(String moduleFullQualifiedName) {
        return null;
    }

}

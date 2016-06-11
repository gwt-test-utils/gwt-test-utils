package com.googlecode.gxt.test;

import com.googlecode.gwt.test.gxt3.GxtTest;

public abstract class GwtGxtTest extends GxtTest {

    @Override
    public String getModuleName() {
        return "com.googlecode.gxt.test.GxtTestUtils";
    }

    @Override
    protected String getHostPagePath(String moduleFullQualifiedName) {
        return null;
    }

}

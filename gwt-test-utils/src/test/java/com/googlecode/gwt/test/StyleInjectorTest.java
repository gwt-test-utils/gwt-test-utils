package com.googlecode.gwt.test;

import com.google.gwt.dom.client.StyleInjector;
import org.junit.Test;

public class StyleInjectorTest extends GwtTestTest {

    @Test
    public void inject() {
        // Then no expection is thrown
        StyleInjector.inject(".test{color:red;}");
        StyleInjector.inject(".testImmediate{color:green;}", true);
    }

    @Test
    public void injectAtEnd() {
        // Then no expection is thrown
        StyleInjector.injectAtEnd(".test{color:red;}");
        StyleInjector.injectAtEnd(".testImmediate{color:green;}", true);
    }

    @Test
    public void injectAtStart() {
        // Then no expection is thrown
        StyleInjector.injectAtStart(".test{color:red;}");
        StyleInjector.injectAtStart(".testImmediate{color:green;}", true);
    }

}

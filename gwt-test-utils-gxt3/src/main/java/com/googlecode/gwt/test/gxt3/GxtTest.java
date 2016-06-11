package com.googlecode.gwt.test.gxt3;

import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.gxt3.internal.handlers.BindingPropertySetCreateHandler;
import com.googlecode.gwt.test.gxt3.internal.handlers.XtemplatesCreateHandler;
import org.junit.After;
import org.junit.Before;

/**
 * <p>
 * Base class for test classes which need to manipulate (directly or indirectly)
 * GXT 3.x components.
 * </p>
 */
public abstract class GxtTest extends GwtTest {

    @Before
    public final void setupGxtTest() {
        addGwtCreateHandler(new BindingPropertySetCreateHandler());
        addGwtCreateHandler(XtemplatesCreateHandler.get());
    }

    @After
    public final void tearDownGxtTest() throws Exception {
        GxtReset.get().reset();
    }

}

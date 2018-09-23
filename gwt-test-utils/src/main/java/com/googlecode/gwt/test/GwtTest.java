package com.googlecode.gwt.test;

import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.internal.AfterTestCallbackManager;
import com.googlecode.gwt.test.internal.GwtConfig;
import com.googlecode.gwt.test.internal.GwtFactory;
import com.googlecode.gwt.test.internal.GwtTestDataHolder;
import com.googlecode.gwt.test.utils.events.Browser.BrowserErrorHandler;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.assertj.core.api.Fail;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.model.TestClass;

import java.util.List;

/**
 * <p>
 * Base class for test classes which need to manipulate (directly or indirectly) GWT components.
 * </p>
 * <p>
 * <p>
 * It provides the mechanism which allows the instantiation of GWT components in the Java Virtual
 * Machine, by beeing launched with the {@link GwtRunner} JUnit Runner.
 * </p>
 * <p>
 * <p>
 * Class loading parameters used to instantiate classes referenced in tests can be configured using
 * the META-INF\gwt-test-utils.properties file of your application.
 * </p>
 *
 * @author Gael Lazzari
 */
@RunWith(GwtRunner.class)
public abstract class GwtTest extends GwtModuleRunnerAdapter {

    private static final BrowserErrorHandler FEST_BROWSER_ERROR_HANDLER = new BrowserErrorHandler() {

        public void onError(String errorMessage) {
            Fail.fail(errorMessage);
        }
    };

    /**
     * Bind the GwtClassLoader to the current thread
     */
    @BeforeClass
    public static final void bindClassLoader() {
        Thread.currentThread().setContextClassLoader(GwtFactory.get().getClassLoader());
    }

    /**
     * Unbind the static classloader instance from the current thread by binding the system
     * classloader instead.
     */
    @AfterClass
    public static void unbindClassLoader() {
        Thread.currentThread().setContextClassLoader(GwtFactory.get().getClassLoader().getParent());
    }

    /**
     * Setup a new gwt-test-utils test class.
     */
    public GwtTest() {
        TestClass testClass = new TestClass(this.getClass());
        GwtConfig.get().setupGwtModule(testClass.getJavaClass());
        GwtConfig.get().setupInstance(this);
    }

    @Before
    public final void setUpGwtTest() throws Exception {
        this.setCanDispatchEventsOnDetachedWidgets(true);
        GwtTestDataHolder.get().setCurrentTestFailed(false);
    }

    @After
    public final void tearDownGwtTest() throws Exception {

        GwtReset.get().reset();

        boolean currentTestFailed = GwtTestDataHolder.get().isCurrentTestFailed();

        List<Throwable> throwables = AfterTestCallbackManager.get().triggerCallbacks();

        if (!currentTestFailed && throwables.size() > 0) {
            String error = (throwables.size() == 1)
                    ? "One exception thrown during gwt-test-utils cleanup phase : "
                    : throwables.size()
                    + " exceptions thrown during gwt-test-utils cleanup phase. First one is thrown :";

            throw new GwtTestException(error, throwables.get(0));
        }

    }

    /**
     * Create a test instance compatible with JUnit 3 {@link Test} so that the current
     * <code>GwtTest</code> can be added to a {@link TestSuite}.
     *
     * @return A JUnit Test adapter for this test.
     */
    protected Test createJUnit4TestAdapter() {
        return new JUnit4TestAdapter(this.getClass());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.gwt.test.GwtModuleRunnerAdapter#getDefaultBrowserErrorHandler ()
     */
    @Override
    protected BrowserErrorHandler getDefaultBrowserErrorHandler() {
        return FEST_BROWSER_ERROR_HANDLER;
    }

}

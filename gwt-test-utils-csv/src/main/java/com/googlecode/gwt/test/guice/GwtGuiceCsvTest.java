package com.googlecode.gwt.test.guice;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.googlecode.gwt.test.csv.GwtCsvTest;
import com.googlecode.gwt.test.exceptions.GwtTestConfigurationException;
import com.googlecode.gwt.test.rpc.RemoteServiceCreateHandler;
import org.junit.Before;

/**
 * <p>
 * Base class for CSV tests that needs to be run with a backend configured with Google Guice.
 * </p>
 * <p>
 * You can inject any object in subclasses. For {@link RemoteService} objects, see the
 * {@link GwtGuiceCsvTest#getRpcServiceFromInjector(Injector, Class, String)} method which you might
 * want to override to get deferred binding working as you need in test mode.
 * </p>
 * <p>
 * <p>
 * The {@link GwtGuiceCsvTest#getInjector()} also provides a default way to get a working Guice
 * {@link Injector} for your test.
 * </p>
 *
 * @author Alex Dobjanschi
 * @author Gael Lazzari
 */
public abstract class GwtGuiceCsvTest extends GwtCsvTest {

    /**
     * Guice test initialization method.
     */
    @Before
    public void beforeGwtGuiceTest() {

        final Injector injector = getInjector();
        // add a new RemoteServiceHandler which will call Guice Injector
        addGwtCreateHandler(new RemoteServiceCreateHandler() {

            @Override
            protected Object findService(Class<?> remoteServiceClass, String remoteServiceRelativePath) {
                return getRpcServiceFromInjector(injector, remoteServiceClass,
                        remoteServiceRelativePath);
            }
        });

        injector.injectMembers(this);
    }

    /**
     * Get the Guice injector to use. This implementation search in web.xml for any listener
     * assignable to {@link GuiceServletContextListener} to retrieve its internal Injector by
     * introspection.
     *
     * @return The Guice Injector to use.
     */
    protected Injector getInjector() {
        Injector injector = GwtGuiceHelper.get().getInjector();

        if (injector == null) {
            throw new GwtTestConfigurationException(
                    this.getClass().getSimpleName()
                            + ".getInjector() default implementation is not able to localize the Guice Injector to use. You should override it to provide our own implementation");
        }

        return injector;
    }

    /**
     * Retrieve a GWT RPC service from a Guice injector. This implementation reads ServletDefinition
     * declared in a Guice {@link ServletModule} to be able to bind object through the supplied
     * remote service relative path of the targeted servlet.
     *
     * @param injector                  The Guice Injector configured.
     * @param remoteServiceClass        The remote service interface of the Guice object to retrieve.
     * @param remoteServiceRelativePath The remote service relative path of the Guice object to
     *                                  retrieve.
     * @return The corresponding Guice object, or null if no object has been found for this type and
     * path.
     * @see GwtGuiceTest#getInjector()
     */
    protected Object getRpcServiceFromInjector(Injector injector, Class<?> remoteServiceClass,
                                               String remoteServiceRelativePath) {

        return GwtGuiceHelper.get().getRpcServiceFromInjector(injector, remoteServiceClass,
                remoteServiceRelativePath);
    }

}

package com.googlecode.gwt.test.spring;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.googlecode.gwt.test.csv.GwtCsvTest;
import com.googlecode.gwt.test.rpc.RemoteServiceCreateHandler;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

/**
 * <p>
 * Base class for tests that needs to be run with <code>spring-test</code> module.
 * </p>
 * <p>
 * Subclasses <strong>must be annotated</strong> with the spring {@link ContextConfiguration}
 * annotation to configure the spring files location to use to build the test context.
 * </p>
 * <p>
 * You can autowire any spring bean in subclasses. For {@link RemoteService} beans, see the
 * {@link GwtSpringCsvTest#findRpcServiceInSpringContext(ApplicationContext, Class, String)} method
 * which you might want to override to get deferred binding working as you need in test mode.
 * </p>
 *
 * @author Gael Lazzari
 */
@RunWith(GwtSpringCsvRunner.class)
public abstract class GwtSpringCsvTest extends GwtCsvTest implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * Spring test initialization method.
     */
    @Before
    public void beforeGwtSpringCsvTest() {
        // add a new RemoteServiceHandler which will call Spring context
        addGwtCreateHandler(new RemoteServiceCreateHandler() {

            @Override
            protected Object findService(Class<?> remoteServiceClass, String remoteServiceRelativePath) {
                return findRpcServiceInSpringContext(applicationContext, remoteServiceClass,
                        remoteServiceRelativePath);
            }
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext
     * (org.springframework.context.ApplicationContext)
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Retrieve a GWT RPC service from a spring context. This implementation collects all assignable
     * beans in the context. If no assignable bean can be found, it would returns <code>null</code>.
     * If one an only one assignable bean can be found, it would be returned. If many assignable
     * beans are found, this implementation would check if one of it has a matching
     * {@link RemoteServiceRelativePath} annotation on its class. If so, it is returned. Otherwise,
     * <code>null</code> is returned.
     *
     * @param applicationContext        The Spring context.
     * @param remoteServiceClass        The remote service interface of the Spring bean to retrieve.
     * @param remoteServiceRelativePath The remote service relative path of the Spring bean to
     *                                  retrieve.
     * @return The corresponding Spring bean, or null if no bean has been found for this type and
     * path.
     */
    protected Object findRpcServiceInSpringContext(ApplicationContext applicationContext,
                                                   Class<?> remoteServiceClass, String remoteServiceRelativePath) {

        return GwtSpringHelper.findRpcServiceInSpringContext(applicationContext, remoteServiceClass,
                remoteServiceRelativePath);
    }

    /**
     * Get the Spring context which as been injected in the test class.
     *
     * @return The injected Spring context.
     * @see ApplicationContextAware
     */
    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}

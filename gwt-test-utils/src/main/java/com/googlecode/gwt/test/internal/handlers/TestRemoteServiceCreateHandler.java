package com.googlecode.gwt.test.internal.handlers;

import com.google.gwt.user.client.rpc.RemoteService;
import com.googlecode.gwt.test.exceptions.GwtTestConfigurationException;
import com.googlecode.gwt.test.internal.GwtConfig;
import com.googlecode.gwt.test.internal.ModuleData;
import com.googlecode.gwt.test.rpc.RemoteServiceCreateHandler;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * GwtCreateHandler for {@link RemoteService} instances which would have been declared in the module
 * .ui.xml configuration file with the <servlet> tag.
 *
 * @author Gael Lazzari
 */
class TestRemoteServiceCreateHandler extends RemoteServiceCreateHandler {

    private static final TestRemoteServiceCreateHandler INSTANCE = new TestRemoteServiceCreateHandler();

    public static TestRemoteServiceCreateHandler get() {
        return INSTANCE;
    }

    private final Map<String, Object> cachedServices = new HashMap<>();

    public void reset() {
        cachedServices.clear();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.gwt.test.server.RemoteServiceCreateHandler#findService(java .lang .Class,
     * java.lang.String)
     */
    @Override
    protected Object findService(Class<?> remoteServiceClass, String remoteServiceRelativePath) {

        Object remoteServiceInstance = cachedServices.get(remoteServiceRelativePath);

        if (remoteServiceInstance == null) {
            remoteServiceInstance = instanciateRemoteServiceInstance(remoteServiceClass,
                    remoteServiceRelativePath);
            cachedServices.put(remoteServiceRelativePath, remoteServiceInstance);
        }

        return remoteServiceInstance;
    }

    @SuppressWarnings("unchecked")
    private <T> T instanciateRemoteServiceInstance(Class<T> remoteServiceClass,
                                                   String remoteServiceRelativePath) {

        String moduleName = GwtConfig.get().getTestedModuleName();
        Class<?> remoteServiceImplClass = ModuleData.get(moduleName).getRemoteServiceImplClass(
                remoteServiceRelativePath);

        if (remoteServiceImplClass == null) {
            return null;
        } else if (!remoteServiceClass.isAssignableFrom(remoteServiceImplClass)) {
            throw new GwtTestConfigurationException("The servlet class '"
                    + remoteServiceImplClass.getName() + "' setup for path '"
                    + remoteServiceRelativePath + "' does not implement RemoteService interface '"
                    + remoteServiceClass.getName());
        } else {
            try {
                return (T) GwtReflectionUtils.instantiateClass(remoteServiceImplClass);
            } catch (Exception e) {
                throw new GwtTestConfigurationException("Error during the instanciation of "
                        + RemoteService.class.getSimpleName() + " implementation for servlet path '"
                        + remoteServiceRelativePath + "'", e);
            }
        }

    }

}

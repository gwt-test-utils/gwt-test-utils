package com.googlecode.gwt.test.internal.handlers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.googlecode.gwt.test.exceptions.GwtTestConfigurationException;
import com.googlecode.gwt.test.internal.utils.WebXmlUtils;
import com.googlecode.gwt.test.rpc.RemoteServiceCreateHandler;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * GwtCreateHandler for {@link RemoteService} instances which would have been declared in the
 * web.xml file.
 *
 * @author Gael Lazzari
 */
class WebXmlRemoteServiceCreateHandler extends RemoteServiceCreateHandler {

    // a map with servletUrl as key and serviceImpl instance as value
    private final Map<String, Object> servicesImplMap = new HashMap<>();

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.gwt.test.server.RemoteServiceCreateHandler#findService(java .lang .Class,
     * java.lang.String)
     */
    @Override
    protected Object findService(Class<?> remoteServiceClass, String remoteServiceRelativePath) {

        String servletPath = "/" + GWT.getModuleName() + "/" + remoteServiceRelativePath;

        Object serviceImpl = servicesImplMap.get(servletPath);

        if (serviceImpl != null) {
            return serviceImpl;
        }

        String className = WebXmlUtils.get().getServletClass(servletPath);

        if (className == null) {
            return null;
        }

        try {
            serviceImpl = GwtReflectionUtils.instantiateClass(GwtReflectionUtils.getClass(className.trim()));
        } catch (ClassNotFoundException e) {
            // should not happen..
            throw new GwtTestConfigurationException(e);
        }

        // cache the implementation
        servicesImplMap.put(servletPath, serviceImpl);

        return serviceImpl;
    }

}

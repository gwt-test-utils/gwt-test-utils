package com.googlecode.gwt.test.rpc;

import com.google.gwt.user.server.rpc.AbstractRemoteServiceServlet;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Adapter for {@link ServletMockProvider}, which provides a default (returning null) implementation
 * for all its methods.
 *
 * @author Gael Lazzari
 */
public class ServletMockProviderAdapter implements ServletMockProvider {

    /*
     * (non-Javadoc)
     *
     * @see
     * com.googlecode.gwt.test.rpc.ServletMockProvider#getMockedConfig(com.google.gwt.user.server
     * .rpc.AbstractRemoteServiceServlet)
     */
    public ServletConfig getMockedConfig(AbstractRemoteServiceServlet remoteService) {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.googlecode.gwt.test.rpc.ServletMockProvider#getMockedRequest(com.google.gwt.user.server
     * .rpc.AbstractRemoteServiceServlet, java.lang.reflect.Method)
     */
    public HttpServletRequest getMockedRequest(AbstractRemoteServiceServlet remoteService,
                                               Method remoteMethod) {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.googlecode.gwt.test.rpc.ServletMockProvider#getMockedResponse(com.google.gwt.user.server
     * .rpc.AbstractRemoteServiceServlet, java.lang.reflect.Method)
     */
    public HttpServletResponse getMockedResponse(AbstractRemoteServiceServlet remoteService,
                                                 Method remoteMethod) {
        return null;
    }

}

package com.googlecode.gwt.test.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.server.rpc.AbstractRemoteServiceServlet;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Interface in charge of providing mocks of JEE's Servlet API interfaces, such as
 * {@link HttpServletRequest}, {@link HttpServletResponse}, {@link ServletConfig} which can be very
 * usefull since gwt-test-utils runs your tests without launching any servlet containers.
 *
 * @author Gael Lazzari
 */
public interface ServletMockProvider {

    /**
     * Method which substitutes {@link AbstractRemoteServiceServlet#getServletConfig()} to enable to
     * provide a mocked {@link ServletConfig} configured for your {@link RemoteService} unit test.
     *
     * @param remoteService The remote service being invoked
     * @return The mocked servlet config related to the invoked remote service
     */
    ServletConfig getMockedConfig(AbstractRemoteServiceServlet remoteService);

    /**
     * Method which substitutes {@link AbstractRemoteServiceServlet}'s getThreadLocalRequest to
     * enable to provide a mocked {@link HttpServletRequest} configured for the {@link RemoteService}
     * method being called.
     *
     * @param remoteService The remote service being invoked
     * @param remoteMethod  The remote method being invoked on the remote service instance
     * @return The mocked http request related to the invoked remote method
     */
    HttpServletRequest getMockedRequest(AbstractRemoteServiceServlet remoteService,
                                        Method remoteMethod);

    /**
     * Method which substitutes {@link AbstractRemoteServiceServlet}'s getThreadLocalResponse to
     * enable to provide a mocked {@link HttpServletResponse} configured for the
     * {@link RemoteService} method being called.
     *
     * @param remoteService The remote service being invoked
     * @param remoteMethod  The remote method being invoked on the remote service instance
     * @return The mocked http response related to the invoked remote method
     */
    HttpServletResponse getMockedResponse(AbstractRemoteServiceServlet remoteService,
                                          Method remoteMethod);

}

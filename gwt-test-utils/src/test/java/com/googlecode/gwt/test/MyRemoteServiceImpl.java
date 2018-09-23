package com.googlecode.gwt.test;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.gwt.test.rpc.MyService;

@RemoteServiceRelativePath("myRemoteService")
public class MyRemoteServiceImpl extends RemoteServiceServlet implements MyRemoteService {

    public String myMethod(String param1) {
        return param1;
    }

}
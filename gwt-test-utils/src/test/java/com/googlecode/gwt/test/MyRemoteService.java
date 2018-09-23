package com.googlecode.gwt.test;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("MyRemoteService")
public interface MyRemoteService extends RemoteService {

    String myMethod(String param1);

}
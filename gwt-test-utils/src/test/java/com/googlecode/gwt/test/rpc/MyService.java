package com.googlecode.gwt.test.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.googlecode.gwt.test.client.MyObject;

@RemoteServiceRelativePath("myService")
public interface MyService extends RemoteService {

    String getHttpRequestHeader(String header);

    void someCallWithException();

    MyObject update(MyObject object);

}
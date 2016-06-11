package com.googlecode.gwt.test.csv.data;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("myService")
public interface MyRemoteService extends RemoteService {

    String myMethod(String param1);

    MyCustomObject myMethod2(MyCustomObject object);

    void myMethod3();

}
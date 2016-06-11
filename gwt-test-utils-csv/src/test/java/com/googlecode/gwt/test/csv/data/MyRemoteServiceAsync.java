package com.googlecode.gwt.test.csv.data;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MyRemoteServiceAsync {

    void myMethod(String param1, AsyncCallback<String> callback);

    void myMethod2(MyCustomObject object, AsyncCallback<MyCustomObject> callback);

    void myMethod3(AsyncCallback<Void> callback);

}

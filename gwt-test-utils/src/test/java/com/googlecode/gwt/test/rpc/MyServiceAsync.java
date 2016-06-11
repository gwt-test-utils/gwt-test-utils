package com.googlecode.gwt.test.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.gwt.test.client.MyObject;

public interface MyServiceAsync {

    void getHttpRequestHeader(String header, AsyncCallback<String> callback);

    void someCallWithException(AsyncCallback<Void> callback);

    void update(MyObject object, AsyncCallback<MyObject> callback);

}

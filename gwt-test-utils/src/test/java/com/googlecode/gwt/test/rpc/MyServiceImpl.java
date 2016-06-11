package com.googlecode.gwt.test.rpc;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.gwt.test.client.MyChildObject;
import com.googlecode.gwt.test.client.MyObject;

@RemoteServiceRelativePath("myService")
public class MyServiceImpl extends RemoteServiceServlet implements MyService {

    private static final long serialVersionUID = 7323341628793612279L;

    public String getHttpRequestHeader(String header) {
        return getThreadLocalRequest().getHeader(header);
    }

    public void someCallWithException() {
        throw new RuntimeException("Server side thrown exception !!");
    }

    public MyObject update(MyObject object) {
        object.setMyField("updated field by server side code");
        object.setMyTransientField("this will not be serialized");

        MyChildObject childObject = new MyChildObject("this is a child !");
        childObject.setMyChildTransientField("this will not be serialized too");
        childObject.setMyField("the field inherited from the parent has been updated !");
        childObject.setMyTransientField("this field is not expected to be serialized too");

        object.getMyChildObjects().add(childObject);

        return object;
    }

}

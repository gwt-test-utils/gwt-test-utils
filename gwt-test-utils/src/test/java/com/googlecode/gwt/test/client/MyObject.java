package com.googlecode.gwt.test.client;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyObject implements IsSerializable, Serializable {

    private static final long serialVersionUID = -6459796225189554335L;

    private final List<MyChildObject> myChildObjects = new ArrayList<>();
    private String myField;
    private transient String myTransientField = "transient field";

    /**
     * Default constructor for serialization
     */
    public MyObject() {

    }

    public MyObject(String myField) {
        this.myField = myField;
    }

    public List<MyChildObject> getMyChildObjects() {
        return myChildObjects;
    }

    public String getMyField() {
        return myField;
    }

    public String getMyTransientField() {
        return myTransientField;
    }

    public void setMyField(String myField) {
        this.myField = myField;
    }

    public void setMyTransientField(String myTransientField) {
        this.myTransientField = myTransientField;
    }

}

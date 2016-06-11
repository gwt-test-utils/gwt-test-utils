package com.googlecode.gwt.test.csv.data;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MyCustomObject implements IsSerializable {

    public String myField;
    public transient String myTransientField = "transient field";

    public MyCustomObject(String myField) {
        this.myField = myField;
    }

    /**
     * Default constructor for serialization
     */
    MyCustomObject() {

    }

}

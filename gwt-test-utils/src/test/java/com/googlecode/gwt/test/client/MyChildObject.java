package com.googlecode.gwt.test.client;

import java.io.Serializable;

public class MyChildObject extends MyObject implements Serializable {

    private static final long serialVersionUID = -8359127151374995805L;

    private String myChildField;
    private transient String myChildTransientField = "child object transient field";

    public MyChildObject(String myChildField) {
        this.myChildField = myChildField;
    }

    /**
     * Default constructor for serialization
     */
    MyChildObject() {

    }

    public String getMyChildField() {
        return myChildField;
    }

    public String getMyChildTransientField() {
        return myChildTransientField;
    }

    public void setMyChildField(String myChildField) {
        this.myChildField = myChildField;
    }

    public void setMyChildTransientField(String myChildTransientField) {
        this.myChildTransientField = myChildTransientField;
    }

}

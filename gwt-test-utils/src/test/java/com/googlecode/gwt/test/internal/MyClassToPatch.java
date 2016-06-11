package com.googlecode.gwt.test.internal;

public class MyClassToPatch {

    public static class MyInnerClass {

        private String innerString;

        public MyInnerClass(String innerString) {
            this.innerString = innerString;
        }

        public String getInnerString() {
            return innerString;
        }

    }

    public String myStringMethod(MyInnerClass innerObject) throws Exception {
        throw new Exception("Method myStringMethod has not been patched");
    }

}

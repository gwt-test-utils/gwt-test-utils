package com.googlecode.gwt.test.csv.data;

import static org.junit.Assert.assertEquals;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("myService")
public class MyService implements MyRemoteService {

   public String myMethod(String param1) {
      if (param1.contains("_suffix")) {
         throw new RuntimeException();
      }
      return param1 + "_suffix";
   }

   public MyCustomObject myMethod2(MyCustomObject object) {
      assertEquals("toto", object.myField);

      object.myField = "titi";
      object.myTransientField = "this will not be serialized";
      return object;
   }

   public void myMethod3() {
      throw new NullPointerException();
   }

}

package com.googlecode.gwt.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GwtReflectionUtilsTest {

   private static class TestBean {

      private static final String CONST = "TEST BEAN CONST";
      private boolean bool;
      private String string;

      private void setBool(boolean bool) {
         this.bool = bool;
      }

      @SuppressWarnings("unused")
      private void setBoolObject(Boolean bool) {
         this.bool = bool;
      }

      private void setString(String string) {
         this.string = string;
      }
   }

   @Test
   public void callPrivateMethod() {
      // Arrange
      TestBean testBean = new TestBean();

      // Act
      GwtReflectionUtils.callPrivateMethod(testBean, "setString", "my string");

      // Assert
      assertEquals("my string", testBean.string);
   }

   @Test
   public void callPrivateMethod_Primitive() {
      // Arrange
      TestBean testBean = new TestBean();

      // Act
      GwtReflectionUtils.callPrivateMethod(testBean, "setBool", true);

      // Assert
      assertTrue(testBean.bool);
   }

   @Test
   public void callPrivateMethod_PrimitiveToWrapperType() {
      // Arrange
      TestBean testBean = new TestBean();

      // Act
      GwtReflectionUtils.callPrivateMethod(testBean, "setBoolObject", true);

      // Assert
      assertTrue(testBean.bool);
   }

   @Test
   public void getPrivateFieldValue() {
      // Arrange
      TestBean testBean = new TestBean();
      testBean.setString("my string");

      // Act
      String s = GwtReflectionUtils.getPrivateFieldValue(testBean, "string");

      // Assert
      assertEquals("my string", s);
   }

   @Test
   public void getPrivateFieldValue_Primitive() {
      // Arrange
      TestBean testBean = new TestBean();
      testBean.setBool(true);

      // Act
      boolean b = (Boolean) GwtReflectionUtils.getPrivateFieldValue(testBean, "bool");

      // Assert
      assertTrue(b);
   }

   @Test
   public void ok() {
      // Act
      String c = GwtReflectionUtils.getStaticFieldValue(TestBean.class, "CONST");

      // Assert
      assertEquals(TestBean.CONST, c);
   }

}

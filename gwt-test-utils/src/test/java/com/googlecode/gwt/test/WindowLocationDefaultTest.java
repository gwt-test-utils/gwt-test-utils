package com.googlecode.gwt.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.user.client.Window;

public class WindowLocationDefaultTest extends GwtTestTest {

   @Test
   public void defaultPath() {
      assertEquals("/index.html", Window.Location.getPath());
   }

   @Test
   public void defaultProtocol() {
      assertEquals("http:", Window.Location.getProtocol());
   }

   @Override
   protected String getHostPagePath(String moduleFullQualifiedName) {
      return null;
   }

}

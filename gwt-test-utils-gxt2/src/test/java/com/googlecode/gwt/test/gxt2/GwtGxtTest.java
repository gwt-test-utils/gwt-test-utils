package com.googlecode.gwt.test.gxt2;

import com.googlecode.gwt.test.GwtModule;

@GwtModule("com.extjs.gxt.samples.desktop.DesktopApp")
public abstract class GwtGxtTest extends GxtTest {

   @Override
   protected String getHostPagePath(String moduleFullQualifiedName) {
      return "test.html";
   }

}

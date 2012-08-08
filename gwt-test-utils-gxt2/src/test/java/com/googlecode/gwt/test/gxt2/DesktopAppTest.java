package com.googlecode.gwt.test.gxt2;

import org.junit.Test;

import com.extjs.gxt.samples.desktop.client.DesktopApp;
import com.google.gwt.core.client.GWT;

public class DesktopAppTest extends GwtGxtTest {

   @Test
   public void onModuleLoad() {
      // Arrange
      DesktopApp app = GWT.create(DesktopApp.class);

      // Act
      app.onModuleLoad();

      // Assert
   }

}

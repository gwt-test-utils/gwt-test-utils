package com.googlecode.gwt.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gwt.user.client.Window.Navigator;

public class NavigatorTest extends GwtTestTest {

   @Test
   public void getAppCodeName() {
      // Act
      String appCodeName = Navigator.getAppCodeName();

      // Assert
      assertNotNull(appCodeName);
   }

   @Test
   public void getAppName() {
      // Act
      String appName = Navigator.getAppName();

      // Assert
      assertNotNull(appName);
   }

   @Test
   public void getAppVersion() {
      // Act
      String appVersion = Navigator.getAppVersion();

      // Assert
      assertNotNull(appVersion);
   }

   @Test
   public void getPlatform() {
      // Act
      String platform = Navigator.getPlatform();

      // Assert
      assertNotNull(platform);
   }

   @Test
   public void getUserAgent() {
      // Act
      String userAgent = Navigator.getUserAgent();

      // Assert
      assertNotNull(userAgent);
   }

   @Test
   public void isCookiesEnabled() {
      // Act
      boolean isCookiesEnabled = Navigator.isCookieEnabled();

      // Assert
      assertTrue(isCookiesEnabled);
   }

   @Test
   public void isJavaEnabled() {
      // Act
      boolean isJavaEnabled = Navigator.isJavaEnabled();

      // Assert
      assertTrue(isJavaEnabled);
   }

}

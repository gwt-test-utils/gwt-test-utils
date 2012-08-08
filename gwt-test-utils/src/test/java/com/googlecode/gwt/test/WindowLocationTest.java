package com.googlecode.gwt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;

public class WindowLocationTest extends GwtTestTest {

   @Test
   public void assign() {
      // Act
      Location.assign("http://assign.location.com");

      // Assert
      assertEquals("http:", Location.getProtocol());
      assertEquals("assign.location.com", Location.getHostName());
      assertEquals("80", Location.getPort());
      assertEquals("assign.location.com:80", Location.getHost());
      assertEquals("http://assign.location.com", Location.getHref());
   }

   @Test
   public void getHash() {
      // Act
      String hash = Window.Location.getHash();

      // Assert
      assertEquals("", hash);
   }

   @Test
   public void getHashWhenHistoryTokenHasBeenSet() {
      // Arrange
      History.newItem("myToken");

      // Act
      String hash = Window.Location.getHash();

      // Assert
      assertEquals("#myToken", hash);
   }

   @Test
   public void getHref() {
      // Act
      String href = Window.Location.getHref();

      // Assert
      assertEquals("http://127.0.0.1:8888/WindowLocationTest.html", href);
   }

   @Test
   public void getHrefWhenHistoryTokenHasBeenSet() {
      // Arrange
      History.newItem("myToken");
      // Act
      String href = Window.Location.getHref();

      // Assert
      assertEquals("http://127.0.0.1:8888/WindowLocationTest.html#myToken", href);
   }

   @Test
   public void getParameter() {
      // Act
      String parameter = Window.Location.getParameter("test");

      // Assert
      assertNull(parameter);
   }

   @Test
   public void getPath() {
      // Act
      String path = Window.Location.getPath();

      // Assert
      assertEquals("/WindowLocationTest.html", path);
   }

   @Test
   public void getPathWhenHistoryTokenHasBeenSet() {
      // Arrange
      History.newItem("myToken");

      // Act
      String path = Window.Location.getPath();

      // Assert
      assertEquals("/WindowLocationTest.html", path);
   }

   @Test
   public void getPort() {
      // Act
      String port = Window.Location.getPort();

      // Assert
      assertEquals("8888", port);
   }

   @Test
   public void getProtocol() {
      // Act
      String protocol = Window.Location.getProtocol();

      // Assert
      assertEquals("http:", protocol);
   }

   @Test
   public void getQueryString() {
      // Act
      String queryString = Window.Location.getQueryString();

      // Assert
      assertEquals("", queryString);
   }

   @Test
   public void replace() {
      // Act
      Location.replace("http://replace.location.com");

      // Assert
      assertEquals("http:", Location.getProtocol());
      assertEquals("replace.location.com", Location.getHostName());
      assertEquals("80", Location.getPort());
      assertEquals("replace.location.com:80", Location.getHost());
      assertEquals("http://replace.location.com", Location.getHref());
   }

   @Override
   protected String getHostPagePath(String moduleFullQualifiedName) {
      return "fake-path/WindowLocationTest.html";
   }

}

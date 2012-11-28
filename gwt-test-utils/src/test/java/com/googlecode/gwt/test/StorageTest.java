package com.googlecode.gwt.test;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

import com.google.gwt.storage.client.Storage;

public class StorageTest extends GwtTestTest {

   @Test
   public void clearLocalStorage() {
      // Arrange
      Storage local = Storage.getLocalStorageIfSupported();
      local.setItem("test", "my test");
      local.setItem("test2", "my test 2");

      // Pre-Assert
      assertThat(local.getLength()).isEqualTo(2);

      // Act
      local.clear();

      // Assert
      assertThat(local.getLength()).isEqualTo(0);
   }

   @Test
   public void clearSessionStorage() {
      // Arrange
      Storage session = Storage.getSessionStorageIfSupported();
      session.setItem("test", "my test");
      session.setItem("test2", "my test 2");

      // Pre-Assert
      assertThat(session.getLength()).isEqualTo(2);

      // Act
      session.clear();

      // Assert
      assertThat(session.getLength()).isEqualTo(0);
   }

   @Test
   public void getLenghtLocalStorage() {
      // Arrange
      Storage local = Storage.getLocalStorageIfSupported();
      local.setItem("test", "my test");
      local.setItem("test", "my test bis");
      local.setItem("test2", "my test 2");

      // Act
      int length = local.getLength();

      // Assert
      assertThat(length).isEqualTo(2);
   }

   @Test
   public void getLengthSessionStorage() {
      // Arrange
      Storage session = Storage.getSessionStorageIfSupported();
      session.setItem("test", "my test");
      session.setItem("test", "my test bis");
      session.setItem("test2", "my test 2");

      // Act
      int length = session.getLength();

      // Assert
      assertThat(length).isEqualTo(2);
   }

   @Test
   public void itemLocalStorage() {
      // Arrange
      Storage local = Storage.getLocalStorageIfSupported();

      // Act
      local.setItem("test", "my test");

      // Assert
      assertThat(local.getItem("test")).isEqualTo("my test");
   }

   @Test
   public void itemSessionStorage() {
      // Arrange
      Storage session = Storage.getSessionStorageIfSupported();

      // Act
      session.setItem("test", "my test");

      // Assert
      assertThat(session.getItem("test")).isEqualTo("my test");
   }

   @Test
   public void keyLocalStorage() {
      // Arrange
      Storage local = Storage.getLocalStorageIfSupported();
      local.setItem("test0", "my test 0");
      local.setItem("test1", "my test 1");
      local.setItem("test2", "my test 2");

      // Act
      String key0 = local.key(0);
      String key1 = local.key(1);
      String key2 = local.key(2);

      // Assert
      assertThat(key0).isEqualTo("test0");
      assertThat(key1).isEqualTo("test1");
      assertThat(key2).isEqualTo("test2");
   }

   @Test
   public void keySessionStorage() {
      // Arrange
      Storage session = Storage.getSessionStorageIfSupported();
      session.setItem("test0", "my test 0");
      session.setItem("test1", "my test 1");
      session.setItem("test2", "my test 2");

      // Act
      String key0 = session.key(0);
      String key1 = session.key(1);
      String key2 = session.key(2);

      // Assert
      assertThat(key0).isEqualTo("test0");
      assertThat(key1).isEqualTo("test1");
      assertThat(key2).isEqualTo("test2");
   }

   @Test
   public void localStorage() {
      // Arrange
      setLocalStorageSupported(false);

      // Act & Assert
      assertThat(Storage.isLocalStorageSupported()).isFalse();

      // Arrange 2
      setLocalStorageSupported(true);

      // Act & Assert 2
      assertThat(Storage.isLocalStorageSupported()).isTrue();
   }

   @Test
   public void localStorageIsSupportedByDefault() {
      // Act
      boolean supported = Storage.isLocalStorageSupported();

      // Assert
      assertThat(supported).isTrue();
   }

   @Test
   public void sessionStorage() {
      // Arrange
      setSessionStorageSupported(false);

      // Act & Assert
      assertThat(Storage.isSessionStorageSupported()).isFalse();

      // Arrange 2
      setSessionStorageSupported(true);

      // Act & Assert 2
      assertThat(Storage.isSessionStorageSupported()).isTrue();
   }

   @Test
   public void sessionStorageIsSupportedByDefault() {
      // Act
      boolean supported = Storage.isSessionStorageSupported();

      // Assert
      assertThat(supported).isTrue();
   }

}

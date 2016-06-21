package com.googlecode.gwt.test;

import com.google.gwt.storage.client.Storage;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StorageTest extends GwtTestTest {

    @Test
    public void clearLocalStorage() {
        // Given
        Storage local = Storage.getLocalStorageIfSupported();
        local.setItem("test", "my test");
        local.setItem("test2", "my test 2");

        // Preconditions
        assertThat(local.getLength()).isEqualTo(2);

        // When
        local.clear();

        // Then
        assertThat(local.getLength()).isEqualTo(0);
    }

    @Test
    public void clearSessionStorage() {
        // Given
        Storage session = Storage.getSessionStorageIfSupported();
        session.setItem("test", "my test");
        session.setItem("test2", "my test 2");

        // Preconditions
        assertThat(session.getLength()).isEqualTo(2);

        // When
        session.clear();

        // Then
        assertThat(session.getLength()).isEqualTo(0);
    }

    @Test
    public void getLenghtLocalStorage() {
        // Given
        Storage local = Storage.getLocalStorageIfSupported();
        local.setItem("test", "my test");
        local.setItem("test", "my test bis");
        local.setItem("test2", "my test 2");

        // When
        int length = local.getLength();

        // Then
        assertThat(length).isEqualTo(2);
    }

    @Test
    public void getLengthSessionStorage() {
        // Given
        Storage session = Storage.getSessionStorageIfSupported();
        session.setItem("test", "my test");
        session.setItem("test", "my test bis");
        session.setItem("test2", "my test 2");

        // When
        int length = session.getLength();

        // Then
        assertThat(length).isEqualTo(2);
    }

    @Test
    public void itemLocalStorage() {
        // Given
        Storage local = Storage.getLocalStorageIfSupported();

        // When
        local.setItem("test", "my test");

        // Then
        assertThat(local.getItem("test")).isEqualTo("my test");
    }

    @Test
    public void itemSessionStorage() {
        // Given
        Storage session = Storage.getSessionStorageIfSupported();

        // When
        session.setItem("test", "my test");

        // Then
        assertThat(session.getItem("test")).isEqualTo("my test");
    }

    @Test
    public void keyLocalStorage() {
        // Given
        Storage local = Storage.getLocalStorageIfSupported();
        local.setItem("test0", "my test 0");
        local.setItem("test1", "my test 1");
        local.setItem("test2", "my test 2");

        // When
        String key0 = local.key(0);
        String key1 = local.key(1);
        String key2 = local.key(2);

        // Then
        assertThat(key0).isEqualTo("test0");
        assertThat(key1).isEqualTo("test1");
        assertThat(key2).isEqualTo("test2");
    }

    @Test
    public void keySessionStorage() {
        // Given
        Storage session = Storage.getSessionStorageIfSupported();
        session.setItem("test0", "my test 0");
        session.setItem("test1", "my test 1");
        session.setItem("test2", "my test 2");

        // When
        String key0 = session.key(0);
        String key1 = session.key(1);
        String key2 = session.key(2);

        // Then
        assertThat(key0).isEqualTo("test0");
        assertThat(key1).isEqualTo("test1");
        assertThat(key2).isEqualTo("test2");
    }

    @Test
    public void localStorage() {
        // Given
        setLocalStorageSupported(false);

        // When & Then
        assertThat(Storage.isLocalStorageSupported()).isFalse();

        // Given 2
        setLocalStorageSupported(true);

        // When & Then 2
        assertThat(Storage.isLocalStorageSupported()).isTrue();
    }

    @Test
    public void localStorageIsSupportedByDefault() {
        // When
        boolean supported = Storage.isLocalStorageSupported();

        // Then
        assertThat(supported).isTrue();
    }

    @Test
    public void sessionStorage() {
        // Given
        setSessionStorageSupported(false);

        // When & Then
        assertThat(Storage.isSessionStorageSupported()).isFalse();

        // Given 2
        setSessionStorageSupported(true);

        // When & Then 2
        assertThat(Storage.isSessionStorageSupported()).isTrue();
    }

    @Test
    public void sessionStorageIsSupportedByDefault() {
        // When
        boolean supported = Storage.isSessionStorageSupported();

        // Then
        assertThat(supported).isTrue();
    }

}

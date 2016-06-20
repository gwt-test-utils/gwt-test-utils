package com.googlecode.gwt.test;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WindowLocationTest extends GwtTestTest {

    @Test
    public void assign() {
        // When
        Location.assign("http://assign.location.com");

        // Then
        assertThat(Location.getProtocol()).isEqualTo("http:");
        assertThat(Location.getHostName()).isEqualTo("assign.location.com");
        assertThat(Location.getPort()).isEqualTo("80");
        assertThat(Location.getHost()).isEqualTo("assign.location.com:80");
        assertThat(Location.getHref()).isEqualTo("http://assign.location.com");
    }

    @Test
    public void getHash() {
        // When
        String hash = Window.Location.getHash();

        // Then
        assertThat(hash).isEqualTo("");
    }

    @Test
    public void getHashWhenHistoryTokenHasBeenSet() {
        // Given
        History.newItem("myToken");

        // When
        String hash = Window.Location.getHash();

        // Then
        assertThat(hash).isEqualTo("#myToken");
    }

    @Test
    public void getHref() {
        // When
        String href = Window.Location.getHref();

        // Then
        assertThat(href).isEqualTo("http://127.0.0.1:8888/WindowLocationTest.html");
    }

    @Test
    public void getHrefWhenHistoryTokenHasBeenSet() {
        // Given
        History.newItem("myToken");
        // When
        String href = Window.Location.getHref();

        // Then
        assertThat(href).isEqualTo("http://127.0.0.1:8888/WindowLocationTest.html#myToken");
    }

    @Test
    public void getParameter() {
        // When
        String parameter = Window.Location.getParameter("test");

        // Then
        assertThat(parameter).isNull();
    }

    @Test
    public void getPath() {
        // When
        String path = Window.Location.getPath();

        // Then
        assertThat(path).isEqualTo("/WindowLocationTest.html");
    }

    @Test
    public void getPathWhenHistoryTokenHasBeenSet() {
        // Given
        History.newItem("myToken");

        // When
        String path = Window.Location.getPath();

        // Then
        assertThat(path).isEqualTo("/WindowLocationTest.html");
    }

    @Test
    public void getPort() {
        // When
        String port = Window.Location.getPort();

        // Then
        assertThat(port).isEqualTo("8888");
    }

    @Test
    public void getProtocol() {
        // When
        String protocol = Window.Location.getProtocol();

        // Then
        assertThat(protocol).isEqualTo("http:");
    }

    @Test
    public void getQueryString() {
        // When
        String queryString = Window.Location.getQueryString();

        // Then
        assertThat(queryString).isEqualTo("");
    }

    @Test
    public void replace() {
        // When
        Location.replace("http://replace.location.com");

        // Then
        assertThat(Location.getProtocol()).isEqualTo("http:");
        assertThat(Location.getHostName()).isEqualTo("replace.location.com");
        assertThat(Location.getPort()).isEqualTo("80");
        assertThat(Location.getHost()).isEqualTo("replace.location.com:80");
        assertThat(Location.getHref()).isEqualTo("http://replace.location.com");
    }

    @Override
    protected String getHostPagePath(String moduleFullQualifiedName) {
        return "fake-path/WindowLocationTest.html";
    }

}

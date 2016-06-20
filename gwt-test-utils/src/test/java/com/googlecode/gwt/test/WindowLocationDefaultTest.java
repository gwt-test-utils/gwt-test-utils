package com.googlecode.gwt.test;

import com.google.gwt.user.client.Window;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WindowLocationDefaultTest extends GwtTestTest {

    @Test
    public void defaultPath() {
        assertThat(Window.Location.getPath()).isEqualTo("/index.html");
    }

    @Test
    public void defaultProtocol() {
        assertThat(Window.Location.getProtocol()).isEqualTo("http:");
    }

    @Override
    protected String getHostPagePath(String moduleFullQualifiedName) {
        return null;
    }

}

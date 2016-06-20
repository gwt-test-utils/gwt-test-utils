package com.googlecode.gwt.test;

import com.google.gwt.user.client.Cookies;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CookiesTest extends GwtTestTest {

    @Test
    public void cookies() {
        // Preconditions
        assertThat(Cookies.getCookie("test")).isNull();

        // When 1
        Cookies.setCookie("test", "test-value");

        // Then 1
        assertThat(Cookies.getCookie("test")).isEqualTo("test-value");

        // When 2
        Cookies.removeCookie("test");

        // Then 2
        assertThat(Cookies.getCookie("test")).isNull();
    }

    @Test
    public void removeCookieWhenItDoesNotExist() {
        Cookies.removeCookie("test");
        assertThat(Cookies.getCookie("test")).isNull();
    }

}

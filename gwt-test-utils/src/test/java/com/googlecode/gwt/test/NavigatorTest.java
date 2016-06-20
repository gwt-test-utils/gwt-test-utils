package com.googlecode.gwt.test;

import com.google.gwt.user.client.Window.Navigator;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NavigatorTest extends GwtTestTest {

    @Test
    public void getAppCodeName() {
        // When
        String appCodeName = Navigator.getAppCodeName();

        // Then
        assertThat(appCodeName).isNotNull();
    }

    @Test
    public void getAppName() {
        // When
        String appName = Navigator.getAppName();

        // Then
        assertThat(appName).isNotNull();
    }

    @Test
    public void getAppVersion() {
        // When
        String appVersion = Navigator.getAppVersion();

        // Then
        assertThat(appVersion).isNotNull();
    }

    @Test
    public void getPlatform() {
        // When
        String platform = Navigator.getPlatform();

        // Then
        assertThat(platform).isNotNull();
    }

    @Test
    public void getUserAgent() {
        // When
        String userAgent = Navigator.getUserAgent();

        // Then
        assertThat(userAgent).isNotNull();
    }

    @Test
    public void isCookiesEnabled() {
        // When
        boolean isCookiesEnabled = Navigator.isCookieEnabled();

        // Then
        assertThat(isCookiesEnabled).isTrue();
    }

    @Test
    public void isJavaEnabled() {
        // When
        boolean isJavaEnabled = Navigator.isJavaEnabled();

        // Then
        assertThat(isJavaEnabled).isTrue();
    }

}

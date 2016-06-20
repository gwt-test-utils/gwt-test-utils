package com.googlecode.gwt.test.internal.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GwtTestStringUtilsTest {

    @Test
    public void dehyphenize() throws Exception {
        assertThat(GwtStringUtils.dehyphenize("foo")).isEqualTo("foo");
        assertThat(GwtStringUtils.dehyphenize("foo-bar")).isEqualTo("fooBar");
    }

    @Test
    public void hyphenize() throws Exception {
        assertThat(GwtStringUtils.hyphenize("foo")).isEqualTo("foo");
        assertThat(GwtStringUtils.hyphenize("fooBar")).isEqualTo("foo-bar");
    }

    @Test
    public void treatDoubleValue() {
        assertThat(GwtStringUtils.treatDoubleValue("250px")).isEqualTo("250px");
        assertThat(GwtStringUtils.treatDoubleValue("250.1px")).isEqualTo("250.1px");
        assertThat(GwtStringUtils.treatDoubleValue("250.0px")).isEqualTo("250px");
        assertThat(GwtStringUtils.treatDoubleValue("250.1 px")).isEqualTo("250.1 px");
        assertThat(GwtStringUtils.treatDoubleValue("250.0 px")).isEqualTo("250 px");
        assertThat(GwtStringUtils.treatDoubleValue("120.20202020202021px")).isEqualTo("120.20202020202021px");
    }

    @Test
    public void testDoubleValue() {
        assertThat(GwtStringUtils.parseDouble("250px", 0)).isEqualTo(250.0);
    }
}

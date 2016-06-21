package com.googlecode.gwt.test;

import com.googlecode.gwt.test.internal.utils.ArrayUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ArrayUtilTest {

    @Test
    public void contains() {
        // Given
        String[] strings = new String[]{"test1", "test2", "test3"};

        // When & Then
        assertThat(ArrayUtils.contains(strings, "test1")).isTrue();
        assertThat(ArrayUtils.contains(strings, "test2")).isTrue();
        assertThat(ArrayUtils.contains(strings, "test3")).isTrue();
        assertThat(ArrayUtils.contains(strings, "test4")).isFalse();
    }

}

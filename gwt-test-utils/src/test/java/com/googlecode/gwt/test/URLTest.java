package com.googlecode.gwt.test;

import com.google.gwt.http.client.URL;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class URLTest extends GwtTestTest {

    @Test
    public void encodePathSegment() {
        // Given
        String decodedURLComponent = "this\\is\\encoded%2B";

        // When
        String encoded = URL.encodePathSegment(decodedURLComponent);

        assertThat(encoded).isEqualTo("this%5Cis%5Cencoded%252B");
    }

    @Test
    public void encodeQueryString() {
        // Given
        String stringToEncode = "test";

        // When & Then
        assertThat(URL.encodeQueryString(stringToEncode)).isEqualTo(stringToEncode);
    }

}

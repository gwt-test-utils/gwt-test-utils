package com.googlecode.gwt.test;

import com.google.gwt.http.client.URL;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;

public class URLTest extends GwtTestTest {

    @Test
    public void encode() {
        // Given
        String encoded = "my test.asp?name=ståle&car=saab";

        // When & Then
        assertThat(URL.encode(encoded)).isEqualTo("my%20test.asp?name=st%C3%A5le&car=saab");
    }

    @Test
    public void encodePathSegment() {
        // Given
        String decodedURLComponent = "http://w3schools.com/my test.asp?name=ståle&car=saab";

        // When & Then
        assertThat(URL.encodePathSegment(decodedURLComponent)).isEqualTo("http%3A%2F%2Fw3schools.com%2Fmy%20test.asp%3Fname%3Dst%C3%A5le%26car%3Dsaab");
    }

    @Test
    public void encodeQueryString() {
        // Given
        String stringToEncode = "name=ståle test&car=saab";

        // When & Then
        assertThat(URL.encodeQueryString(stringToEncode)).isEqualTo("name%3Dst%C3%A5le+test%26car%3Dsaab");
    }

    @Test
    public void decode() throws UnsupportedEncodingException {
        // Given
        String urlEscaped = "my%20test.asp?name=st%C3%A5le&car=saab";

        // When & Then
        assertThat(URL.decode(urlEscaped)).isEqualTo("my test.asp?name=ståle&car=saab");
    }

    @Test
    public void decodePathSegment() {
        // Given
        String urlEscaped = "http%3A%2F%2Fw3schools.com%2Fmy%20test.asp%3Fname%3Dst%C3%A5le%26car%3Dsaab";

        // When & Then
        assertThat(URL.decodePathSegment(urlEscaped)).isEqualTo("http://w3schools.com/my test.asp?name=ståle&car=saab");
    }

    @Test
    public void decodeQueryString() {
        // Given
        String stringToDecode = "name%3Dst%C3%A5le+test%26car%3Dsaab";

        // When & Then
        assertThat(URL.decodeQueryString(stringToDecode)).isEqualTo("name=ståle test&car=saab");
    }

}

package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.http.client.URL;
import com.google.gwt.safehtml.shared.UriUtils;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@PatchClass(URL.class)
class URLPatcher {

    @PatchMethod
    static String encodeImpl(String decodedURL) {
        return UriUtils.encode(decodedURL);
    }

    @PatchMethod
    static String encodePathSegmentImpl(String decodedURLComponent) {
        try {
            String url = URLEncoder.encode(decodedURLComponent, "UTF-8");
            return url.replaceAll("\\+", "%20").replaceAll("%2B", "+");
        } catch (UnsupportedEncodingException e) {
            throw new GwtTestPatchException(e);
        }
    }

    @PatchMethod
    static String encodeQueryStringImpl(String decodedURLComponent) {
        return encodePathSegmentImpl(decodedURLComponent).replaceAll("%20", "+");
    }

    @PatchMethod
    static String decodeImpl(String encodedURL) {
        try {
            return URLDecoder.decode(encodedURL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new GwtTestPatchException(e);
        }
    }

    @PatchMethod
    static String decodePathSegmentImpl(String encodedURLComponent) {
        return decodeImpl(encodedURLComponent);
    }

    @PatchMethod
    static String decodeQueryStringImpl(String encodedURLComponent) {
        return decodePathSegmentImpl(encodedURLComponent).replaceAll("\\+", "%20");
    }

}

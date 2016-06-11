package com.googlecode.gwt.test.internal.resources;

import com.google.gwt.resources.client.TextResource;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;

/**
 * Callback interface where {@link TextResource } methods calls are redirected. <strong>For internal
 * use only.</strong>
 *
 * @author Gael Lazzari
 */
class TextResourceCallback implements ResourcePrototypeCallback {

    private static interface TextReader {

        String readText() throws Exception;
    }

    private final TextReader textReader;

    TextResourceCallback(final List<URL> resourceURLs) {
        textReader = new TextReader() {

            public String readText() throws Exception {
                return TextResourceReader.get().readFiles(resourceURLs);
            }

        };
    }

    TextResourceCallback(final String text) {
        textReader = new TextReader() {

            public String readText() throws Exception {
                return text;
            }

        };
    }

    public Object call(Method method, Object[] args) throws Exception {
        if (method.getName().equals("getText")) {
            return textReader.readText();
        }

        return null;

    }

}

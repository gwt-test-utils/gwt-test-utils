package com.googlecode.gwt.test.internal.resources;

import com.googlecode.gwt.test.internal.AfterTestCallback;
import com.googlecode.gwt.test.internal.AfterTestCallbackManager;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to parse CSS files.<strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
class CssResourceReader implements AfterTestCallback {

    public static class CssParsingResult {

        private final Map<String, String> cssConstants;

        private CssParsingResult(Map<String, String> cssConstants) {
            this.cssConstants = cssConstants;
        }

        /**
         * Returns the value of the css constant passed as parameter
         *
         * @param name The name of the constant to retrive the value
         * @return The value of the corresponding constants, or null if it does not exist
         */
        public String getConstantValue(String name) {
            return cssConstants.get(name);
        }
    }

    private static final Pattern CSS_CONSTANT_PATTERN = Pattern.compile("^\\s*@def (\\S+)\\s+(\\S+)\\s*$");

    private static final CssResourceReader INSTANCE = new CssResourceReader();

    public static CssResourceReader get() {
        return INSTANCE;
    }

    private final Map<URL, CssParsingResult> cache;

    private CssResourceReader() {
        cache = new HashMap<>();
        AfterTestCallbackManager.get().registerCallback(this);
    }

    @Override
    public void afterTest() {
        cache.clear();
    }

    public CssParsingResult readCss(List<URL> urls) throws IOException {

        Map<String, String> cssConstants = new HashMap<>();

        for (URL url : urls) {
            cssConstants.putAll(readCssFile(url).cssConstants);
        }

        return new CssParsingResult(cssConstants);
    }

    public CssParsingResult readCss(String text) throws IOException {
        return parse(new StringReader(text));
    }

    public CssParsingResult readCssFile(URL url) throws IOException {

        CssParsingResult cssConstants = cache.get(url);
        if (cssConstants == null) {
            cssConstants = parse(new InputStreamReader(url.openStream(), "UTF-8"));
            cache.put(url, cssConstants);
        }

        return cssConstants;
    }

    private CssParsingResult parse(Reader reader) throws IOException {

        Map<String, String> cssConstants = new HashMap<>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(reader);

            String line;
            Matcher m;
            while ((line = br.readLine()) != null) {
                m = CSS_CONSTANT_PATTERN.matcher(line);
                if (m.matches()) {
                    cssConstants.put(m.group(1), m.group(2));
                }
            }

            return new CssParsingResult(cssConstants);

        } finally {
            if (br != null) {
                br.close();
            }
        }
    }

}

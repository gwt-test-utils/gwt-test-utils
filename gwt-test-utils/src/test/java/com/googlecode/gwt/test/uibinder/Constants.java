package com.googlecode.gwt.test.uibinder;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * Used to test static imports in UiBinder templates.
 */
public class Constants {
    /**
     * Used to test a wildcard import.
     */
    public static class Inner {
        public static String CONST_BAR = "Bar";
        protected static String PROTECTED = "protected";
        static String CONST_BAZ = "Baz";
    }

    /**
     * Tests enum imports.
     */
    public enum MyEnum {
        ENUM_1, ENUM_2;
    }

    public static String CONST_FOO = "Foo";

    public static final DateTimeFormat MY_DATE_FORMAT = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);

    public String getRendererText() {
        return "<b>Here's the text!</b>";
    }

    public SafeHtml getSafeHtml() {
        return SafeHtmlUtils.fromSafeConstant("<b>This text should be bold!</b>");
    }

    public String getText() {
        return "<b>This text won't be bold!</b>";
    }
}

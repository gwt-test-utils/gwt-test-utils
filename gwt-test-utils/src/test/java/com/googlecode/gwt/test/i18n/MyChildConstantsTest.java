package com.googlecode.gwt.test.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class MyChildConstantsTest extends GwtTestTest {

    private MyChildConstants childConstants;

    @Before
    public void beforeMyChildConstantsTest() {
        childConstants = GWT.create(MyChildConstants.class);
        setLocale(Locale.ENGLISH);
    }

    @Test
    public void childConstant() {
        // Act
        SafeHtml hello = childConstants.hello();
        String valueWithoutDefaultAnnotationInChild = childConstants.valueWithoutDefaultAnnotationInChild();
        String valueWithoutLocale = childConstants.valueWithoutLocale();
        String valueWithoutLocaleToBeOverride = childConstants.valueWithoutLocaleToBeOverride();

        // Assert
        assertEquals("Hello english !", hello.asString());
        assertEquals("Value in child default .properties", valueWithoutDefaultAnnotationInChild);
        assertEquals("Value from a default .properties file, without locale", valueWithoutLocale);
        assertEquals("Value overriden by child in default .properties",
                valueWithoutLocaleToBeOverride);
    }

}

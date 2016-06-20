package com.googlecode.gwt.test.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class MyChildChildConstantsTest extends GwtTestTest {

    private MyChildChildConstants childChildConstants;

    @Before
    public void beforeMyChildConstantsTest() {
        childChildConstants = GWT.create(MyChildChildConstants.class);
        setLocale(Locale.ENGLISH);
    }

    @Test
    public void childChildConstant() {
        // When
        SafeHtml hello = childChildConstants.hello();
        String valueWithoutDefaultAnnotationInChild = childChildConstants.valueWithoutDefaultAnnotationInChild();
        String valueWithoutLocale = childChildConstants.valueWithoutLocale();
        String valueWithoutLocaleToBeOverride = childChildConstants.valueWithoutLocaleToBeOverride();

        // Then
        assertThat(hello.asString()).isEqualTo("Hello english !");
        assertThat(valueWithoutDefaultAnnotationInChild).isEqualTo("Value in child default .properties");
        assertThat(valueWithoutLocale).isEqualTo("Value from a default .properties file, without locale");
        assertThat(valueWithoutLocaleToBeOverride).isEqualTo("Value overriden by child in default .properties");
    }

}

package com.googlecode.gwt.test.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class MyChildConstantsTest extends GwtTestTest {

    private MyChildConstants childConstants;

    @Before
    public void beforeMyChildConstantsTest() {
        childConstants = GWT.create(MyChildConstants.class);
        setLocale(Locale.ENGLISH);
    }

    @Test
    public void childConstant() {
        // When
        SafeHtml hello = childConstants.hello();
        String valueWithoutDefaultAnnotationInChild = childConstants.valueWithoutDefaultAnnotationInChild();
        String valueWithoutLocale = childConstants.valueWithoutLocale();
        String valueWithoutLocaleToBeOverride = childConstants.valueWithoutLocaleToBeOverride();

        // Then
        assertThat(hello.asString()).isEqualTo("Hello english !");
        assertThat(valueWithoutDefaultAnnotationInChild).isEqualTo("Value in child default .properties");
        assertThat(valueWithoutLocale).isEqualTo("Value from a default .properties file, without locale");
        assertThat(valueWithoutLocaleToBeOverride).isEqualTo("Value overriden by child in default .properties");
    }

}

package com.googlecode.gwt.test.template;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SafeHtmlTest extends GwtTestTest {

    private static MySafeHtmlTemplate TEMPLATE = GWT.create(MySafeHtmlTemplate.class);

    @Test
    public void template() {
        // Given
        SafeHtmlBuilder builder = new SafeHtmlBuilder();
        builder.appendEscaped("this is a test");

        // When
        SafeHtml result = TEMPLATE.div(builder.toSafeHtml());

        // Then
        assertThat(result.asString()).isEqualTo("<div style=\"outline:none;\">this is a test</div>");
    }

}

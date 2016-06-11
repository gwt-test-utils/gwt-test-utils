package com.googlecode.gwt.test.template;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SafeHtmlTest extends GwtTestTest {

    private static MySafeHtmlTemplate TEMPLATE = GWT.create(MySafeHtmlTemplate.class);

    @Test
    public void template() {
        // Arrange
        SafeHtmlBuilder builder = new SafeHtmlBuilder();
        builder.appendEscaped("this is a test");

        // Act
        SafeHtml result = TEMPLATE.div(builder.toSafeHtml());

        // Assert
        assertEquals("<div style=\"outline:none;\">this is a test</div>", result.asString());
    }

}

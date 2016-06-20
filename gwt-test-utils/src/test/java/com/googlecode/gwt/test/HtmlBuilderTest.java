package com.googlecode.gwt.test;

import com.google.gwt.dom.builder.shared.HtmlBuilderFactory;
import com.google.gwt.dom.builder.shared.HtmlDivBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HtmlBuilderTest extends GwtTestTest {

    @Test
    public void divWithCamelCaseStyle() throws Exception {
        // Given
        HtmlDivBuilder divBuilder = HtmlBuilderFactory.get().createDivBuilder();

        // When
        divBuilder.style().trustedProperty("propertyName", "my value");

        // Then
        assertThat(divBuilder.asSafeHtml().asString()).isEqualTo(
                "<div style=\"property-name:my value;\"></div>");
    }
}

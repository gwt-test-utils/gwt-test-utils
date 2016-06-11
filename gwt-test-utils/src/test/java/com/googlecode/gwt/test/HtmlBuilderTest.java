package com.googlecode.gwt.test;

import com.google.gwt.dom.builder.shared.HtmlBuilderFactory;
import com.google.gwt.dom.builder.shared.HtmlDivBuilder;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class HtmlBuilderTest extends GwtTestTest {

    @Test
    public void divWithCamelCaseStyle() throws Exception {
        // Arrange
        HtmlDivBuilder divBuilder = HtmlBuilderFactory.get().createDivBuilder();

        // Act
        divBuilder.style().trustedProperty("propertyName", "my value");

        // assert
        assertThat(divBuilder.asSafeHtml().asString()).isEqualTo(
                "<div style=\"property-name:my value;\"></div>");
    }
}

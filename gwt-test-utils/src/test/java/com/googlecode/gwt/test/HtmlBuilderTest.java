package com.googlecode.gwt.test;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

import com.google.gwt.dom.builder.shared.HtmlBuilderFactory;
import com.google.gwt.dom.builder.shared.HtmlDivBuilder;

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

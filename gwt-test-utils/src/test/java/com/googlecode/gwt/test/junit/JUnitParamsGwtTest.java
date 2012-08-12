package com.googlecode.gwt.test.junit;

import static org.fest.assertions.api.Assertions.assertThat;
import junitparams.Parameters;

import org.junit.Test;

import com.googlecode.gwt.test.GwtTestTest;

public class JUnitParamsGwtTest extends GwtTestTest {

   @Test
   @Parameters({"John, text : John", "Loke, text : Loke"})
   public void junitParams(String textToSet, String textToAssert) throws Exception {
      // Arrange
      JUnitParamsWidget w = new JUnitParamsWidget();

      // Act
      w.setText(textToSet);

      // Assert
      assertThat(w.getText()).isEqualTo(textToAssert);
   }

}

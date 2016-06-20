package com.googlecode.gwt.test.junit;

import com.googlecode.gwt.test.GwtTestTest;
import junitparams.Parameters;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JUnitParamsGwtTest extends GwtTestTest {

    @Test
    @Parameters({"John, text : John", "Loke, text : Loke"})
    public void junitParams(String textToSet, String textToAssert) throws Exception {
        // Given
        JUnitParamsWidget w = new JUnitParamsWidget();

        // When
        w.setText(textToSet);

        // Then
        assertThat(w.getText()).isEqualTo(textToAssert);
    }

}

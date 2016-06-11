package com.googlecode.gwt.test.i18n;

import com.google.gwt.i18n.client.NumberFormat;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class NumberFormatTest extends GwtTestTest {

    @Test
    public void numberFormat_Fr() throws Exception {
        // Arrange
        setLocale(Locale.FRENCH);

        // Act & Assert
        assertEquals("10,00 €", NumberFormat.getCurrencyFormat().format(10));
        assertEquals("3,142", NumberFormat.getDecimalFormat().format(3.1416));
    }

    @Test
    public void numberFormat_SpecificPattern() {
        // Arrange
        setLocale(Locale.FRENCH);
        NumberFormat numberFormat = NumberFormat.getFormat("0000000000");

        // Act
        String numberString = numberFormat.format(1234);

        // Assert
        assertEquals("0000001234", numberString);
    }

    @Test
    public void numberFormat_SpecificPatternWithDouble() {
        // Arrange
        setLocale(Locale.FRENCH);
        NumberFormat numberFormat = NumberFormat.getFormat("0000000000");

        // Act
        String numberString = numberFormat.format(42147482);

        // Assert
        assertEquals("0042147482", numberString);
    }

    @Test
    public void numberFormat_Us() {
        // Arrange
        setLocale(Locale.ENGLISH);

        // Act & Assert
        assertEquals("$10.00", NumberFormat.getCurrencyFormat().format(10));
        assertEquals("3.142", NumberFormat.getDecimalFormat().format(3.1416));
    }

}

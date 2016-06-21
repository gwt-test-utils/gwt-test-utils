package com.googlecode.gwt.test.i18n;

import com.google.gwt.i18n.client.NumberFormat;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class NumberFormatTest extends GwtTestTest {

    @Test
    public void numberFormat_Fr() throws Exception {
        // Given
        setLocale(Locale.FRENCH);

        // When & Then
        assertThat(NumberFormat.getCurrencyFormat().format(10)).isEqualTo("10,00 €");
        assertThat(NumberFormat.getDecimalFormat().format(3.1416)).isEqualTo("3,142");
    }

    @Test
    public void numberFormat_SpecificPattern() {
        // Given
        setLocale(Locale.FRENCH);
        NumberFormat numberFormat = NumberFormat.getFormat("0000000000");

        // When
        String numberString = numberFormat.format(1234);

        // Then
        assertThat(numberString).isEqualTo("0000001234");
    }

    @Test
    public void numberFormat_SpecificPatternWithDouble() {
        // Given
        setLocale(Locale.FRENCH);
        NumberFormat numberFormat = NumberFormat.getFormat("0000000000");

        // When
        String numberString = numberFormat.format(42147482);

        // Then
        assertThat(numberString).isEqualTo("0042147482");
    }

    @Test
    public void numberFormat_Us() {
        // Given
        setLocale(Locale.ENGLISH);

        // When & Then
        assertThat(NumberFormat.getCurrencyFormat().format(10)).isEqualTo("$10.00");
        assertThat(NumberFormat.getDecimalFormat().format(3.1416)).isEqualTo("3.142");
    }

}

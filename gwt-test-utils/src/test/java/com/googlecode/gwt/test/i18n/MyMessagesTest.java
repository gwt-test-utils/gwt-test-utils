package com.googlecode.gwt.test.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class MyMessagesTest extends GwtTestTest {

    private MyMessages messages;

    @Test
    public void a_message() {
        // Given
        String result = null;
        String expectedException = "Unable to find a Locale specific resource file to bind with i18n interface 'com.googlecode.gwt.test.i18n.MyMessages' and there is no @DefaultXXXValue annotation on 'a_message' called method";

        // When 1
        try {
            result = messages.a_message("Gael", 23, true);
            fail("The test is expected to throw an execption since 'a_message' can't be retrieve in the default property file and no @DefaultMessage is set on the method");
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo(expectedException);

        }

        // Given 2
        setLocale(Locale.FRANCE);

        // When 2
        result = messages.a_message("Gael", 23, true);

        // Then2
        assertThat(result).isEqualTo("Bonjour Gael, vous avez saisi le nombre 23 et le booléen true");
    }

    @Before
    public void beforeMyMessages() {
        messages = GWT.create(MyMessages.class);
    }

    @Test
    public void meaningAnnotation() {
        // Given 1
        setLocale(Locale.FRANCE);

        // When 1
        String orangeColor = messages.orangeColor();
        SafeHtml orangeFruit = messages.orangeFruit();

        // Then 1
        assertThat(orangeColor).isEqualTo("Orange");
        assertThat(orangeFruit.asString()).isEqualTo("Orange");

        // Given 2
        setLocale(Locale.ENGLISH);

        // When 2
        orangeColor = messages.orangeColor();
        orangeFruit = messages.orangeFruit();

        // Then 2
        assertThat(orangeColor).isEqualTo("orange");
        assertThat(orangeFruit.asString()).isEqualTo("orange");

    }

    @Test
    public void totalAmount() {
        // Given 1
        setLocale(Locale.US);

        // When
        String totalAmount = messages.totalAmount(6);

        // Then 1
        assertThat(totalAmount).isEqualTo("Your cart total is $6.00");

        // Given 2
        setLocale(Locale.FRANCE);

        // When 2
        totalAmount = messages.totalAmount(6);

        // Then 2
        assertThat(totalAmount).isEqualTo("Le total de votre panier est de 6,00\u00A0€");
    }

    @Test
    public void widgetCount_default_en() {
        // When
        String result0 = messages.widgetCount(0);
        String result1 = messages.widgetCount(1);
        String result2 = messages.widgetCount(2);

        // Then
        assertThat(result0).isEqualTo("You have 0 widgets");
        assertThat(result1).isEqualTo("You have 1 widget");
        assertThat(result2).isEqualTo("You have 2 widgets");
    }

    // @Test
    // public void checkAlternateMessageWithSelect_default_en() {
    // // When
    // String resultFEMALE = messages.alternateMessageWithSelect("Jenny",
    // Gender.FEMALE);
    // String resultMALE = messages.alternateMessageWithSelect("Brian",
    // Gender.MALE);
    // String resultUNKNOWN = messages.alternateMessageWithSelect("Gloups",
    // Gender.UNKNOWN);
    //
    // // Then
    // ThenEquals("Jenny likes her widgets.", resultFEMALE);
    // ThenEquals("Brian likes his widgets.", resultMALE);
    // ThenEquals("Gloups likes their widgets.", resultUNKNOWN);
    // }
    //
    // @Test
    // public void checkAlternateMessageWithSelect_fr() {
    // // Given
    // setLocale(Locale.FRANCE);
    //
    // // When
    // String resultFEMALE = messages.alternateMessageWithSelect("Jenny",
    // Gender.FEMALE);
    // String resultMALE = messages.alternateMessageWithSelect("Brian",
    // Gender.MALE);
    // String resultUNKNOWN = messages.alternateMessageWithSelect("Gloups",
    // Gender.UNKNOWN);
    //
    // // Then
    // ThenEquals("Jenny aime sa poupée", resultFEMALE);
    // ThenEquals("Brian aime son dinosaure", resultMALE);
    // ThenEquals("Gloups aime son nonosse", resultUNKNOWN);
    // }
    //
    // @Test
    // public void checkAlternateMessageWithSelectAndPluralCount_default_en() {
    // // When
    // String resultFEMALE_ONE =
    // messages.alternateMessageWithSelectAndPluralCount("Jenny", Gender.FEMALE,
    // 1);
    // String resultFEMALE_MANY =
    // messages.alternateMessageWithSelectAndPluralCount("Jenny", Gender.FEMALE,
    // 4);
    // String resultMALE_ONE =
    // messages.alternateMessageWithSelectAndPluralCount("Brian", Gender.MALE,
    // 1);
    // String resultMALE_MANY =
    // messages.alternateMessageWithSelectAndPluralCount("Brian", Gender.MALE,
    // 2);
    // String resultUNKNOWN =
    // messages.alternateMessageWithSelectAndPluralCount("Gloups",
    // Gender.UNKNOWN,
    // 0);
    //
    // // Then
    // ThenEquals("Jenny gave away her widget", resultFEMALE_ONE);
    // ThenEquals("Jenny gave away her 4 widgets", resultFEMALE_MANY);
    // ThenEquals("Brian gave away his widget", resultMALE_ONE);
    // ThenEquals("Brian gave away his 2 widgets", resultMALE_MANY);
    // ThenEquals("Gloups gave away their 0 widgets", resultUNKNOWN);
    // }
    //
    // @Test
    // public void checkAlternateMessageWithSelectAndPluralCount_fr() {
    // // Given
    // setLocale(Locale.FRANCE);
    //
    // // When
    // String resultFEMALE_ONE =
    // messages.alternateMessageWithSelectAndPluralCount("Jenny", Gender.FEMALE,
    // 1);
    // String resultFEMALE_MANY =
    // messages.alternateMessageWithSelectAndPluralCount("Jenny", Gender.FEMALE,
    // 4);
    // String resultMALE_ONE =
    // messages.alternateMessageWithSelectAndPluralCount("Brian", Gender.MALE,
    // 1);
    // String resultMALE_MANY =
    // messages.alternateMessageWithSelectAndPluralCount("Brian", Gender.MALE,
    // 2);
    // String resultUNKNOWN =
    // messages.alternateMessageWithSelectAndPluralCount("Gloups",
    // Gender.UNKNOWN,
    // 0);
    //
    // // Then
    // ThenEquals("Jenny aime sa poupée", resultFEMALE_ONE);
    // ThenEquals("Jenny aime ses 4 poupées", resultFEMALE_MANY);
    // ThenEquals("Brian aime son dinosaure", resultMALE_ONE);
    // ThenEquals("Brian aime ses 2 dinosaures", resultMALE_MANY);
    // ThenEquals("Gloups gave away their 0 widgets", resultUNKNOWN);
    // }

    @Test
    public void widgetCount_fr() {
        // Given
        setLocale(Locale.FRANCE);

        // When
        String result0 = messages.widgetCount(0);
        String result1 = messages.widgetCount(1);
        String result2 = messages.widgetCount(2);

        // Then
        assertThat(result0).isEqualTo("Vous avez 0 widget");
        assertThat(result1).isEqualTo("Vous avez 1 widget");
        assertThat(result2).isEqualTo("Vous avez 2 widgets");
    }
}

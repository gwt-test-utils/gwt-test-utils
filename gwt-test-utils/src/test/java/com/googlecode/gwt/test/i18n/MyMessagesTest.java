package com.googlecode.gwt.test.i18n;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.googlecode.gwt.test.GwtTestTest;

public class MyMessagesTest extends GwtTestTest {

   private MyMessages messages;

   @Test
   public void a_message() {
      // Arrange
      String result = null;
      String expectedException = "Unable to find a Locale specific resource file to bind with i18n interface 'com.googlecode.gwt.test.i18n.MyMessages' and there is no @DefaultXXXValue annotation on 'a_message' called method";

      // Act 1
      try {
         result = messages.a_message("Gael", 23, true);
         fail("The test is expected to throw an execption since 'a_message' can't be retrieve in the default property file and no @DefaultMessage is set on the method");
      } catch (Exception e) {
         assertEquals(expectedException, e.getMessage());

      }

      // Arrange 2
      setLocale(Locale.FRANCE);

      // Act 2
      result = messages.a_message("Gael", 23, true);

      // Assert2
      assertEquals("Bonjour Gael, vous avez saisi le nombre 23 et le booléen true", result);
   }

   @Before
   public void beforeMyMessages() {
      messages = GWT.create(MyMessages.class);
   }

   @Test
   public void meaningAnnotation() {
      // Arrange 1
      setLocale(Locale.FRANCE);

      // Act 1
      String orangeColor = messages.orangeColor();
      SafeHtml orangeFruit = messages.orangeFruit();

      // Assert 1
      assertEquals("Orange", orangeColor);
      assertEquals("Orange", orangeFruit.asString());

      // Arrange 2
      setLocale(Locale.ENGLISH);

      // Act 2
      orangeColor = messages.orangeColor();
      orangeFruit = messages.orangeFruit();

      // Assert 2
      assertEquals("orange", orangeColor);
      assertEquals("orange", orangeFruit.asString());

   }

   @Test
   public void totalAmount() {
      // Arrange 1
      setLocale(Locale.US);

      // Act
      String totalAmount = messages.totalAmount(6);

      // Assert 1
      assertEquals("Your cart total is $6.00", totalAmount);

      // Arrange 2
      setLocale(Locale.FRANCE);

      // Act 2
      totalAmount = messages.totalAmount(6);

      // Assert 2
      assertEquals("Le total de votre panier est de 6,00 €", totalAmount);
   }

   @Test
   public void widgetCount_default_en() {
      // Act
      String result0 = messages.widgetCount(0);
      String result1 = messages.widgetCount(1);
      String result2 = messages.widgetCount(2);

      // Assert
      assertEquals("You have 0 widgets", result0);
      assertEquals("You have 1 widget", result1);
      assertEquals("You have 2 widgets", result2);
   }

   // @Test
   // public void checkAlternateMessageWithSelect_default_en() {
   // // Act
   // String resultFEMALE = messages.alternateMessageWithSelect("Jenny",
   // Gender.FEMALE);
   // String resultMALE = messages.alternateMessageWithSelect("Brian",
   // Gender.MALE);
   // String resultUNKNOWN = messages.alternateMessageWithSelect("Gloups",
   // Gender.UNKNOWN);
   //
   // // Assert
   // assertEquals("Jenny likes her widgets.", resultFEMALE);
   // assertEquals("Brian likes his widgets.", resultMALE);
   // assertEquals("Gloups likes their widgets.", resultUNKNOWN);
   // }
   //
   // @Test
   // public void checkAlternateMessageWithSelect_fr() {
   // // Arrange
   // setLocale(Locale.FRANCE);
   //
   // // Act
   // String resultFEMALE = messages.alternateMessageWithSelect("Jenny",
   // Gender.FEMALE);
   // String resultMALE = messages.alternateMessageWithSelect("Brian",
   // Gender.MALE);
   // String resultUNKNOWN = messages.alternateMessageWithSelect("Gloups",
   // Gender.UNKNOWN);
   //
   // // Assert
   // assertEquals("Jenny aime sa poupée", resultFEMALE);
   // assertEquals("Brian aime son dinosaure", resultMALE);
   // assertEquals("Gloups aime son nonosse", resultUNKNOWN);
   // }
   //
   // @Test
   // public void checkAlternateMessageWithSelectAndPluralCount_default_en() {
   // // Act
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
   // // Assert
   // assertEquals("Jenny gave away her widget", resultFEMALE_ONE);
   // assertEquals("Jenny gave away her 4 widgets", resultFEMALE_MANY);
   // assertEquals("Brian gave away his widget", resultMALE_ONE);
   // assertEquals("Brian gave away his 2 widgets", resultMALE_MANY);
   // assertEquals("Gloups gave away their 0 widgets", resultUNKNOWN);
   // }
   //
   // @Test
   // public void checkAlternateMessageWithSelectAndPluralCount_fr() {
   // // Arrange
   // setLocale(Locale.FRANCE);
   //
   // // Act
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
   // // Assert
   // assertEquals("Jenny aime sa poupée", resultFEMALE_ONE);
   // assertEquals("Jenny aime ses 4 poupées", resultFEMALE_MANY);
   // assertEquals("Brian aime son dinosaure", resultMALE_ONE);
   // assertEquals("Brian aime ses 2 dinosaures", resultMALE_MANY);
   // assertEquals("Gloups gave away their 0 widgets", resultUNKNOWN);
   // }

   @Test
   public void widgetCount_fr() {
      // Arrange
      setLocale(Locale.FRANCE);

      // Act
      String result0 = messages.widgetCount(0);
      String result1 = messages.widgetCount(1);
      String result2 = messages.widgetCount(2);

      // Assert
      assertEquals("Vous avez 0 widget", result0);
      assertEquals("Vous avez 1 widget", result1);
      assertEquals("Vous avez 2 widgets", result2);
   }
}

package com.googlecode.gwt.test.i18n;

import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

@DefaultLocale("en")
public interface MyMessages extends Messages {

    public enum Gender {
        FEMALE, MALE, UNKNOWN,
    }

    String a_message(String name, int number, boolean bool);

    @Key("1234")
    @DefaultMessage("This is a plain string.")
    String oneTwoThreeFour();

    // @DefaultMessage("{0} likes their widgets.")
    // @AlternateMessage({ "FEMALE", "{0} likes her widgets.", "MALE",
    // "{0} likes his widgets.", })
    // String alternateMessageWithSelect(String name, @Select Gender gender);
    //
    // @DefaultMessage("{0} gave away their {2} widgets")
    // @AlternateMessage({ "MALE|other", "{0} gave away his {2} widgets",
    // "FEMALE|other", "{0} gave away her {2} widgets", "MALE|one",
    // "{0} gave away his widget", "FEMALE|one", "{0} gave away her widget",
    // "other|one", "{0} gave away their widget", })
    // String alternateMessageWithSelectAndPluralCount(String name, @Select
    // Gender
    // gender, @PluralCount int count);

    @DefaultMessage("No reference to the argument")
    String optionalArg(@Optional
                               String ignored);

    @Meaning("the color")
    @DefaultMessage("orange")
    String orangeColor();

    @Meaning("the fruit")
    @DefaultMessage("orange")
    SafeHtml orangeFruit();

    @DefaultMessage("Your cart total is {0,number,currency}")
    @Description("The total value of the items in the shopping cart in local currency")
    String totalAmount(@Example("$5.00")
                               double amount);

    @SuppressWarnings("deprecation")
    @DefaultMessage("You have {0} widgets")
    @PluralText({"one", "You have {0} widget"})
    String widgetCount(@PluralCount
                               int count);
}

package com.googlecode.gwt.test.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class MyConstantsTest extends GwtTestTest {

    private MyConstants constants;

    @Before
    public void beforeMyConstantsTest() {
        constants = GWT.create(MyConstants.class);
    }

    @Test
    public void changeLocale() {
        // Given
        setLocale(Locale.ENGLISH);

        // When 1
        SafeHtml hello = constants.hello();
        String goodbye = constants.goodbye();
        String[] stringArray = constants.stringArray();
        Map<String, String> map = constants.map();
        String valueWithoutLocale = constants.valueWithoutLocale();
        String valueWithoutLocaleToBeOverride = constants.valueWithoutLocaleToBeOverride();
        String messageWithKey = constants.messageWithKey();

        // Then 1
        assertThat(hello.asString()).isEqualTo("Hello english !");
        assertThat(goodbye).isEqualTo("Goodbye english !");

        assertThat(stringArray.length).isEqualTo(3);
        assertThat(stringArray[0]).isEqualTo("one");
        assertThat(stringArray[1]).isEqualTo("two");
        assertThat(stringArray[2]).isEqualTo("three");

        assertThat(map).hasSize(5);
        assertThat(map.get("hello")).isEqualTo("Hello english !");
        assertThat(map.get("goodbye")).isEqualTo("Goodbye english !");
        assertThat(map.get("map1")).isEqualTo("glad to work with gwt-test-utils");
        assertThat(map.get("map2")).isEqualTo("hehe, it roxs !");
        assertThat(map.get("map3")).isNull();

        assertThat(valueWithoutLocale).isEqualTo("Value from a default .properties file, without locale");
        assertThat(valueWithoutLocaleToBeOverride).isEqualTo("Value from parent default .properties");

        // @Key
        assertThat(messageWithKey).isEqualTo("Message with key english");

        // When 2
        setLocale(Locale.US);
        hello = constants.hello();
        goodbye = constants.goodbye();
        stringArray = constants.stringArray();
        map = constants.map();
        messageWithKey = constants.messageWithKey();

        // Then 2
        assertThat(hello.asString()).isEqualTo("Hello US !");
        assertThat(goodbye).isEqualTo("Goodbye US !");

        assertThat(stringArray.length).isEqualTo(4);
        assertThat(stringArray[0]).isEqualTo("one");
        assertThat(stringArray[1]).isEqualTo("two");
        assertThat(stringArray[2]).isEqualTo("three");
        assertThat(stringArray[3]).isEqualTo("four");

        // "map" is not present in MyConstants_en_US.properties : loaded from
        // @DefaultStringMapMapValue
        assertThat(map).hasSize(3);
        assertThat(map.get("map1")).isEqualTo("default map1 value");
        assertThat(map.get("map2")).isEqualTo("default map2 value");
        assertThat(map.get("map3")).isEqualTo("default map3 value");

        // @Key
        assertThat(messageWithKey).isEqualTo("Message with key US");
    }

    @Test
    public void defaultValues() {
        // Given
        String expectedErrorMessage = "No matching property \"goodbye\" for Constants class [com.googlecode.gwt.test.i18n.MyConstants]. Please check the corresponding properties files or use @DefaultStringValue";

        // When 1
        SafeHtml hello = constants.hello();
        String[] stringArray = constants.stringArray();
        Map<String, String> map = constants.map();
        int functionInt = constants.functionInt();
        double functionDouble = constants.functionDouble();
        float functionFloat = constants.functionFloat();
        boolean functionBoolean = constants.functionBoolean();

        // Then
        assertThat(hello.asString()).isEqualTo("hello from @DefaultStringValue");
        assertThat(stringArray.length).isEqualTo(2);
        assertThat(stringArray[0]).isEqualTo("default0");
        assertThat(stringArray[1]).isEqualTo("default1");
        assertThat(map).hasSize(3);
        assertThat(map.get("map1")).isEqualTo("default map1 value");
        assertThat(map.get("map2")).isEqualTo("default map2 value");
        assertThat(map.get("map3")).isEqualTo("default map3 value");

        assertThat(functionInt).isEqualTo(6);
        assertThat(functionDouble).isCloseTo(6.6, within(new Double(0)));
        assertThat(functionFloat).isCloseTo((float) 6.66, within(new Float(0)));
        assertThat(functionBoolean).isTrue();

        // When 2 : no @DefaultStringValue
        try {
            constants.goodbye();
            fail("i18n patching mechanism should throw an exception if no locale, no default .properties file and no @DefaultStringValue is set");
        } catch (Exception e) {
            // Then 2
            assertThat(e.getMessage()).isEqualTo(expectedErrorMessage);
        }
    }

    @Test
    public void specialChars() {
        setLocale(Locale.FRENCH);

        // When
        SafeHtml hello = constants.hello();
        String goodbye = constants.goodbye();
        String[] stringArray = constants.stringArray();
        Map<String, String> map = constants.map();
        int functionInt = constants.functionInt();
        double functionDouble = constants.functionDouble();
        float functionFloat = constants.functionFloat();
        boolean functionBoolean = constants.functionBoolean();

        // Then
        assertThat(hello.asString()).isEqualTo("Bonjour");
        assertThat(goodbye).isEqualTo("Au revoir et un caractère qui pue");

        assertThat(stringArray.length).isEqualTo(3);
        assertThat(stringArray[0]).isEqualTo("un");
        assertThat(stringArray[1]).isEqualTo("deux");
        assertThat(stringArray[2]).isEqualTo("trois");

        assertThat(map).hasSize(5);
        assertThat(map.get("hello")).isEqualTo("Bonjour");
        assertThat(map.get("goodbye")).isEqualTo("Au revoir et un caractère qui pue");
        assertThat(map.get("map1")).isEqualTo("je suis content");
        assertThat(map.get("map2")).isEqualTo("tout pareil !");
        assertThat(map.get("map3")).isNull();

        assertThat(functionInt).isEqualTo(4);
        assertThat(functionDouble).isCloseTo(4.4, within(new Double(0)));
        assertThat(functionFloat).isCloseTo((float) 5.55, within(new Float(0)));
        assertThat(functionBoolean).isTrue();
    }

}

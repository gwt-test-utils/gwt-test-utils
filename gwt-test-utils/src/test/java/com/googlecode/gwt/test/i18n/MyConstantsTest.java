package com.googlecode.gwt.test.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;

public class MyConstantsTest extends GwtTestTest {

    private MyConstants constants;

    @Before
    public void beforeMyConstantsTest() {
        constants = GWT.create(MyConstants.class);
    }

    @Test
    public void changeLocale() {
        // Arrange
        setLocale(Locale.ENGLISH);

        // Act 1
        SafeHtml hello = constants.hello();
        String goodbye = constants.goodbye();
        String[] stringArray = constants.stringArray();
        Map<String, String> map = constants.map();
        String valueWithoutLocale = constants.valueWithoutLocale();
        String valueWithoutLocaleToBeOverride = constants.valueWithoutLocaleToBeOverride();
        String messageWithKey = constants.messageWithKey();

        // Assert 1
        assertEquals("Hello english !", hello.asString());
        assertEquals("Goodbye english !", goodbye);

        assertEquals(3, stringArray.length);
        assertEquals("one", stringArray[0]);
        assertEquals("two", stringArray[1]);
        assertEquals("three", stringArray[2]);

        assertEquals(5, map.size());
        assertEquals("Hello english !", map.get("hello"));
        assertEquals("Goodbye english !", map.get("goodbye"));
        assertEquals("glad to work with gwt-test-utils", map.get("map1"));
        assertEquals("hehe, it roxs !", map.get("map2"));
        assertNull(map.get("map3"));

        assertEquals("Value from a default .properties file, without locale", valueWithoutLocale);
        assertEquals("Value from parent default .properties", valueWithoutLocaleToBeOverride);

        // @Key
        assertEquals("Message with key english", messageWithKey);

        // Act 2
        setLocale(Locale.US);
        hello = constants.hello();
        goodbye = constants.goodbye();
        stringArray = constants.stringArray();
        map = constants.map();
        messageWithKey = constants.messageWithKey();

        // Assert 2
        assertEquals("Hello US !", hello.asString());
        assertEquals("Goodbye US !", goodbye);

        assertEquals(4, stringArray.length);
        assertEquals("one", stringArray[0]);
        assertEquals("two", stringArray[1]);
        assertEquals("three", stringArray[2]);
        assertEquals("four", stringArray[3]);

        // "map" is not present in MyConstants_en_US.properties : loaded from
        // @DefaultStringMapMapValue
        assertEquals(3, map.size());
        assertEquals("default map1 value", map.get("map1"));
        assertEquals("default map2 value", map.get("map2"));
        assertEquals("default map3 value", map.get("map3"));

        // @Key
        assertEquals("Message with key US", messageWithKey);
    }

    @Test
    public void defaultValues() {
        // Arrange
        String expectedErrorMessage = "No matching property \"goodbye\" for Constants class [com.googlecode.gwt.test.i18n.MyConstants]. Please check the corresponding properties files or use @DefaultStringValue";

        // Act 1
        SafeHtml hello = constants.hello();
        String[] stringArray = constants.stringArray();
        Map<String, String> map = constants.map();
        int functionInt = constants.functionInt();
        double functionDouble = constants.functionDouble();
        float functionFloat = constants.functionFloat();
        boolean functionBoolean = constants.functionBoolean();

        // Assert
        assertEquals("hello from @DefaultStringValue", hello.asString());
        assertEquals(2, stringArray.length);
        assertEquals("default0", stringArray[0]);
        assertEquals("default1", stringArray[1]);
        assertEquals(3, map.size());
        assertEquals("default map1 value", map.get("map1"));
        assertEquals("default map2 value", map.get("map2"));
        assertEquals("default map3 value", map.get("map3"));

        assertEquals(6, functionInt);
        assertEquals(6.6, functionDouble, 0);
        assertEquals((float) 6.66, functionFloat, 0);
        assertTrue(functionBoolean);

        // Act 2 : no @DefaultStringValue
        try {
            constants.goodbye();
            fail("i18n patching mechanism should throw an exception if no locale, no default .properties file and no @DefaultStringValue is set");
        } catch (Exception e) {
            // Assert 2
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }

    @Test
    public void specialChars() {
        setLocale(Locale.FRENCH);

        // Act
        SafeHtml hello = constants.hello();
        String goodbye = constants.goodbye();
        String[] stringArray = constants.stringArray();
        Map<String, String> map = constants.map();
        int functionInt = constants.functionInt();
        double functionDouble = constants.functionDouble();
        float functionFloat = constants.functionFloat();
        boolean functionBoolean = constants.functionBoolean();

        // Assert
        assertEquals("Bonjour", hello.asString());
        assertEquals("Au revoir et un caractère qui pue", goodbye);

        assertEquals(3, stringArray.length);
        assertEquals("un", stringArray[0]);
        assertEquals("deux", stringArray[1]);
        assertEquals("trois", stringArray[2]);

        assertEquals(5, map.size());
        assertEquals("Bonjour", map.get("hello"));
        assertEquals("Au revoir et un caractère qui pue", map.get("goodbye"));
        assertEquals("je suis content", map.get("map1"));
        assertEquals("tout pareil !", map.get("map2"));
        assertNull(map.get("map3"));

        assertEquals(4, functionInt);
        assertEquals(4.4, functionDouble, 0);
        assertEquals((float) 5.55, functionFloat, 0);
        assertTrue(functionBoolean);
    }

}

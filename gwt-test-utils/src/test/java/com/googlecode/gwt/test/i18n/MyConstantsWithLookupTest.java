package com.googlecode.gwt.test.i18n;

import com.google.gwt.core.client.GWT;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;

public class MyConstantsWithLookupTest extends GwtTestTest {

    private MyConstantsWithLookup constants;

    @Before
    public void beforeMyConstantsWithLookupTest() {
        constants = GWT.create(MyConstantsWithLookup.class);
    }

    @Test
    public void defaultValues() {
        // Arrange
        String expectedErrorMessage = "No matching property \"goodbye\" for Constants class [com.googlecode.gwt.test.i18n.MyConstantsWithLookup]. Please check the corresponding properties files or use @DefaultStringValue";

        // Act 1
        String hello = constants.hello();
        String[] stringArray = constants.stringArray();
        Map<String, String> map = constants.map();
        int functionInt = constants.functionInt();
        double functionDouble = constants.functionDouble();
        float functionFloat = constants.functionFloat();
        boolean functionBoolean = constants.functionBoolean();

        // MyConstantsWithLookup specific methods
        String getString = constants.getString("hello");
        String[] getStringArray = constants.getStringArray("stringArray");
        Map<String, String> getMap = constants.getMap("map");
        int getInt = constants.getInt("functionInt");
        double getDouble = constants.getDouble("functionDouble");
        float getFloat = constants.getFloat("functionFloat");
        boolean getBoolean = constants.getBoolean("functionBoolean");

        // Assert
        assertEquals("hello from @DefaultStringValue", hello);
        assertEquals(2, stringArray.length);
        assertEquals("default0", stringArray[0]);
        assertEquals("default1", stringArray[1]);
        assertEquals("defaultMap1", map.get("map1"));
        assertEquals("defaultMap2", map.get("map2"));

        assertEquals(6, functionInt);
        assertEquals(6.6, functionDouble, 0);
        assertEquals((float) 6.66, functionFloat, 0);
        assertTrue(functionBoolean);

        // MyConstantsWithLookup specific methods assertions
        assertEquals(hello, getString);
        assertEquals(stringArray.length, getStringArray.length);
        assertEquals(stringArray[0], getStringArray[0]);
        assertEquals(stringArray[1], getStringArray[1]);

        assertEquals(map.size(), getMap.size());
        assertEquals(map.get("hello"), getMap.get("hello"));
        assertEquals(map.get("goodbye"), getMap.get("goodbye"));
        assertEquals(map.get("noCorrespondance"), getMap.get("noCorrespondance"));

        assertEquals(functionInt, getInt);
        assertEquals(functionDouble, getDouble, 0);
        assertEquals(functionFloat, getFloat, 0);
        assertEquals(functionBoolean, getBoolean);

        // Act 2 : no @DefaultStringValue
        try {
            constants.goodbye();
            fail("i18n patching mechanism should throw an exception if no locale and no @DefaultStringValue is set");
        } catch (Exception e) {
            // Assert 2
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }

    @Test
    public void specialChars() {
        setLocale(Locale.FRENCH);

        // Act
        String hello = constants.hello();
        String goodbye = constants.goodbye();
        String[] stringArray = constants.stringArray();
        Map<String, String> map = constants.map();
        int functionInt = constants.functionInt();
        double functionDouble = constants.functionDouble();
        float functionFloat = constants.functionFloat();
        boolean functionBoolean = constants.functionBoolean();

        // MyConstantsWithLookup specific methods
        String getString = constants.getString("hello");
        String[] getStringArray = constants.getStringArray("stringArray");
        Map<String, String> getMap = constants.getMap("map");
        int getInt = constants.getInt("functionInt");
        double getDouble = constants.getDouble("functionDouble");
        float getFloat = constants.getFloat("functionFloat");
        boolean getBoolean = constants.getBoolean("functionBoolean");

        // Assert
        assertEquals("Bonjour", hello);
        assertEquals("Au revoir et un caractère qui pue", goodbye);

        assertEquals(3, stringArray.length);
        assertEquals("un", stringArray[0]);
        assertEquals("deux", stringArray[1]);
        assertEquals("trois", stringArray[2]);

        assertEquals(4, map.size());
        assertEquals("Bonjour", map.get("hello"));
        assertEquals("Au revoir et un caractère qui pue", map.get("goodbye"));
        assertEquals("premiere valeur de la map", map.get("map1"));
        assertEquals("seconde valeur de la map", map.get("map2"));
        assertNull(map.get("map3"));

        assertEquals(4, functionInt);
        assertEquals(4.4, functionDouble, 0);
        assertEquals((float) 5.55, functionFloat, 0);
        assertTrue(functionBoolean);

        // MyConstantsWithLookup specific methods assertions
        assertEquals(hello, getString);
        assertEquals(stringArray.length, getStringArray.length);
        assertEquals(stringArray[0], getStringArray[0]);
        assertEquals(stringArray[1], getStringArray[1]);
        assertEquals(stringArray[2], getStringArray[2]);

        assertEquals(map.size(), getMap.size());
        assertEquals(map.get("hello"), getMap.get("hello"));
        assertEquals(map.get("goodbye"), getMap.get("goodbye"));
        assertEquals(map.get("noCorrespondance"), getMap.get("noCorrespondance"));

        assertEquals(functionInt, getInt);
        assertEquals(functionDouble, getDouble, 0);
        assertEquals(functionFloat, getFloat, 0);
        assertEquals(functionBoolean, getBoolean);
    }

}

package com.googlecode.gwt.test.i18n;

import com.google.gwt.core.client.GWT;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;


public class MyConstantsWithLookupTest extends GwtTestTest {

    private MyConstantsWithLookup constants;

    @Before
    public void beforeMyConstantsWithLookupTest() {
        constants = GWT.create(MyConstantsWithLookup.class);
    }

    @Test
    public void defaultValues() {
        // Given
        String expectedErrorMessage = "No matching property \"goodbye\" for Constants class [com.googlecode.gwt.test.i18n.MyConstantsWithLookup]. Please check the corresponding properties files or use @DefaultStringValue";

        // When 1
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

        // Then
        assertThat(hello).isEqualTo("hello from @DefaultStringValue");
        assertThat(stringArray.length).isEqualTo(2);
        assertThat(stringArray[0]).isEqualTo("default0");
        assertThat(stringArray[1]).isEqualTo("default1");
        assertThat(map.get("map1")).isEqualTo("defaultMap1");
        assertThat(map.get("map2")).isEqualTo("defaultMap2");

        assertThat(functionInt).isEqualTo(6);
        assertThat(functionDouble).isCloseTo(6.6, within(0.0));
        assertThat(functionFloat).isCloseTo((float) 6.66, within(new Float(0)));
        assertThat(functionBoolean).isTrue();

        // MyConstantsWithLookup specific methods assertions
        assertThat(getString).isEqualTo(hello);
        assertThat(getStringArray.length).isEqualTo(stringArray.length);
        assertThat(getStringArray[0]).isEqualTo(stringArray[0]);
        assertThat(getStringArray[1]).isEqualTo(stringArray[1]);

        assertThat(getMap.size()).isEqualTo(map.size());
        assertThat(getMap.get("hello")).isEqualTo(map.get("hello"));
        assertThat(getMap.get("goodbye")).isEqualTo(map.get("goodbye"));
        assertThat(getMap.get("noCorrespondance")).isEqualTo(map.get("noCorrespondance"));

        assertThat(getInt).isEqualTo(functionInt);
        assertThat(getDouble).isCloseTo(functionDouble, within(new Double(0)));
        assertThat(getFloat).isCloseTo(functionFloat, within(new Float(0)));
        assertThat(getBoolean).isEqualTo(functionBoolean);

        // When 2 : no @DefaultStringValue
        try {
            constants.goodbye();
            fail("i18n patching mechanism should throw an exception if no locale and no @DefaultStringValue is set");
        } catch (Exception e) {
            // Then 2
            assertThat(e.getMessage()).isEqualTo(expectedErrorMessage);
        }
    }

    @Test
    public void specialChars() {
        setLocale(Locale.FRENCH);

        // When
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

        // Then
        assertThat(hello).isEqualTo("Bonjour");
        assertThat(goodbye).isEqualTo("Au revoir et un caractère qui pue");

        assertThat(stringArray.length).isEqualTo(3);
        assertThat(stringArray[0]).isEqualTo("un");
        assertThat(stringArray[1]).isEqualTo("deux");
        assertThat(stringArray[2]).isEqualTo("trois");

        assertThat(map).hasSize(4);
        assertThat(map.get("hello")).isEqualTo("Bonjour");
        assertThat(map.get("goodbye")).isEqualTo("Au revoir et un caractère qui pue");
        assertThat(map.get("map1")).isEqualTo("premiere valeur de la map");
        assertThat(map.get("map2")).isEqualTo("seconde valeur de la map");
        assertThat(map.get("map3")).isNull();

        assertThat(functionInt).isEqualTo(4);
        assertThat(functionDouble).isCloseTo(4.4, within(new Double(0)));
        assertThat(functionFloat).isCloseTo((float) 5.55, within(new Float(0)));
        assertThat(functionBoolean).isTrue();

        // MyConstantsWithLookup specific methods assertions
        assertThat(getString).isEqualTo(hello);
        assertThat(getStringArray.length).isEqualTo(stringArray.length);
        assertThat(getStringArray[0]).isEqualTo(stringArray[0]);
        assertThat(getStringArray[1]).isEqualTo(stringArray[1]);
        assertThat(getStringArray[2]).isEqualTo(stringArray[2]);

        assertThat(getMap.size()).isEqualTo(map.size());
        assertThat(getMap.get("hello")).isEqualTo(map.get("hello"));
        assertThat(getMap.get("goodbye")).isEqualTo(map.get("goodbye"));
        assertThat(getMap.get("noCorrespondance")).isEqualTo(map.get("noCorrespondance"));

        assertThat(getInt).isEqualTo(functionInt);
        assertThat(getDouble).isCloseTo(functionDouble, within(new Double(0)));
        assertThat(getFloat).isCloseTo(functionFloat, within(new Float(0)));
        assertThat(getBoolean).isEqualTo(functionBoolean);
    }

}

package com.googlecode.gwt.test.i18n;

import com.google.gwt.i18n.client.ConstantsWithLookup;

import java.util.Map;

public interface MyConstantsWithLookup extends ConstantsWithLookup {

    @DefaultBooleanValue(true)
    public boolean functionBoolean();

    @DefaultDoubleValue(6.6)
    public double functionDouble();

    @DefaultFloatValue((float) 6.66)
    public float functionFloat();

    @DefaultIntValue(6)
    public int functionInt();

    public String goodbye();

    @DefaultStringValue("hello from @DefaultStringValue")
    public String hello();

    @DefaultStringMapValue({"map1", "defaultMap1", "map2", "defaultMap2"})
    public Map<String, String> map();

    @DefaultStringValue("no corresponding property in any file, value from @DefaultStringValue")
    public String noCorrespondance();

    @DefaultStringArrayValue({"default0", "default1"})
    public String[] stringArray();

}

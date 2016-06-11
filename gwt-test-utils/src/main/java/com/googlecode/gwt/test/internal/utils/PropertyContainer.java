package com.googlecode.gwt.test.internal.utils;

import com.google.gwt.core.client.JavaScriptObject;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * An object which is added to all {@link JavaScriptObject} instance to store its native properties.
 * <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 * @author Bertrand Paquet
 */
public class PropertyContainer implements Serializable {

    private static final long serialVersionUID = -5044826131951960161L;

    public static PropertyContainer newInstance(Map<String, Object> map) {
        return new PropertyContainer(map);
    }

    private final Map<String, Object> map;

    private PropertyContainer(Map<String, Object> map) {
        this.map = map;
    }

    public void clear() {
        map.clear();
    }

    public boolean contains(String key) {
        return map.containsKey(key);
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    public boolean getBoolean(String key) {
        Object o = map.get(key);
        return o == null ? false : Boolean.valueOf(o.toString());
    }

    public byte getByte(String key) {
        Byte b = (Byte) map.get(key);
        return b == null ? 0 : b;
    }

    public char getChar(String key) {
        Character c = (Character) map.get(key);
        return c == null ? 0 : c;
    }

    public double getDouble(String key) {
        Object o = map.get(key);
        return o == null ? 0 : Double.valueOf(o.toString());
    }

    public float getFloat(String key) {
        Object o = map.get(key);
        return o == null ? 0 : Float.valueOf(o.toString());
    }

    public int getInteger(String key) {
        Object o = map.get(key);
        return o == null ? 0 : Integer.valueOf(o.toString());
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(String key) {
        return (T) map.get(key);
    }

    public short getShort(String key) {
        Object o = map.get(key);
        return o == null ? 0 : Short.valueOf(o.toString());
    }

    public String getString(String key) {
        Object o = map.get(key);
        return o == null ? "" : o.toString();
    }

    public Object put(String key, boolean value) {
        return map.put(key, Boolean.valueOf(value));
    }

    public Object put(String key, double value) {
        return map.put(key, Double.valueOf(value));
    }

    public Object put(String key, int value) {
        return map.put(key, Integer.valueOf(value));
    }

    public Object put(String key, long value) {
        return map.put(key, Long.valueOf(value));
    }

    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    public Object put(String key, short value) {
        return map.put(key, Short.valueOf(value));
    }

    public Object remove(String key) {
        return map.remove(key);
    }

    public int size() {
        return map.size();
    }

    @Override
    public String toString() {
        return map.toString();
    }

}

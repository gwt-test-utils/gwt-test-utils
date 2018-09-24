package com.googlecode.gwt.test.internal.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Some array utility methods. <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
public class ArrayUtils {

    public static boolean contains(Object[] array, Object valueToFind) {
        for (int i = 0; i < array.length; i++) {
            if (valueToFind.equals(array[i])) {
                return true;
            }
        }
        return false;
    }

    public static <K, V> Map<K, V> copyMap(Map<K, V> map) {
        Map<K, V> result = new HashMap<>();

        for (Map.Entry<K, V> entry : map.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    private ArrayUtils() {

    }

}

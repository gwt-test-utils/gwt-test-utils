package com.googlecode.gwt.test.internal.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Double-Map implementation. <strong>For internal use only.</strong>
 *
 * @param <A>
 * @param <B>
 * @param <C>
 * @author Bertrand Paquet
 */
public class DoubleMap<A, B, C> {

    private final Map<A, Map<B, C>> map;

    public DoubleMap() {
        map = new HashMap<>();
    }

    public C get(A a, B b) {
        Map<B, C> m = map.get(a);
        return m == null ? null : m.get(b);
    }

    public void put(A a, B b, C c) {
        if (map.get(a) == null) {
            map.put(a, new HashMap<>());
        }
        map.get(a).put(b, c);
    }
}

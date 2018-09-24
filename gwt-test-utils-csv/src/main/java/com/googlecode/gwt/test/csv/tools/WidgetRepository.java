package com.googlecode.gwt.test.csv.tools;

import java.util.HashMap;
import java.util.Map;

public class WidgetRepository {

    private final Map<String, Object> map = new HashMap<>();

    public Object addAlias(String alias, Object widget) {
        return map.put(alias, widget);
    }

    public void clear() {
        map.clear();
    }

    public Object getAlias(String alias) {
        return map.get(alias);
    }

    public Object removeAlias(String alias) {
        return map.remove(alias);
    }
}

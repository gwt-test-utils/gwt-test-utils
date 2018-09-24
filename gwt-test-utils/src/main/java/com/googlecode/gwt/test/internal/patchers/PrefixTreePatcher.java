package com.googlecode.gwt.test.internal.patchers;

import com.googlecode.gwt.test.internal.GwtClassPool;
import com.googlecode.gwt.test.patchers.InitMethod;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

@PatchClass(target = "com.google.gwt.user.client.ui.PrefixTree")
class PrefixTreePatcher {

    private static final String PREFIXES_SET_PROPERTY = "PREFIXES_SET";

    @PatchMethod
    static boolean add(Object prefixTree, String s) {
        return getPrefixSet(prefixTree).add(s);
    }

    @PatchMethod
    static void clear(Object prefixTree) {
        GwtReflectionUtils.setPrivateFieldValue(prefixTree, "size", 0);
    }

    @InitMethod
    static void initClass(CtClass c) throws CannotCompileException {
        // add field "private Set<?> PREFIXES_SET;"
        CtClass pcType = GwtClassPool.getCtClass(Set.class);
        CtField field = new CtField(pcType, PREFIXES_SET_PROPERTY, c);
        field.setModifiers(Modifier.PRIVATE);
        c.addField(field);
    }

    @PatchMethod
    static void suggestImpl(Object prefixTree, String search, String prefix,
                            Collection<String> output, int limit) {

        for (String record : getPrefixSet(prefixTree)) {
            if (search == null || record.contains(search.trim().toLowerCase())) {
                output.add(record);
            }
        }
    }

    private static Set<String> getPrefixSet(Object prefixTree) {
        Set<String> set = GwtReflectionUtils.getPrivateFieldValue(prefixTree, PREFIXES_SET_PROPERTY);
        if (set == null) {
            set = new TreeSet<>();
            GwtReflectionUtils.setPrivateFieldValue(prefixTree, PREFIXES_SET_PROPERTY, set);
        }
        return set;
    }

}

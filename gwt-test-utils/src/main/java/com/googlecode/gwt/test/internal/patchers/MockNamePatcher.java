package com.googlecode.gwt.test.internal.patchers;

import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import org.mockito.internal.util.MockNameImpl;

@PatchClass(MockNameImpl.class)
class MockNamePatcher {

    @PatchMethod
    static String toInstanceName(Class<?> clazz) {

        // This test is made to avoid the use of clazz.getSimpleName() which would break for inner
        // classes declared in some OverlayType
        // example : class com.google.gwt.user.client.Event$NativePreviewEvent
        if (clazz.getName().matches("^.+\\$.+$")) {
            return clazz.getName().substring(clazz.getName().lastIndexOf('$') + 1);
        }

        String className = clazz.getSimpleName();
        if (className.length() == 0) {
            // it's an anonymous class, let's get name from the parent
            className = clazz.getSuperclass().getSimpleName();
        }
        // lower case first letter
        return className.substring(0, 1).toLowerCase() + className.substring(1);

    }

}

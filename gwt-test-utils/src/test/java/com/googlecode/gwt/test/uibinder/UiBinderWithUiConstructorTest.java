package com.googlecode.gwt.test.uibinder;

import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.uibinder.UiConstructorWidget.AnotherType;
import com.googlecode.gwt.test.uibinder.UiConstructorWidget.Type;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UiBinderWithUiConstructorTest extends GwtTestTest {

    @Test
    public void instanciation() throws Exception {
        // Act
        UiBinderWithUiConstructor w = new UiBinderWithUiConstructor();

        // Assert
        assertEquals(5, w.myWidget.size);
        assertEquals(Type.T1, w.myWidget.type);

        assertEquals(AnotherType.T2, w.myWidget.anotherType);
    }

}

package com.googlecode.gwt.test.uibinder;

import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.uibinder.UiConstructorWidget.AnotherType;
import com.googlecode.gwt.test.uibinder.UiConstructorWidget.Type;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UiBinderWithUiConstructorTest extends GwtTestTest {

    @Test
    public void instanciation() {
        // When
        UiBinderWithUiConstructor w = new UiBinderWithUiConstructor();

        // Then
        assertThat(w.myWidget.size).isEqualTo(5);
        assertThat(w.myWidget.type).isEqualTo(Type.T1);

        assertThat(w.myWidget.anotherType).isEqualTo(AnotherType.T2);
    }

}

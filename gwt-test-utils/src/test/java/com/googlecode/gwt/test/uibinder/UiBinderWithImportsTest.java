package com.googlecode.gwt.test.uibinder;

import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.assertj.core.api.Assertions.assertThat;

public class UiBinderWithImportsTest extends GwtTestTest {

    @Test
    public void dateLabel_customFormat() {
        // Given
        UiBinderWithImports view = new UiBinderWithImports();
        Calendar cal = new GregorianCalendar();
        cal.set(2011, 10, 18);
        Date date = cal.getTime();

        // When
        view.myDateLabel3.setValue(date);

        // Then
        assertThat(view.myDateLabel3.getElement().getInnerHTML()).isEqualTo("Fri 18 Nov");
    }

    @Test
    public void dateLabel_importedFormat() {
        // Given
        UiBinderWithImports view = new UiBinderWithImports();
        Calendar cal = new GregorianCalendar();
        cal.set(2011, 10, 18);
        Date date = cal.getTime();

        // When
        view.myDateLabel2.setValue(date);

        // Then
        assertThat(view.myDateLabel2.getElement().getInnerHTML()).isEqualTo("2011 Nov 18");
    }

    @Test
    public void dateLabel_predefinedFormat() {
        // Given

        UiBinderWithImports view = new UiBinderWithImports();
        Calendar cal = new GregorianCalendar();
        cal.set(2011, 10, 18);
        Date date = cal.getTime();

        // When
        view.myDateLabel.setValue(date);

        // Then
//      assertThat(view.myDateLabel.getElement().getInnerHTML()).isEqualTo("Friday, 2011 November 18");
        // TODO(gael) fix this properly!
        assertThat(view.myDateLabel.getElement().getInnerHTML()).isEqualTo("2011 November 18, Friday");
    }

    @Test
    public void testImports() {
        // When
        UiBinderWithImports view = new UiBinderWithImports();

        // Then
        assertThat(view.singleConstantImport.getText()).isEqualTo("single import value : Foo");
        assertThat(view.multipleConstantsImport.getText()).isEqualTo("first contant : Bar, second constant : Baz");
        assertThat(view.enumImport.getText()).isEqualTo("first enum value : ENUM_1, second enum value : ENUM_2");
    }

}

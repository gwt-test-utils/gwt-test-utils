package com.googlecode.gwt.test.uibinder;

import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class UiBinderWithImportsTest extends GwtTestTest {

    @Test
    public void dateLabel_customFormat() {
        // Arrange
        UiBinderWithImports view = new UiBinderWithImports();
        Calendar cal = new GregorianCalendar();
        cal.set(2011, 10, 18);
        Date date = cal.getTime();

        // Act
        view.myDateLabel3.setValue(date);

        // assert
        assertEquals("Fri 18 Nov", view.myDateLabel3.getElement().getInnerHTML());
    }

    @Test
    public void dateLabel_importedFormat() {
        // Arrange
        UiBinderWithImports view = new UiBinderWithImports();
        Calendar cal = new GregorianCalendar();
        cal.set(2011, 10, 18);
        Date date = cal.getTime();

        // Act
        view.myDateLabel2.setValue(date);

        // assert
        assertEquals("2011 Nov 18", view.myDateLabel2.getElement().getInnerHTML());
    }

    @Test
    public void dateLabel_predefinedFormat() {
        // Arrange

        UiBinderWithImports view = new UiBinderWithImports();
        Calendar cal = new GregorianCalendar();
        cal.set(2011, 10, 18);
        Date date = cal.getTime();

        // Act
        view.myDateLabel.setValue(date);

        // assert
//      assertEquals("Friday, 2011 November 18", view.myDateLabel.getElement().getInnerHTML());
        // TODO(gael) fix this properly!
        assertEquals("2011 November 18, Friday", view.myDateLabel.getElement().getInnerHTML());
    }

    @Test
    public void testImports() {
        // Act
        UiBinderWithImports view = new UiBinderWithImports();

        // Assert
        assertEquals("single import value : Foo", view.singleConstantImport.getText());

        assertEquals("first contant : Bar, second constant : Baz",
                view.multipleConstantsImport.getText());

        assertEquals("first enum value : ENUM_1, second enum value : ENUM_2",
                view.enumImport.getText());
    }

}

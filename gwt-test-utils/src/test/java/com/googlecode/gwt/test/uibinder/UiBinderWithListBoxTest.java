package com.googlecode.gwt.test.uibinder;

import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UiBinderWithListBoxTest extends GwtTestTest {

    @Test
    public void content() {
        // Act
        UiBinderWithListBox uibinder = new UiBinderWithListBox();

        // Assert
        assertEquals(2, uibinder.listBox.getItemCount());
        assertEquals("first", uibinder.listBox.getItemText(0));
        assertEquals("", uibinder.listBox.getValue(0));
        assertEquals("second", uibinder.listBox.getItemText(1));
        assertEquals("2", uibinder.listBox.getValue(1));
    }

}

package com.googlecode.gwt.test.uibinder.specialization;

import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UiBinderWithUiBinderSpecializationsTest extends GwtTestTest {

    @Test
    public void uiBinderWithDockLayoutPanel() {
        // Arrange
        UiBinderWithMoreThanOneUiBinderFactoriesForSameType w = new UiBinderWithMoreThanOneUiBinderFactoriesForSameType();

        // Act
        RootPanel.get().add(w);

        // Assert
        assertEquals("item created by @UiFactory", w.itemWidget.genericLabel.getText());
        assertEquals("person created by @UiFactory", w.personWidget.genericLabel.getText());
    }
}

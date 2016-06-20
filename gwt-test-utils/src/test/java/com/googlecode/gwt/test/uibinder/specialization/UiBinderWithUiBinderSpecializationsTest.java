package com.googlecode.gwt.test.uibinder.specialization;

import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UiBinderWithUiBinderSpecializationsTest extends GwtTestTest {

    @Test
    public void uiBinderWithDockLayoutPanel() {
        // Given
        UiBinderWithMoreThanOneUiBinderFactoriesForSameType w = new UiBinderWithMoreThanOneUiBinderFactoriesForSameType();

        // When
        RootPanel.get().add(w);

        // Then
        assertThat(w.itemWidget.genericLabel.getText()).isEqualTo("item created by @UiFactory");
        assertThat(w.personWidget.genericLabel.getText()).isEqualTo("person created by @UiFactory");
    }
}

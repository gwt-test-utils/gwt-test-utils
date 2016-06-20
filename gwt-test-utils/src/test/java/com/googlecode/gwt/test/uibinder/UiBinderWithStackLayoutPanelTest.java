package com.googlecode.gwt.test.uibinder;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UiBinderWithStackLayoutPanelTest extends GwtTestTest {

    @Test
    public void uiBinderWithStackLayoutPanel() {
        // Given
        UiBinderWithStackLayoutPanel panel = new UiBinderWithStackLayoutPanel();

        // When
        RootLayoutPanel.get().add(panel);

        // Then
        assertThat(panel.first.getText()).isEqualTo("able");
        assertThat(panel.customHeader.getText()).isEqualTo("Custom header");
        assertThat(panel.second.getText()).isEqualTo("baker");
        assertThat(panel.getPanel().getVisibleWidget()).isEqualTo(panel.first);

        assertThat(panel.getPanel().getHeaderWidget(0).getElement().getInnerHTML()).isEqualTo("<b>HTML</b>");

        assertThat(panel.getPanel().getHeaderWidget(panel.second)).isEqualTo(panel.customHeader);
    }

}

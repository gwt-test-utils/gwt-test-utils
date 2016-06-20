package com.googlecode.gwt.test.uibinder;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UiBinderWithTabLayoutPanelTest extends GwtTestTest {

    @Test
    public void uiBinderWithTabLayoutPanel() {
        // Given
        UiBinderWithTabLayoutPanel panel = new UiBinderWithTabLayoutPanel();

        // When
        RootLayoutPanel.get().add(panel);

        // Then
        assertThat(panel.first.getText()).isEqualTo("able");
        assertThat(panel.customHeader.getText()).isEqualTo("Custom header");
        assertThat(panel.second.getText()).isEqualTo("baker");
        assertThat(panel.getPanel().getTabWidget(0).getElement().getInnerHTML()).isEqualTo("<b>HTML</b>");
        assertThat(panel.getPanel().getTabWidget(panel.second)).isEqualTo(panel.customHeader);

    }

}

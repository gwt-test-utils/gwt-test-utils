package com.googlecode.gwt.test.uibinder;

import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UiBinderWithLayoutPanelTest extends GwtTestTest {

    @Test
    public void content() {
        // When
        UiBinderWithLayoutPanel panel = new UiBinderWithLayoutPanel();

        // Then
        assertThat(panel.getPanel().getWidgetCount()).isEqualTo(2);
        assertThat(panel.getPanel().getWidget(0)).isEqualTo(panel.defaultLabel);
        assertThat(panel.getPanel().getWidget(1)).isEqualTo(panel.headerLabel);
    }

}

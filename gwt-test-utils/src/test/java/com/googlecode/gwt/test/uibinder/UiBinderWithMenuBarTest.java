package com.googlecode.gwt.test.uibinder;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UiBinderWithMenuBarTest extends GwtTestTest {

    @Test
    public void uiBinderWithMenuBar() {
        // Given
        UiBinderWithMenuBar panel = new UiBinderWithMenuBar();

        // When
        RootLayoutPanel.get().add(panel);

        // Then
        assertThat(panel.getMenu().getItemIndex(panel.menuItem1)).isEqualTo(0);
        assertThat(panel.menuItem1.getParentMenu()).isEqualTo(panel.getMenu());
        assertThat(panel.getMenu().getItemIndex(panel.menuItem2)).isEqualTo(1);
        assertThat(panel.menuItem2.getParentMenu()).isEqualTo(panel.getMenu());
        assertThat(panel.getMenu().getItemIndex(panel.menuItem3)).isEqualTo(2);
        assertThat(panel.menuItem3.getParentMenu()).isEqualTo(panel.getMenu());

        assertThat(panel.menuItem1.getSubMenu()).isEqualTo(panel.menu1);
        assertThat(panel.menuItem2.getSubMenu()).isEqualTo(panel.menu2);
        assertThat(panel.menuItem3.getSubMenu()).isEqualTo(panel.menu3);

        assertThat(panel.menu1.getItemIndex(panel.subMenuItem1)).isEqualTo(0);
        assertThat(panel.subMenuItem1.getParentMenu()).isEqualTo(panel.menu1);
        assertThat(panel.menu2.getItemIndex(panel.subMenuItem2)).isEqualTo(1);
        assertThat(panel.subMenuItem2.getParentMenu()).isEqualTo(panel.menu2);
        assertThat(panel.menu3.getItemIndex(panel.subMenuItem3)).isEqualTo(2);
        assertThat(panel.subMenuItem3.getParentMenu()).isEqualTo(panel.menu3);
    }

}

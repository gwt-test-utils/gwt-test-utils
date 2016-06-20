package com.googlecode.gwt.test.uibinder;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.user.client.ui.DockLayoutPanel.Direction;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UiBinderWithDockLayoutPanelTest extends GwtTestTest {

    @Test
    public void uiBinderWithDockLayoutPanel() {
        // Given
        UiBinderWithDockLayoutPanel panel = new UiBinderWithDockLayoutPanel();

        // When
        RootLayoutPanel.get().add(panel);

        // Then
        assertThat(panel.northLabel.getText()).isEqualTo("North");
        assertThat(panel.centerLabel.getText()).isEqualTo("Center");
        assertThat(panel.eastLabel.getText()).isEqualTo("East");
        assertThat(panel.southLabel.getText()).isEqualTo("South");
        assertThat(panel.centerLabel.getText()).isEqualTo("Center");
        assertThat(panel.getLayout().getWidgetDirection(panel.northLabel)).isEqualTo(Direction.NORTH);
        assertThat(panel.westHTML.getHTML()).isEqualTo("<ul><li id=\"li-west0\">west0</li><li id=\"li-west1\">west1</li></ul>");

        LIElement li0 = panel.westHTML.getElement().getFirstChildElement().getChild(0).cast();
        LIElement li1 = panel.westHTML.getElement().getFirstChildElement().getChild(1).cast();
        assertThat(li0.getInnerText()).isEqualTo("west0");
        assertThat(li1.getInnerText()).isEqualTo("west1");

        assertThat(Document.get().getElementById("li-west0")).isEqualTo(li0);
        assertThat(Document.get().getElementById("li-west1")).isEqualTo(li1);
    }

}

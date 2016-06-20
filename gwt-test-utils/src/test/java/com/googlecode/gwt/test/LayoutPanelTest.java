package com.googlecode.gwt.test;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.layout.client.Layout.AnimationCallback;
import com.google.gwt.layout.client.Layout.Layer;
import com.google.gwt.user.client.ui.*;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LayoutPanelTest extends GwtTestTest {

    private boolean onAnimationComplete;

    private LayoutPanel panel;

    @Test
    public void add() {
        // Given
        Button b = new Button();
        assertThat(b.isAttached()).isFalse();

        // When
        panel.add(b);

        // Then
        assertThat(panel.getWidgetCount()).isEqualTo(3);
        assertThat(panel.getWidget(2)).isEqualTo(b);
        assertThat(b.isAttached()).isTrue();
    }

    @Test
    public void animate() {
        // Given
        AnimationCallback callback = new AnimationCallback() {

            public void onAnimationComplete() {
                onAnimationComplete = true;
            }

            public void onLayout(Layer layer, double progress) {
                // never called in gwt-test-utils
            }
        };

        // When
        panel.animate(4, callback);

        // Then
        assertThat(onAnimationComplete).isTrue();
    }

    @Before
    public void beforeLayoutPanel() {
        onAnimationComplete = false;

        panel = new LayoutPanel();
        assertThat(panel.isAttached()).isFalse();

        // Attach the LayoutPanel to the RootLayoutPanel. The latter will listen
        // for
        // resize events on the window to ensure that its children are informed of
        // possible size changes.
        RootLayoutPanel.get().add(panel);
        assertThat(panel.isAttached()).isTrue();
        assertThat(panel.getWidgetCount()).isEqualTo(0);

        // Attach two child widgets to a LayoutPanel, laying them out
        // horizontally,
        // splitting at 50%.
        Widget childOne = new HTML("left"), childTwo = new HTML("right");
        panel.add(childOne);
        panel.add(childTwo);

        panel.setWidgetLeftWidth(childOne, 0, Unit.PCT, 50, Unit.PCT);
        panel.setWidgetRightWidth(childTwo, 0, Unit.PCT, 50, Unit.PCT);

        assertThat(panel.getWidgetCount()).isEqualTo(2);
    }

    @Test
    public void getWidgetContainerElement() {
        // Given
        FlowPanel fp1 = new FlowPanel();
        panel.add(fp1);
        Element fp1Element = fp1.getElement();

        // When
        Element fp1Container = panel.getWidgetContainerElement(fp1);

        // Then
        assertThat(fp1Container.getFirstChildElement()).isEqualTo(fp1Element);
    }

}

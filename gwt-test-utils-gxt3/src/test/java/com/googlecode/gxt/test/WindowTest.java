package com.googlecode.gxt.test;

import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.event.*;
import com.sencha.gxt.widget.core.client.event.ActivateEvent.ActivateHandler;
import com.sencha.gxt.widget.core.client.event.DeactivateEvent.DeactivateHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.MaximizeEvent.MaximizeHandler;
import com.sencha.gxt.widget.core.client.event.MinimizeEvent.MinimizeHandler;
import com.sencha.gxt.widget.core.client.event.RestoreEvent.RestoreHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WindowTest extends GwtGxtTest {

    private boolean activate;
    private boolean deactivate;
    private boolean hide;
    private boolean maximize;
    private boolean minimize;
    private boolean restore;
    private boolean show;
    private Window window;

    @Test
    public void activate() {
        // Given
        window.addActivateHandler(new ActivateHandler<Window>() {

            public void onActivate(ActivateEvent<Window> event) {
                assertThat(event.getItem()).isEqualTo(window);
                activate = true;
            }

        });

        window.show();

        // When
        window.setActive(true);

        // Then
        assertThat(activate).isTrue();
    }

    @Before
    public void beforeWindowTest() {
        window = new Window();
        RootPanel.get().add(window);
        activate = false;
        deactivate = false;
        hide = false;
        maximize = false;
        minimize = false;
        restore = false;
        show = false;
    }

    @Test
    public void deactivate() {
        // Given
        window.addDeactivateHandler(new DeactivateHandler<Window>() {

            public void onDeactivate(DeactivateEvent<Window> event) {
                assertThat(event.getItem()).isEqualTo(window);
                deactivate = true;
            }
        });

        window.show();

        // When
        window.setActive(false);

        // Then
        assertThat(deactivate).isTrue();
    }

    @Test
    public void hide() {
        // Given
        window.addHideHandler(new HideHandler() {

            public void onHide(HideEvent event) {
                assertThat(event.getSource()).isEqualTo(window);
                hide = true;
            }
        });

        window.show();

        // When
        window.hide();

        // Then
        assertThat(hide).isTrue();
    }

    @Test
    public void maximize() {
        // Given
        window.addMaximizeHandler(new MaximizeHandler() {

            public void onMaximize(MaximizeEvent event) {
                assertThat(event.getSource()).isEqualTo(window);
                maximize = true;
            }
        });

        window.show();

        // When
        window.maximize();

        // Then
        assertThat(maximize).isTrue();
    }

    @Test
    public void minimize() {
        // Given
        window.addMinimizeHandler(new MinimizeHandler() {

            public void onMinimize(MinimizeEvent event) {
                assertThat(event.getSource()).isEqualTo(window);
                minimize = true;
            }
        });

        window.show();

        // When
        window.minimize();

        // Then
        assertThat(minimize).isTrue();
    }

    @Test
    public void restore() {
        // Given
        window.addRestoreHandler(new RestoreHandler() {

            public void onRestore(RestoreEvent event) {
                assertThat(event.getSource()).isEqualTo(window);
                restore = true;
            }
        });

        window.show();
        window.maximize();

        // When
        window.restore();

        // Then
        assertThat(restore).isTrue();
    }

    @Test
    public void show() {
        // Given
        window.addShowHandler(new ShowHandler() {

            public void onShow(ShowEvent event) {
                assertThat(event.getSource()).isEqualTo(window);
                show = true;
            }
        });

        // When
        window.show();

        // Then
        assertThat(show).isTrue();
    }

}

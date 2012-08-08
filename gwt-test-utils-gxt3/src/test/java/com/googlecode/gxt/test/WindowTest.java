package com.googlecode.gxt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.event.ActivateEvent;
import com.sencha.gxt.widget.core.client.event.ActivateEvent.ActivateHandler;
import com.sencha.gxt.widget.core.client.event.DeactivateEvent;
import com.sencha.gxt.widget.core.client.event.DeactivateEvent.DeactivateHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.MaximizeEvent;
import com.sencha.gxt.widget.core.client.event.MaximizeEvent.MaximizeHandler;
import com.sencha.gxt.widget.core.client.event.MinimizeEvent;
import com.sencha.gxt.widget.core.client.event.MinimizeEvent.MinimizeHandler;
import com.sencha.gxt.widget.core.client.event.RestoreEvent;
import com.sencha.gxt.widget.core.client.event.RestoreEvent.RestoreHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;

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
    // Arrange
    window.addActivateHandler(new ActivateHandler<Window>() {

      public void onActivate(ActivateEvent<Window> event) {
        assertEquals(window, event.getItem());
        activate = true;
      }

    });

    window.show();

    // Act
    window.setActive(true);

    // Assert
    assertTrue(activate);
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
    // Arrange
    window.addDeactivateHandler(new DeactivateHandler<Window>() {

      public void onDeactivate(DeactivateEvent<Window> event) {
        assertEquals(window, event.getItem());
        deactivate = true;
      }
    });

    window.show();

    // Act
    window.setActive(false);

    // Assert
    assertTrue(deactivate);
  }

  @Test
  public void hide() {
    // Arrange
    window.addHideHandler(new HideHandler() {

      public void onHide(HideEvent event) {
        assertEquals(window, event.getSource());
        hide = true;
      }
    });

    window.show();

    // Act
    window.hide();

    // Assert
    assertTrue(hide);
  }

  @Test
  public void maximize() {
    // Arrange
    window.addMaximizeHandler(new MaximizeHandler() {

      public void onMaximize(MaximizeEvent event) {
        assertEquals(window, event.getSource());
        maximize = true;
      }
    });

    window.show();

    // Act
    window.maximize();

    // Assert
    assertTrue(maximize);
  }

  @Test
  public void minimize() {
    // Arrange
    window.addMinimizeHandler(new MinimizeHandler() {

      public void onMinimize(MinimizeEvent event) {
        assertEquals(window, event.getSource());
        minimize = true;
      }
    });

    window.show();

    // Act
    window.minimize();

    // Assert
    assertTrue(minimize);
  }

  @Test
  public void restore() {
    // Arrange
    window.addRestoreHandler(new RestoreHandler() {

      public void onRestore(RestoreEvent event) {
        assertEquals(window, event.getSource());
        restore = true;
      }
    });

    window.show();
    window.maximize();

    // Act
    window.restore();

    // Assert
    assertTrue(restore);
  }

  @Test
  public void show() {
    // Arrange
    window.addShowHandler(new ShowHandler() {

      public void onShow(ShowEvent event) {
        assertEquals(window, event.getSource());
        show = true;
      }
    });

    // Act
    window.show();

    // Assert
    assertTrue(show);
  }

}

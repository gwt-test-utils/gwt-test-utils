package com.googlecode.gwt.test.gwtbootstrap;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mock;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.ModalFooter;
import com.github.gwtbootstrap.client.ui.Popover;
import com.github.gwtbootstrap.client.ui.Scrollspy;
import com.github.gwtbootstrap.client.ui.TabLink;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.github.gwtbootstrap.client.ui.event.CloseEvent;
import com.github.gwtbootstrap.client.ui.event.CloseHandler;
import com.github.gwtbootstrap.client.ui.event.ClosedEvent;
import com.github.gwtbootstrap.client.ui.event.ClosedHandler;
import com.github.gwtbootstrap.client.ui.event.HiddenEvent;
import com.github.gwtbootstrap.client.ui.event.HiddenHandler;
import com.github.gwtbootstrap.client.ui.event.HideEvent;
import com.github.gwtbootstrap.client.ui.event.HideHandler;
import com.github.gwtbootstrap.client.ui.event.ShowEvent;
import com.github.gwtbootstrap.client.ui.event.ShowHandler;
import com.github.gwtbootstrap.client.ui.event.ShownEvent;
import com.github.gwtbootstrap.client.ui.event.ShownHandler;
import com.google.gwt.user.client.ui.Label;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTestWithMockito;
import com.googlecode.gwt.test.gwtbootstrap.test.client.GwtBootstrapModule;

/**
 * Test for Patchers.
 * 
 * @author Kenichiro Tanaka
 * 
 */
@GwtModule("com.googlecode.gwt.test.gwtbootstrap.test.GwtBootstrapTest")
public class PatcherTest extends GwtTestWithMockito {

   @Mock
   private HiddenHandler hiddenHandler;
   @Mock
   private HideHandler hideHandler;
   @Mock
   private ShowHandler showHandler;
   @Mock
   private ShownHandler shownHandler;

   @Test
   public void testAlertBase() {
      // onAttach
      GwtBootstrapModule module = new GwtBootstrapModule();
      module.onModuleLoad();

      Alert alert = module.getWidget().getAlert();

      final CloseHandler closeHandler = mock(CloseHandler.class);
      alert.addCloseHandler(closeHandler);

      final ClosedHandler closedHandler = mock(ClosedHandler.class);
      alert.addClosedHandler(closedHandler);

      alert.close();

      verify(closeHandler).onClose(any(CloseEvent.class));
      verify(closedHandler).onClosed(any(ClosedEvent.class));
   }

   @Test
   public void testButtonLoadingState() {
      Button b = new Button("button");
      b.state().complete();
   }

   // FIXME : reactive this test when using gwtbootstrap 2.1.1.0
   /**
    * @Test public void testCollapse() { final CollapseTrigger trigger = new
    *       CollapseTrigger("#myCollapse"); final Button triggerButton = new Button("trigger");
    *       trigger.add(triggerButton);
    * 
    *       final Collapse collapse = new Collapse(); collapse.setId("myCollapse");
    *       collapse.setWidget(new Label("test")); collapse.setExistTrigger(true);
    *       collapse.setToggle(true);
    * 
    *       collapse.addShowHandler(showHandler); collapse.addShownHandler(shownHandler);
    *       collapse.addHideHandler(hideHandler); collapse.addHiddenHandler(hiddenHandler);
    * 
    *       collapse.asWidget(); trigger.asWidget();
    * 
    *       getBrowserSimulator().fireLoopEnd();
    * 
    *       collapse.hide(); verify(hideHandler).onHide(any(HideEvent.class));
    *       verify(hiddenHandler).onHidden(any(HiddenEvent.class));
    * 
    *       collapse.show(); verify(showHandler).onShow(any(ShowEvent.class));
    *       verify(shownHandler).onShown(any(ShownEvent.class));
    * 
    *       collapse.toggle(); verify(hideHandler, times(2)).onHide(any(HideEvent.class));
    *       verify(hiddenHandler, times(2)).onHidden(any(HiddenEvent.class));
    * 
    *       // call asWidget twice to invoke configure(String selector, String parent, boolean
    *       toggle) trigger.asWidget();
    * 
    *       // click to invoke changeVisibility(String target, String c)
    *       Browser.click(triggerButton);
    * 
    *       getBrowserSimulator().fireLoopEnd(); }
    * @Test public void testDropDown() { // onLoad GwtBootstrapModule module = new
    *       GwtBootstrapModule(); module.onModuleLoad(); }
    **/

   @Test
   public void testModal() {
      Modal modal = new Modal();
      modal.add(new Label("Modal"));

      ModalFooter footer = new ModalFooter();
      footer.add(new Button("close"));
      modal.add(footer);

      modal.addShowHandler(showHandler);
      modal.addShownHandler(shownHandler);
      modal.addHideHandler(hideHandler);
      modal.addHiddenHandler(hiddenHandler);

      assertFalse(modal.isVisible());

      modal.show();
      assertTrue(modal.isVisible());

      verify(showHandler).onShow(any(ShowEvent.class));
      verify(shownHandler).onShown(any(ShownEvent.class));

      modal.hide();
      assertFalse(modal.isVisible());

      verify(hideHandler).onHide(any(HideEvent.class));
      verify(hiddenHandler).onHidden(any(HiddenEvent.class));

      modal.toggle();
      assertTrue(modal.isVisible());

      // for unsetHandlerFunctions
      modal.setDynamicSafe(true);
      modal.hide();
   }

   @Test
   public void testPopover() {
      Popover popover = new Popover();
      popover.setHeading("heading");
      popover.setText("conetnt");

      popover.setWidget(new Button("test"));

      popover.show();
      popover.hide();

      // for configure()
      popover.asWidget();
      this.getBrowserSimulator().fireLoopEnd();
   }

   @Test
   public void testScrollspy() {
      Scrollspy spy = new Scrollspy();
      spy.configure();
      spy.refresh();
   }

   @Test
   public void testTabLink() {
      TabLink tabLink = new TabLink();
      tabLink.setText("Tab");
      tabLink.show();
   }

   @Test
   public void testTooltip() {
      Tooltip tooltip = new Tooltip("test");
      tooltip.setWidget(new Button("button"));
      tooltip.show();
      tooltip.hide();

      // for configure()
      tooltip.asWidget();
      this.getBrowserSimulator().fireLoopEnd();

      // TODO: TooltipCellDecorator test.
   }

   @Override
   protected String getHostPagePath(String moduleFullQualifiedName) {
      return null;
   }
}

package com.googlecode.gwt.test.gwtbootstrap.test.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class GwtBootstrapModule implements EntryPoint {

   private TestWidget widget;

   public TestWidget getWidget() {
      return widget;
   }

   public void onModuleLoad() {
      FlowPanel panel = new FlowPanel();
      RootLayoutPanel.get().add(panel);

      widget = new TestWidget();
      panel.add(widget);
   }

}

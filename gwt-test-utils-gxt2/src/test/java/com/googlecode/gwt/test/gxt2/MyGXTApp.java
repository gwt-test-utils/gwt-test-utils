package com.googlecode.gwt.test.gxt2;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class MyGXTApp implements EntryPoint {

   public void onModuleLoad() {
      ContentPanel cp = new ContentPanel();
      cp.setHeading("Folder Contents");
      cp.setTitle("my title");
      cp.setSize(250, 140);
      cp.setPosition(10, 10);
      cp.setCollapsible(true);
      cp.setFrame(true);
      cp.setBodyStyle("backgroundColor: white;");
      cp.getHeader().addTool(new ToolButton("x-tool-gear"));
      cp.getHeader().addTool(new ToolButton("x-tool-close"));
      cp.addText("BogusText");
      cp.addButton(new com.extjs.gxt.ui.client.widget.button.Button("Ok"));

      cp.setIconStyle("tree-folder-open");
      RootPanel.get().add(cp);
      cp.layout();
   }

}

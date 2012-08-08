/*
 * Ext GWT 2.2.1 - Ext for GWT Copyright(c) 2007-2010, Ext JS, LLC. licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.desktop.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.desktop.client.Desktop;
import com.extjs.gxt.desktop.client.Shortcut;
import com.extjs.gxt.desktop.client.StartMenu;
import com.extjs.gxt.desktop.client.TaskBar;
import com.extjs.gxt.samples.resources.client.ResourcesData;
import com.extjs.gxt.samples.resources.client.model.Stock;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class DesktopApp implements EntryPoint {

   private Desktop desktop = new Desktop();

   public void onModuleLoad() {
      SelectionListener<MenuEvent> menuListener = new SelectionListener<MenuEvent>() {
         @Override
         public void componentSelected(MenuEvent me) {
            itemSelected(me);
         }
      };

      SelectionListener<ComponentEvent> shortcutListener = new SelectionListener<ComponentEvent>() {
         @Override
         public void componentSelected(ComponentEvent ce) {
            itemSelected(ce);
         }
      };

      Window gridWindow = createGridWindow();
      Window accordionWindow = createAccordionWindow();

      Shortcut s1 = new Shortcut();
      s1.setText("Grid Window");
      s1.setId("grid-win-shortcut");
      s1.setData("window", gridWindow);
      s1.addSelectionListener(shortcutListener);
      desktop.addShortcut(s1);

      Shortcut s2 = new Shortcut();
      s2.setText("Accordion Window");
      s2.setId("acc-win-shortcut");
      s2.setData("window", accordionWindow);
      s2.addSelectionListener(shortcutListener);
      desktop.addShortcut(s2);

      TaskBar taskBar = desktop.getTaskBar();

      StartMenu menu = taskBar.getStartMenu();
      menu.setHeading("Darrell Meyer");
      menu.setIconStyle("user");

      MenuItem menuItem = new MenuItem("Grid Window");
      menuItem.setData("window", gridWindow);
      menuItem.setIcon(IconHelper.createStyle("icon-grid"));
      menuItem.addSelectionListener(menuListener);
      menu.add(menuItem);

      menuItem = new MenuItem("Tab Window");
      menuItem.setIcon(IconHelper.createStyle("tabs"));
      menuItem.addSelectionListener(menuListener);
      menuItem.setData("window", createTabWindow());
      menu.add(menuItem);

      menuItem = new MenuItem("Accordion Window");
      menuItem.setIcon(IconHelper.createStyle("accordion"));
      menuItem.addSelectionListener(menuListener);
      menuItem.setData("window", accordionWindow);
      menu.add(menuItem);

      menuItem = new MenuItem("Bogus Submenu");
      menuItem.setIcon(IconHelper.createStyle("bogus"));

      Menu sub = new Menu();

      for (int i = 0; i < 5; i++) {
         MenuItem item = new MenuItem("Bogus Window " + (i + 1));
         item.setData("window", createBogusWindow(i));
         item.addSelectionListener(menuListener);
         sub.add(item);
      }

      menuItem.setSubMenu(sub);
      menu.add(menuItem);

      // tools
      MenuItem tool = new MenuItem("Settings");
      tool.setIcon(IconHelper.createStyle("settings"));
      tool.addSelectionListener(new SelectionListener<MenuEvent>() {
         @Override
         public void componentSelected(MenuEvent ce) {
            Info.display("Event", "The 'Settings' tool was clicked");
         }
      });
      menu.addTool(tool);

      menu.addToolSeperator();

      tool = new MenuItem("Logout");
      tool.setIcon(IconHelper.createStyle("logout"));
      tool.addSelectionListener(new SelectionListener<MenuEvent>() {
         @Override
         public void componentSelected(MenuEvent ce) {
            Info.display("Event", "The 'Logout' tool was clicked");
         }
      });
      menu.addTool(tool);
   }

   private Window createAccordionWindow() {
      final Window w = new Window();
      w.setMinimizable(true);
      w.setMaximizable(true);
      w.setIcon(IconHelper.createStyle("accordion"));
      w.setHeading("Accordion Window");
      w.setWidth(200);
      w.setHeight(350);

      ToolBar toolBar = new ToolBar();
      Button item = new Button();
      item.setIcon(IconHelper.createStyle("icon-connect"));
      toolBar.add(item);

      toolBar.add(new SeparatorToolItem());
      w.setTopComponent(toolBar);

      item = new Button();
      item.setIcon(IconHelper.createStyle("icon-user-add"));
      toolBar.add(item);

      item = new Button();
      item.setIcon(IconHelper.createStyle("icon-user-delete"));
      toolBar.add(item);

      w.setLayout(new AccordionLayout());

      ContentPanel cp = new ContentPanel();
      cp.setAnimCollapse(false);
      cp.setHeading("Online Users");
      cp.setScrollMode(Scroll.AUTO);
      cp.getHeader().addTool(new ToolButton("x-tool-refresh"));

      w.add(cp);

      TreeStore<ModelData> store = new TreeStore<ModelData>();
      TreePanel<ModelData> tree = new TreePanel<ModelData>(store);
      tree.setIconProvider(new ModelIconProvider<ModelData>() {

         public AbstractImagePrototype getIcon(ModelData model) {
            if (model.get("icon") != null) {
               return IconHelper.createStyle((String) model.get("icon"));
            } else {
               return null;
            }
         }

      });
      tree.setDisplayProperty("name");

      ModelData m = newItem("Family", null);
      store.add(m, false);
      tree.setExpanded(m, true);

      store.add(m, newItem("Darrell", "user"), false);
      store.add(m, newItem("Maro", "user-girl"), false);
      store.add(m, newItem("Lia", "user-kid"), false);
      store.add(m, newItem("Alec", "user-kid"), false);
      store.add(m, newItem("Andrew", "user-kid"), false);

      m = newItem("Friends", null);
      store.add(m, false);
      tree.setExpanded(m, true);
      store.add(m, newItem("Bob", "user"), false);
      store.add(m, newItem("Mary", "user-girl"), false);
      store.add(m, newItem("Sally", "user-girl"), false);
      store.add(m, newItem("Jack", "user"), false);

      cp.add(tree);

      cp = new ContentPanel();
      cp.setAnimCollapse(false);
      cp.setHeading("Settings");
      cp.setBodyStyleName("pad-text");
      cp.addText(ResourcesData.DUMMY_TEXT_SHORT);
      w.add(cp);

      cp = new ContentPanel();
      cp.setAnimCollapse(false);
      cp.setHeading("Stuff");
      cp.setBodyStyleName("pad-text");
      cp.addText(ResourcesData.DUMMY_TEXT_SHORT);
      w.add(cp);

      cp = new ContentPanel();
      cp.setAnimCollapse(false);
      cp.setHeading("More Stuff");
      cp.setBodyStyleName("pad-text");
      cp.addText(ResourcesData.DUMMY_TEXT_SHORT);
      w.add(cp);
      return w;
   }

   private Window createBogusWindow(int index) {
      Window w = new Window();
      w.setIcon(IconHelper.createStyle("bogus"));
      w.setMinimizable(true);
      w.setMaximizable(true);
      w.setHeading("Bogus Window " + ++index);
      w.setSize(400, 300);
      return w;
   }

   private Window createGridWindow() {
      Window w = new Window();
      w.setIcon(IconHelper.createStyle("icon-grid"));
      w.setMinimizable(true);
      w.setMaximizable(true);
      w.setHeading("Grid Window");
      w.setSize(500, 400);
      w.setLayout(new FitLayout());

      GroupingStore<Stock> store = new GroupingStore<Stock>();
      store.add(ResourcesData.getCompanies());
      store.groupBy("industry");

      ColumnConfig company = new ColumnConfig("name", "Company", 60);
      ColumnConfig price = new ColumnConfig("open", "Price", 20);
      price.setNumberFormat(NumberFormat.getCurrencyFormat());
      ColumnConfig change = new ColumnConfig("change", "Change", 20);
      ColumnConfig industry = new ColumnConfig("industry", "Industry", 20);
      ColumnConfig last = new ColumnConfig("date", "Last Updated", 20);
      last.setDateTimeFormat(DateTimeFormat.getFormat("MM/dd/y"));

      List<ColumnConfig> config = new ArrayList<ColumnConfig>();
      config.add(company);
      config.add(price);
      config.add(change);
      config.add(industry);
      config.add(last);

      final ColumnModel cm = new ColumnModel(config);

      GroupingView view = new GroupingView();
      view.setForceFit(true);
      view.setGroupRenderer(new GridGroupRenderer() {
         public String render(GroupColumnData data) {
            String f = cm.getColumnById(data.field).getHeader();
            String l = data.models.size() == 1 ? "Item" : "Items";
            return f + ": " + data.group + " (" + data.models.size() + " " + l + ")";
         }
      });

      Grid<Stock> grid = new Grid<Stock>(store, cm);
      grid.setView(view);
      grid.setBorders(true);

      w.add(grid);
      return w;
   }

   private Window createTabWindow() {
      Window w = new Window();
      w.setMinimizable(true);
      w.setMaximizable(true);
      w.setSize(740, 480);
      w.setIcon(IconHelper.createStyle("tabs"));
      w.setHeading("Tab Window");

      w.setLayout(new FitLayout());

      TabPanel panel = new TabPanel();

      for (int i = 0; i < 4; i++) {
         TabItem item = new TabItem("Tab Item " + (i + 1));
         item.addText("Something useful would be here");
         panel.add(item);
      }

      w.add(panel);
      return w;
   }

   private void itemSelected(ComponentEvent ce) {
      Window w;
      if (ce instanceof MenuEvent) {
         MenuEvent me = (MenuEvent) ce;
         w = me.getItem().getData("window");
      } else {
         w = ce.getComponent().getData("window");
      }
      if (!desktop.getWindows().contains(w)) {
         desktop.addWindow(w);
      }
      if (w != null && !w.isVisible()) {
         w.show();
      } else {
         w.toFront();
      }
   }

   private ModelData newItem(String text, String iconStyle) {
      ModelData m = new BaseModelData();
      m.set("name", text);
      m.set("icon", iconStyle);
      return m;
   }
}

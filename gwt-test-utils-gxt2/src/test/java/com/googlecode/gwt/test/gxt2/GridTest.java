package com.googlecode.gwt.test.gxt2;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.extjs.gxt.samples.resources.client.ResourcesData;
import com.extjs.gxt.samples.resources.client.model.Stock;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;

public class GridTest extends GwtGxtTest {

   @Test
   public void selection() {
      // Arrange
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

      List<Stock> selected = store.getModels().subList(0, 1);

      // Act
      grid.getSelectionModel().setSelection(selected);

      // Assert
      assertEquals(selected, grid.getSelectionModel().getSelectedItems());
   }

}

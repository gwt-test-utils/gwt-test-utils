package com.googlecode.gwt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.test.utils.events.Browser;

@SuppressWarnings("deprecation")
public class GridTest extends GwtTestTest {

   private boolean clicked = false;

   @Test
   public void addStyleName() {
      // Arrange
      // Grids must be sized explicitly, though they can be resized later.
      Grid g = new Grid(1, 1);

      // Act
      g.getRowFormatter().addStyleName(0, "style");

      // Assert
      assertEquals("style", g.getRowFormatter().getStyleName(0));
   }

   @Test
   public void click_ClickHander() {
      // Arrange
      clicked = false;
      final Grid g = new Grid(1, 1);
      final Button b = new Button("Does nothing, but could");
      g.setWidget(0, 0, b);
      g.addClickHandler(new ClickHandler() {

         public void onClick(ClickEvent event) {
            clicked = !clicked;
            assertEquals(b, ((Grid) event.getSource()).getWidget(0, 0));

         }
      });

      // Act
      Browser.click(g, 0, 0);

      // Assert
      assertTrue("TableListener should have been notified", clicked);

   }

   @Test
   public void click_ClickHandler_NestedWidget() {
      // Arrange
      // Grids must be sized explicitly, though they can be resized later.
      Grid g = new Grid(1, 1);

      Button b = new Button();

      b.addClickHandler(new ClickHandler() {

         public void onClick(ClickEvent event) {
            clicked = !clicked;

         }
      });
      // add the button
      g.setWidget(0, 0, b);

      // Pre-Assert
      assertEquals(false, clicked);

      // Act
      Browser.click(g.getWidget(0, 0));

      // Assert
      assertEquals(true, clicked);
   }

   @Test
   public void click_ClickListener_NestedWidget() {
      // Arrange
      // Grids must be sized explicitly, though they can be resized later.
      Grid g = new Grid(1, 1);

      Button b = new Button();

      b.addClickListener(new ClickListener() {

         public void onClick(Widget sender) {
            clicked = !clicked;

         }
      });
      // add the button
      g.setWidget(0, 0, b);

      // Pre-Assert
      assertEquals(false, clicked);

      // Act
      Browser.click(g.getWidget(0, 0));

      // Assert
      assertEquals(true, clicked);
   }

   @Test
   public void click_TableListner() {
      // Arrange
      clicked = false;
      Grid g = new Grid(1, 1);
      Button b = new Button("Does nothing, but could");
      g.setWidget(0, 0, b);
      g.addTableListener(new TableListener() {

         public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
            clicked = !clicked;
         }

      });

      // Act
      Browser.click(g, 0, 0);

      // Assert
      assertTrue("TableListener should have been notified", clicked);
   }

   @Test
   public void html() {
      // Arrange
      Grid g = new Grid(1, 1);

      // Act
      g.setHTML(0, 0, "<h1>test</h1>");

      // Assert
      assertEquals("<h1>test</h1>", g.getHTML(0, 0));
      Element e = g.getCellFormatter().getElement(0, 0);
      assertEquals(1, e.getChildCount());
      HeadingElement h1 = e.getChild(0).cast();
      assertEquals("H1", h1.getTagName());
      assertEquals("test", h1.getInnerText());
   }

   @Test
   public void removeFromGrid() {
      // Arrange
      // Grids must be sized explicitly, though they can be resized later.
      Grid g = new Grid(1, 1);
      Button b = new Button("Does nothing, but could");
      g.setWidget(0, 0, b);

      // Act & Assert
      assertTrue("The button has not been removed from grid", g.remove(b));
   }

   @Test
   public void resizeRow() {
      // Arrange
      Grid g = new Grid(1, 1);
      g.setWidget(0, 0, new Label("first"));
      // Pre-Assert
      assertEquals("<div class=\"gwt-Label\">first</div>", g.getHTML(0, 0));

      // Act
      g.resize(2, 2);

      // Assert
      assertEquals("<div class=\"gwt-Label\">first</div>", g.getHTML(0, 0));
      assertEquals("&nbsp;", g.getHTML(0, 1));
      assertEquals("&nbsp;", g.getHTML(1, 0));
      assertEquals("&nbsp;", g.getHTML(1, 1));
   }

   @Test
   public void setText() {
      // Arrange
      // Grids must be sized explicitly, though they can be resized later.
      Grid g = new Grid(5, 5);

      // Put some values in the grid cells.
      for (int row = 0; row < 5; ++row) {
         for (int col = 0; col < 5; ++col)
            // Act
            g.setText(row, col, "" + row + ", " + col);
      }

      // Assert
      assertEquals("0, 0", g.getText(0, 0));
      assertEquals("3, 2", g.getText(3, 2));
      assertEquals("4, 4", g.getText(4, 4));
   }

   @Test
   public void setWidget() {
      // Arrange
      Grid g = new Grid(3, 3);
      Button b = new Button("Does nothing, but could");

      // Act
      g.setWidget(2, 2, b);

      // Assert
      assertEquals(b, g.getWidget(2, 2));
   }

   @Test
   public void text() {
      // Arrange
      Grid g = new Grid(1, 1);

      // Act
      g.setText(0, 0, "text");

      // Assert
      assertEquals("text", g.getText(0, 0));
   }

   @Test
   public void title() {
      // Arrange
      Grid g = new Grid(1, 1);

      // Act
      g.setTitle("title");

      // Assert
      assertEquals("title", g.getTitle());
   }

   @Test
   public void visible() {
      // Arrange
      Grid g = new Grid(1, 1);
      // Pre-Assert
      assertEquals(true, g.isVisible());

      // Act
      g.setVisible(false);

      // Assert
      assertEquals(false, g.isVisible());
   }

}

package com.googlecode.gwt.test.dom;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.ListStyleType;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.user.client.ui.Button;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.utils.WidgetUtils;

public class StyleTest extends GwtTestTest {

   @Test
   public void checkFloat() {
      // Arrange
      Button button = new Button();
      Style style = button.getElement().getStyle();

      // Act 1
      style.setFloat(Style.Float.RIGHT);

      // Assert 1
      assertEquals(Style.Float.RIGHT.getCssName(), style.getProperty("float"));
      assertEquals("float: right; ", button.getElement().getAttribute("style"));

      // Act 2
      style.clearFloat();

      // Assert 2
      assertEquals("", style.getProperty("float"));
      assertEquals("", button.getElement().getAttribute("style"));
   }

   @Test
   public void opacity() {
      // Arrange
      Button button = new Button();
      Style style = button.getElement().getStyle();

      // Act 1
      style.setOpacity(1.0);

      // Assert 1
      assertEquals("1", style.getOpacity());
      assertEquals("opacity: 1; ", button.getElement().getAttribute("style"));

      // Act 2
      style.setOpacity(0.94);

      // Assert 2
      assertEquals("0.94", style.getOpacity());
      assertEquals("opacity: 0.94; ", button.getElement().getAttribute("style"));

      // Act 3
      style.clearOpacity();
      assertEquals("", button.getElement().getAttribute("style"));

      // Assert 3
      assertEquals("", style.getOpacity());
   }

   @Test
   public void properties() {
      // Arrange
      Button button = new Button();
      Style style = button.getElement().getStyle();

      // Act 1
      style.setBackgroundColor("black");
      style.setBackgroundImage("/img.png");
      style.setBorderColor("blue");
      style.setBorderWidth(1.0, Unit.EM);
      style.setBottom(4.0, Unit.PX);
      style.setColor("red");
      style.setCursor(Style.Cursor.E_RESIZE);
      style.setFloat(Float.LEFT);
      style.setDisplay(Display.INLINE_BLOCK);
      style.setFontSize(10.5, Style.Unit.CM);
      style.setFontStyle(Style.FontStyle.NORMAL);
      style.setFontWeight(Style.FontWeight.BOLD);
      style.setHeight(3.1, Style.Unit.PC);
      style.setLeft(40, Style.Unit.IN);
      style.setListStyleType(ListStyleType.CIRCLE);
      style.setMargin(30.5, Style.Unit.PCT);
      style.setMarginBottom(29, Style.Unit.PT);
      style.setMarginLeft(47, Style.Unit.EX);
      style.setMarginRight(3, Style.Unit.MM);
      style.setMarginTop(10.3, Style.Unit.CM);
      style.setOverflow(Overflow.SCROLL);
      style.setPadding(10, Style.Unit.PX);
      style.setPaddingBottom(11, Style.Unit.PX);
      style.setPaddingLeft(12, Style.Unit.PX);
      style.setPaddingRight(13, Style.Unit.PX);
      style.setPaddingTop(14, Style.Unit.PX);
      style.setPosition(Position.RELATIVE);
      style.setProperty("string", "stringvalue");
      style.setProperty("doubleUnit", 17.2, Style.Unit.CM);
      style.setRight(13.4, Style.Unit.CM);
      style.setTextDecoration(TextDecoration.OVERLINE);
      style.setTop(7.77, Style.Unit.PC);
      style.setVerticalAlign(VerticalAlign.MIDDLE);
      style.setVisibility(Visibility.VISIBLE);
      style.setWidth(3.5, Style.Unit.PX);
      style.setZIndex(1000);

      // Assert 1
      assertEquals(
               "background-color: black; background-image: /img.png; border-color: blue; border-bottom-width: 1em; border-left-width: 1em; border-right-width: 1em; border-top-width: 1em; bottom: 4px; color: red; cursor: e-resize; float: left; display: inline-block; font-size: 10.5cm; font-style: normal; font-weight: bold; height: 3.1pc; left: 40in; list-style-type: circle; margin: 30.5%; margin-bottom: 29pt; margin-left: 47ex; margin-right: 3mm; margin-top: 10.3cm; overflow: scroll; padding: 10px; padding-bottom: 11px; padding-left: 12px; padding-right: 13px; padding-top: 14px; position: relative; string: stringvalue; double-unit: 17.2cm; right: 13.4cm; text-decoration: overline; top: 7.77pc; vertical-align: middle; visibility: visible; width: 3.5px; z-index: 1000; ",
               button.getElement().getAttribute("style"));
      assertEquals("black", style.getBackgroundColor());
      assertEquals("/img.png", style.getBackgroundImage());
      assertEquals("blue", style.getBorderColor());
      assertEquals("1em", style.getBorderWidth());
      assertEquals("4px", style.getBottom());
      assertEquals("red", style.getColor());
      assertEquals(Style.Cursor.E_RESIZE.getCssName(), style.getCursor());
      assertEquals(Display.INLINE_BLOCK.getCssName(), style.getDisplay());
      assertEquals("10.5cm", style.getFontSize());
      assertEquals(Style.FontStyle.NORMAL.getCssName(), style.getFontStyle());
      assertEquals(Style.FontWeight.BOLD.getCssName(), style.getFontWeight());
      assertEquals("3.1pc", style.getHeight());
      assertEquals("40in", style.getLeft());
      assertEquals(ListStyleType.CIRCLE.getCssName(), style.getListStyleType());
      assertEquals("30.5%", style.getMargin());
      assertEquals("29pt", style.getMarginBottom());
      assertEquals("47ex", style.getMarginLeft());
      assertEquals("3mm", style.getMarginRight());
      assertEquals("10.3cm", style.getMarginTop());
      assertEquals(Overflow.SCROLL.getCssName(), style.getOverflow());
      assertEquals("10px", style.getPadding());
      assertEquals("11px", style.getPaddingBottom());
      assertEquals("12px", style.getPaddingLeft());
      assertEquals("13px", style.getPaddingRight());
      assertEquals("14px", style.getPaddingTop());
      assertEquals(Position.RELATIVE.getCssName(), style.getPosition());
      assertEquals("stringvalue", style.getProperty("string"));
      assertEquals("17.2cm", style.getProperty("doubleUnit"));
      assertEquals("13.4cm", style.getRight());
      assertEquals(TextDecoration.OVERLINE.getCssName(), style.getTextDecoration());
      assertEquals("7.77pc", style.getTop());
      assertEquals(VerticalAlign.MIDDLE.getCssName(), style.getVerticalAlign());
      assertEquals(Visibility.VISIBLE.getCssName(), style.getVisibility());
      assertEquals("3.5px", style.getWidth());
      assertEquals("1000", style.getZIndex());

      // Act2
      style.clearBackgroundColor();
      style.clearBackgroundImage();
      style.clearBorderColor();
      style.clearBorderWidth();
      style.clearBottom();
      style.clearColor();
      style.clearCursor();
      style.clearDisplay();
      style.clearFloat();
      style.clearFontSize();
      style.clearFontStyle();
      style.clearFontWeight();
      style.clearHeight();
      style.clearLeft();
      style.clearListStyleType();
      style.clearMargin();
      style.clearMarginBottom();
      style.clearMarginLeft();
      style.clearMarginRight();
      style.clearMarginTop();
      style.clearOverflow();
      style.clearPadding();
      style.clearPaddingBottom();
      style.clearPaddingLeft();
      style.clearPaddingRight();
      style.clearPaddingTop();
      style.clearPosition();
      style.clearProperty("string");
      style.clearProperty("doubleUnit");
      style.clearRight();
      style.clearTextDecoration();
      style.clearTop();
      style.clearVisibility();
      style.clearWidth();
      style.clearZIndex();

      // Assert 2
      // the only style we didn't remove in the test
      assertEquals("vertical-align: middle; ", button.getElement().getAttribute("style"));
      assertEquals("", style.getBackgroundColor());
      assertEquals("", style.getBackgroundImage());
      assertEquals("", style.getBorderColor());
      assertEquals("", style.getBorderWidth());
      assertEquals("", style.getBottom());
      assertEquals("", style.getColor());
      assertEquals("", style.getCursor());
      assertEquals("", style.getDisplay());
      assertEquals("", style.getFontSize());
      assertEquals("", style.getFontStyle());
      assertEquals("", style.getFontWeight());
      assertEquals("", style.getHeight());
      assertEquals("", style.getLeft());
      assertEquals("", style.getListStyleType());
      assertEquals("", style.getMargin());
      assertEquals("", style.getMarginBottom());
      assertEquals("", style.getMarginLeft());
      assertEquals("", style.getMarginRight());
      assertEquals("", style.getMarginTop());
      assertEquals("", style.getOverflow());
      assertEquals("", style.getPadding());
      assertEquals("", style.getPaddingBottom());
      assertEquals("", style.getPaddingLeft());
      assertEquals("", style.getPaddingRight());
      assertEquals("", style.getPaddingTop());
      assertEquals("", style.getPosition());
      assertEquals("", style.getProperty("string"));
      assertEquals("", style.getProperty("doubleUnit"));
      assertEquals("", style.getRight());
      assertEquals("", style.getTextDecoration());
      assertEquals("", style.getTop());
      assertEquals("", style.getVisibility());
      assertEquals("", style.getWidth());
      assertEquals("", style.getZIndex());

   }

   @Test
   public void style_empty() {
      // Arrange
      Button button = new Button();
      Style style = button.getElement().getStyle();

      // Act
      assertEquals("", style.getBackgroundColor());
      assertEquals("", style.getBackgroundImage());
      assertEquals("", style.getBorderColor());
      assertEquals("", style.getBorderWidth());
      assertEquals("", style.getBottom());
      assertEquals("", style.getColor());
      assertEquals("", style.getCursor());
      assertEquals("", style.getDisplay());
      assertEquals("", style.getFontSize());
      assertEquals("", style.getFontStyle());
      assertEquals("", style.getFontWeight());
      assertEquals("", style.getHeight());
      assertEquals("", style.getLeft());
      assertEquals("", style.getListStyleType());
      assertEquals("", style.getMargin());
      assertEquals("", style.getMarginBottom());
      assertEquals("", style.getMarginLeft());
      assertEquals("", style.getMarginRight());
      assertEquals("", style.getMarginTop());
      assertEquals("", style.getOpacity());
      assertEquals("", style.getOverflow());
      assertEquals("", style.getPadding());
      assertEquals("", style.getPaddingBottom());
      assertEquals("", style.getPaddingLeft());
      assertEquals("", style.getPaddingRight());
      assertEquals("", style.getPaddingTop());
      assertEquals("", style.getPosition());
      assertEquals("", style.getProperty("empty"));
      assertEquals("", style.getRight());
      assertEquals("", style.getTextDecoration());
      assertEquals("", style.getTop());
      assertEquals("", style.getVerticalAlign());
      assertEquals("", style.getVisibility());
      assertEquals("", style.getWidth());
      assertEquals("", style.getZIndex());
   }

   @Test
   public void styles() {
      // Arrange
      Button b = new Button();
      b.setStylePrimaryName("toto");
      b.addStyleName("tata");
      b.addStyleName("titi");

      // Act & Asserts
      assertEquals("toto", b.getStylePrimaryName());
      assertEquals(true, WidgetUtils.hasStyle(b, "tata"));
      assertEquals(true, WidgetUtils.hasStyle(b, "titi"));
      assertEquals(true, WidgetUtils.hasStyle(b, "toto"));
   }
}

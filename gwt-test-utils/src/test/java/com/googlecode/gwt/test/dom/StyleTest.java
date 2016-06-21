package com.googlecode.gwt.test.dom;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.*;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.user.client.ui.Button;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.assertions.GwtAssertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StyleTest extends GwtTestTest {

    @Test
    public void checkFloat() {
        // Given
        Button button = new Button();
        Style style = button.getElement().getStyle();

        // When 1
        style.setFloat(Style.Float.RIGHT);

        // Then 1
        assertThat(style.getProperty("float")).isEqualTo(Style.Float.RIGHT.getCssName());
        assertThat(button.getElement().getAttribute("style")).isEqualTo("float: right; ");

        // When 2
        style.clearFloat();

        // Then 2
        assertThat(style.getProperty("float")).isEqualTo("");
        assertThat(button.getElement().getAttribute("style")).isEqualTo("");
    }

    @Test
    public void opacity() {
        // Given
        Button button = new Button();
        Style style = button.getElement().getStyle();

        // When 1
        style.setOpacity(1.0);

        // Then 1
        assertThat(style.getOpacity()).isEqualTo("1");
        assertThat(button.getElement().getAttribute("style")).isEqualTo("opacity: 1; ");

        // When 2
        style.setOpacity(0.94);

        // Then 2
        assertThat(style.getOpacity()).isEqualTo("0.94");
        assertThat(button.getElement().getAttribute("style")).isEqualTo("opacity: 0.94; ");

        // When 3
        style.clearOpacity();
        assertThat(button.getElement().getAttribute("style")).isEqualTo("");

        // Then 3
        assertThat(style.getOpacity()).isEqualTo("");
    }

    @Test
    public void properties() {
        // Given
        Button button = new Button();
        Style style = button.getElement().getStyle();

        // When 1
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

        // Then 1
        assertThat(button.getElement().getAttribute("style")).isEqualTo("background-color: black; background-image: /img.png; border-color: blue; border-bottom-width: 1em; border-left-width: 1em; border-right-width: 1em; border-top-width: 1em; bottom: 4px; color: red; cursor: e-resize; float: left; display: inline-block; font-size: 10.5cm; font-style: normal; font-weight: bold; height: 3.1pc; left: 40in; list-style-type: circle; margin: 30.5%; margin-bottom: 29pt; margin-left: 47ex; margin-right: 3mm; margin-top: 10.3cm; overflow: scroll; padding: 10px; padding-bottom: 11px; padding-left: 12px; padding-right: 13px; padding-top: 14px; position: relative; string: stringvalue; double-unit: 17.2cm; right: 13.4cm; text-decoration: overline; top: 7.77pc; vertical-align: middle; visibility: visible; width: 3.5px; z-index: 1000; ");
        assertThat(style.getBackgroundColor()).isEqualTo("black");
        assertThat(style.getBackgroundImage()).isEqualTo("/img.png");
        assertThat(style.getBorderColor()).isEqualTo("blue");
        assertThat(style.getBorderWidth()).isEqualTo("1em");
        assertThat(style.getBottom()).isEqualTo("4px");
        assertThat(style.getColor()).isEqualTo("red");
        assertThat(style.getCursor()).isEqualTo(Style.Cursor.E_RESIZE.getCssName());
        assertThat(style.getDisplay()).isEqualTo(Display.INLINE_BLOCK.getCssName());
        assertThat(style.getFontSize()).isEqualTo("10.5cm");
        assertThat(style.getFontStyle()).isEqualTo(Style.FontStyle.NORMAL.getCssName());
        assertThat(style.getFontWeight()).isEqualTo(Style.FontWeight.BOLD.getCssName());
        assertThat(style.getHeight()).isEqualTo("3.1pc");
        assertThat(style.getLeft()).isEqualTo("40in");
        assertThat(style.getListStyleType()).isEqualTo(ListStyleType.CIRCLE.getCssName());
        assertThat(style.getMargin()).isEqualTo("30.5%");
        assertThat(style.getMarginBottom()).isEqualTo("29pt");
        assertThat(style.getMarginLeft()).isEqualTo("47ex");
        assertThat(style.getMarginRight()).isEqualTo("3mm");
        assertThat(style.getMarginTop()).isEqualTo("10.3cm");
        assertThat(style.getOverflow()).isEqualTo(Overflow.SCROLL.getCssName());
        assertThat(style.getPadding()).isEqualTo("10px");
        assertThat(style.getPaddingBottom()).isEqualTo("11px");
        assertThat(style.getPaddingLeft()).isEqualTo("12px");
        assertThat(style.getPaddingRight()).isEqualTo("13px");
        assertThat(style.getPaddingTop()).isEqualTo("14px");
        assertThat(style.getPosition()).isEqualTo(Position.RELATIVE.getCssName());
        assertThat(style.getProperty("string")).isEqualTo("stringvalue");
        assertThat(style.getProperty("doubleUnit")).isEqualTo("17.2cm");
        assertThat(style.getRight()).isEqualTo("13.4cm");
        assertThat(style.getTextDecoration()).isEqualTo(TextDecoration.OVERLINE.getCssName());
        assertThat(style.getTop()).isEqualTo("7.77pc");
        assertThat(style.getVerticalAlign()).isEqualTo(VerticalAlign.MIDDLE.getCssName());
        assertThat(style.getVisibility()).isEqualTo(Visibility.VISIBLE.getCssName());
        assertThat(style.getWidth()).isEqualTo("3.5px");
        assertThat(style.getZIndex()).isEqualTo("1000");

        // When2
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

        // Then 2
        // the only style we didn't remove in the test
        assertThat(button.getElement().getAttribute("style")).isEqualTo("vertical-align: middle; ");
        assertThat(style.getBackgroundColor()).isEqualTo("");
        assertThat(style.getBackgroundImage()).isEqualTo("");
        assertThat(style.getBorderColor()).isEqualTo("");
        assertThat(style.getBorderWidth()).isEqualTo("");
        assertThat(style.getBottom()).isEqualTo("");
        assertThat(style.getColor()).isEqualTo("");
        assertThat(style.getCursor()).isEqualTo("");
        assertThat(style.getDisplay()).isEqualTo("");
        assertThat(style.getFontSize()).isEqualTo("");
        assertThat(style.getFontStyle()).isEqualTo("");
        assertThat(style.getFontWeight()).isEqualTo("");
        assertThat(style.getHeight()).isEqualTo("");
        assertThat(style.getLeft()).isEqualTo("");
        assertThat(style.getListStyleType()).isEqualTo("");
        assertThat(style.getMargin()).isEqualTo("");
        assertThat(style.getMarginBottom()).isEqualTo("");
        assertThat(style.getMarginLeft()).isEqualTo("");
        assertThat(style.getMarginRight()).isEqualTo("");
        assertThat(style.getMarginTop()).isEqualTo("");
        assertThat(style.getOverflow()).isEqualTo("");
        assertThat(style.getPadding()).isEqualTo("");
        assertThat(style.getPaddingBottom()).isEqualTo("");
        assertThat(style.getPaddingLeft()).isEqualTo("");
        assertThat(style.getPaddingRight()).isEqualTo("");
        assertThat(style.getPaddingTop()).isEqualTo("");
        assertThat(style.getPosition()).isEqualTo("");
        assertThat(style.getProperty("string")).isEqualTo("");
        assertThat(style.getProperty("doubleUnit")).isEqualTo("");
        assertThat(style.getRight()).isEqualTo("");
        assertThat(style.getTextDecoration()).isEqualTo("");
        assertThat(style.getTop()).isEqualTo("");
        assertThat(style.getVisibility()).isEqualTo("");
        assertThat(style.getWidth()).isEqualTo("");
        assertThat(style.getZIndex()).isEqualTo("");

    }

    @Test
    public void style_empty() {
        // Given
        Button button = new Button();
        Style style = button.getElement().getStyle();

        // When
        assertThat(style.getBackgroundColor()).isEqualTo("");
        assertThat(style.getBackgroundImage()).isEqualTo("");
        assertThat(style.getBorderColor()).isEqualTo("");
        assertThat(style.getBorderWidth()).isEqualTo("");
        assertThat(style.getBottom()).isEqualTo("");
        assertThat(style.getColor()).isEqualTo("");
        assertThat(style.getCursor()).isEqualTo("");
        assertThat(style.getDisplay()).isEqualTo("");
        assertThat(style.getFontSize()).isEqualTo("");
        assertThat(style.getFontStyle()).isEqualTo("");
        assertThat(style.getFontWeight()).isEqualTo("");
        assertThat(style.getHeight()).isEqualTo("");
        assertThat(style.getLeft()).isEqualTo("");
        assertThat(style.getListStyleType()).isEqualTo("");
        assertThat(style.getMargin()).isEqualTo("");
        assertThat(style.getMarginBottom()).isEqualTo("");
        assertThat(style.getMarginLeft()).isEqualTo("");
        assertThat(style.getMarginRight()).isEqualTo("");
        assertThat(style.getMarginTop()).isEqualTo("");
        assertThat(style.getOpacity()).isEqualTo("");
        assertThat(style.getOverflow()).isEqualTo("");
        assertThat(style.getPadding()).isEqualTo("");
        assertThat(style.getPaddingBottom()).isEqualTo("");
        assertThat(style.getPaddingLeft()).isEqualTo("");
        assertThat(style.getPaddingRight()).isEqualTo("");
        assertThat(style.getPaddingTop()).isEqualTo("");
        assertThat(style.getPosition()).isEqualTo("");
        assertThat(style.getProperty("empty")).isEqualTo("");
        assertThat(style.getRight()).isEqualTo("");
        assertThat(style.getTextDecoration()).isEqualTo("");
        assertThat(style.getTop()).isEqualTo("");
        assertThat(style.getVerticalAlign()).isEqualTo("");
        assertThat(style.getVisibility()).isEqualTo("");
        assertThat(style.getWidth()).isEqualTo("");
        assertThat(style.getZIndex()).isEqualTo("");
    }

    @Test
    public void styles() {
        // Given
        Button b = new Button();
        b.setStylePrimaryName("toto");
        b.addStyleName("tata");
        b.addStyleName("titi");

        // When & Thens
        assertThat(b.getStylePrimaryName()).isEqualTo("toto");
        GwtAssertions.assertThat(b).hasStyle("tata", "titi", "toto");
    }
}

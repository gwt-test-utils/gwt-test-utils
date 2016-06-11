package com.googlecode.gwt.test.internal.utils;

import com.google.gwt.dom.client.*;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GwtHtmlParserTest extends GwtTestTest {

    @Test
    public void parse() throws Exception {
        // Arrange
        String html = "<div id=\"parent0\"></div><div id=\"parent1\"><div id=\"child0\"><span class=\"spanClass\" >test</span></div><BR><DIV id=\"child2\" style=\"color:red; font-style:italic; font-weight:bold; font-family:Arial\"></div>";

        // Act
        NodeList<Node> nodes = GwtHtmlParser.parse(html);

        // Assert
        assertEquals(2, nodes.getLength());
        DivElement parent0 = (DivElement) nodes.getItem(0);
        assertEquals("parent0", parent0.getId());
        assertEquals(0, parent0.getChildCount());

        DivElement parent1 = (DivElement) nodes.getItem(1);
        assertEquals("parent1", parent1.getId());
        assertEquals(3, parent1.getChildCount());

        DivElement child0 = (DivElement) parent1.getChild(0);
        assertEquals("child0", child0.getId());
        assertEquals(1, child0.getChildCount());

        SpanElement span = (SpanElement) child0.getChild(0);
        assertEquals("", span.getId());
        assertEquals("spanClass", span.getClassName());
        assertEquals(1, span.getChildCount());
        assertEquals(Node.TEXT_NODE, span.getChild(0).getNodeType());
        Text text = span.getChild(0).cast();
        assertEquals("test", text.getData());
        assertEquals("test", span.getInnerText());

        BRElement br = (BRElement) parent1.getChild(1);
        assertEquals("", br.getId());

        DivElement child2 = (DivElement) parent1.getChild(2);
        assertEquals("child2", child2.getId());
        assertEquals(0, child2.getChildCount());
        Style style = child2.getStyle();
        assertEquals("red", style.getColor());
    }

    @Test
    public void parse_with_sub_html_tree_like_tr_arg() {
        String html = "<tr __gwt_header_row=\"0\"><th colspan=\"1\" __gwt_header=\"header-elem_208\" tabindex=\"-1\" role=\"button\" __gwt_column=\"column-elem_207\" class=\"cellTableHeader cellTableFirstColumnHeader cellTableSortableHeader\">N° téléphone</th><th colspan=\"1\" tabindex=\"-1\" role=\"button\" __gwt_column=\"column-elem_209\" class=\"cellTableHeader cellTableSortableHeader cellTableSortedHeaderAscending\"><div style=\"position: relative; zoom: 1; padding-left: 6px; \"><div style=\"position: absolute; top: 50%; line-height: 0px; margin-top: 0px; left: 0px; \"><img style=\"width: 0px; height: 0px; background:url(http://127.0.0.1: 8888/gwt/sortAscending.png) no-repeat 0px 0px; \" onload=\"this.__gwtLastUnhandledEvent=\"load\";\" src=\"http://127.0.0.1:8888/gwt/clear.cache.gif\" border=\"0\"></img></div><div __gwt_header=\"header-elem_210\">Type</div></div></th><th colspan=\"1\" __gwt_header=\"header-elem_212\" tabindex=\"-1\" role=\"button\" __gwt_column=\"column-elem_211\" class=\"cellTableHeader cellTableSortableHeader\">Nom</th><th colspan=\"1\" __gwt_header=\"header-elem_214\" tabindex=\"-1\" role=\"button\" __gwt_column=\"column-elem_213\" class=\"cellTableHeader cellTableSortableHeader\">N°</th><th colspan=\"1\" __gwt_header=\"header-elem_216\" tabindex=\"-1\" role=\"button\" __gwt_column=\"column-elem_215\" class=\"cellTableHeader cellTableSortableHeader\">Résidence</th><th colspan=\"1\" __gwt_header=\"header-elem_218\" tabindex=\"-1\" role=\"button\" __gwt_column=\"column-elem_217\" class=\"cellTableHeader cellTableSortableHeader\">Bât.</th><th colspan=\"1\" __gwt_header=\"header-elem_220\" tabindex=\"-1\" role=\"button\" __gwt_column=\"column-elem_219\" class=\"cellTableHeader cellTableSortableHeader\">Esc.</th><th colspan=\"1\" __gwt_header=\"header-elem_222\" tabindex=\"-1\" role=\"button\" __gwt_column=\"column-elem_221\" class=\"cellTableHeader cellTableSortableHeader\">Etage</th><th colspan=\"1\" __gwt_header=\"header-elem_224\" tabindex=\"-1\" role=\"button\" __gwt_column=\"column-elem_223\" class=\"cellTableHeader cellTableSortableHeader\">Porte</th><th colspan=\"1\" __gwt_header=\"header-elem_226\" __gwt_column=\"column-elem_225\" class=\"cellTableHeader cellTableSortableHeader cellTableLastColumnHeader\">Logo</th></tr>";

        // Act
        NodeList<Node> nodes = GwtHtmlParser.parse(html);
        assertEquals(1, nodes.getLength());
        assertEquals("tr", nodes.getItem(0).getNodeName().toLowerCase());
        assertEquals(10, nodes.getItem(0).getChildCount());
        for (int i = 0; i < nodes.getItem(0).getChildCount(); i++) {
            assertEquals("th", nodes.getItem(0).getChild(i).getNodeName().toLowerCase());
        }
    }
}

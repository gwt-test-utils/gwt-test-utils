package com.googlecode.gwt.test.internal.utils;

import com.google.gwt.dom.client.*;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GwtHtmlParserTest extends GwtTestTest {

    @Test
    public void parse() throws Exception {
        // Given
        String html = "<div id=\"parent0\"></div><div id=\"parent1\"><div id=\"child0\"><span class=\"spanClass\" >test</span></div><BR><DIV id=\"child2\" style=\"color:red; font-style:italic; font-weight:bold; font-family:Arial\"></div>";

        // When
        NodeList<Node> nodes = GwtHtmlParser.parse(html);

        // Then
        assertThat(nodes.getLength()).isEqualTo(2);
        DivElement parent0 = (DivElement) nodes.getItem(0);
        assertThat(parent0.getId()).isEqualTo("parent0");
        assertThat(parent0.getChildCount()).isEqualTo(0);

        DivElement parent1 = (DivElement) nodes.getItem(1);
        assertThat(parent1.getId()).isEqualTo("parent1");
        assertThat(parent1.getChildCount()).isEqualTo(3);

        DivElement child0 = (DivElement) parent1.getChild(0);
        assertThat(child0.getId()).isEqualTo("child0");
        assertThat(child0.getChildCount()).isEqualTo(1);

        SpanElement span = (SpanElement) child0.getChild(0);
        assertThat(span.getId()).isEqualTo("");
        assertThat(span.getClassName()).isEqualTo("spanClass");
        assertThat(span.getChildCount()).isEqualTo(1);
        assertThat(span.getChild(0).getNodeType()).isEqualTo(Node.TEXT_NODE);
        Text text = span.getChild(0).cast();
        assertThat(text.getData()).isEqualTo("test");
        assertThat(span.getInnerText()).isEqualTo("test");

        BRElement br = (BRElement) parent1.getChild(1);
        assertThat(br.getId()).isEqualTo("");

        DivElement child2 = (DivElement) parent1.getChild(2);
        assertThat(child2.getId()).isEqualTo("child2");
        assertThat(child2.getChildCount()).isEqualTo(0);
        Style style = child2.getStyle();
        assertThat(style.getColor()).isEqualTo("red");
    }

    @Test
    public void parse_with_sub_html_tree_like_tr_arg() {
        String html = "<tr __gwt_header_row=\"0\"><th colspan=\"1\" __gwt_header=\"header-elem_208\" tabindex=\"-1\" role=\"button\" __gwt_column=\"column-elem_207\" class=\"cellTableHeader cellTableFirstColumnHeader cellTableSortableHeader\">N° téléphone</th><th colspan=\"1\" tabindex=\"-1\" role=\"button\" __gwt_column=\"column-elem_209\" class=\"cellTableHeader cellTableSortableHeader cellTableSortedHeaderAscending\"><div style=\"position: relative; zoom: 1; padding-left: 6px; \"><div style=\"position: absolute; top: 50%; line-height: 0px; margin-top: 0px; left: 0px; \"><img style=\"width: 0px; height: 0px; background:url(http://127.0.0.1: 8888/gwt/sortAscending.png) no-repeat 0px 0px; \" onload=\"this.__gwtLastUnhandledEvent=\"load\";\" src=\"http://127.0.0.1:8888/gwt/clear.cache.gif\" border=\"0\"></img></div><div __gwt_header=\"header-elem_210\">Type</div></div></th><th colspan=\"1\" __gwt_header=\"header-elem_212\" tabindex=\"-1\" role=\"button\" __gwt_column=\"column-elem_211\" class=\"cellTableHeader cellTableSortableHeader\">Nom</th><th colspan=\"1\" __gwt_header=\"header-elem_214\" tabindex=\"-1\" role=\"button\" __gwt_column=\"column-elem_213\" class=\"cellTableHeader cellTableSortableHeader\">N°</th><th colspan=\"1\" __gwt_header=\"header-elem_216\" tabindex=\"-1\" role=\"button\" __gwt_column=\"column-elem_215\" class=\"cellTableHeader cellTableSortableHeader\">Résidence</th><th colspan=\"1\" __gwt_header=\"header-elem_218\" tabindex=\"-1\" role=\"button\" __gwt_column=\"column-elem_217\" class=\"cellTableHeader cellTableSortableHeader\">Bât.</th><th colspan=\"1\" __gwt_header=\"header-elem_220\" tabindex=\"-1\" role=\"button\" __gwt_column=\"column-elem_219\" class=\"cellTableHeader cellTableSortableHeader\">Esc.</th><th colspan=\"1\" __gwt_header=\"header-elem_222\" tabindex=\"-1\" role=\"button\" __gwt_column=\"column-elem_221\" class=\"cellTableHeader cellTableSortableHeader\">Etage</th><th colspan=\"1\" __gwt_header=\"header-elem_224\" tabindex=\"-1\" role=\"button\" __gwt_column=\"column-elem_223\" class=\"cellTableHeader cellTableSortableHeader\">Porte</th><th colspan=\"1\" __gwt_header=\"header-elem_226\" __gwt_column=\"column-elem_225\" class=\"cellTableHeader cellTableSortableHeader cellTableLastColumnHeader\">Logo</th></tr>";

        // When
        NodeList<Node> nodes = GwtHtmlParser.parse(html);
        assertThat(nodes.getLength()).isEqualTo(1);
        assertThat(nodes.getItem(0).getNodeName().toLowerCase()).isEqualTo("tr");
        assertThat(nodes.getItem(0).getChildCount()).isEqualTo(10);
        for (int i = 0; i < nodes.getItem(0).getChildCount(); i++) {
            assertThat(nodes.getItem(0).getChild(i).getNodeName().toLowerCase()).isEqualTo("th");
        }
    }
}

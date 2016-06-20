package com.googlecode.gwt.test.dom;

import com.google.gwt.dom.client.*;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TableElementTest extends GwtTestTest {

    private TableElement e;

    @Before
    public void beforeTableElementTest() {
        e = Document.get().createTableElement();
    }

    @Test
    public void caption() {
        // Pre Assert
        assertThat(e.getCaption()).isNull();
        assertThat(e.getChildCount()).isEqualTo(0);

        // Given
        e.createTHead();
        e.createTFoot();

        // When
        TableCaptionElement caption = e.createCaption();

        // Then
        assertThat(e.getCaption()).isEqualTo(caption);
        assertThat(caption.getTagName()).isEqualTo("caption");
        assertThat(e.getChildCount()).isEqualTo(3);
        // caption should be inserted at first rank
        assertThat(e.getChild(0)).isEqualTo(caption);

        // When 2
        e.deleteCaption();
        assertThat(e.getCaption()).isNull();
        assertThat(e.getChildCount()).isEqualTo(2);
        assertThat(e.getChild(0).getNodeName()).isEqualTo("thead");
    }

    @Test
    public void completeTest() {
        // When 1
        TableRowElement r0 = Document.get().createTRElement();
        r0.setId("r0");
        e.appendChild(r0);

        e.createTHead();

        // Then 1
        assertThat(e.toString()).isEqualTo("<table><thead></thead><tr id=\"r0\"></tr></table>");

        // When 2
        Element tbody1 = Document.get().createTBodyElement();
        tbody1.setId("tbody1");
        e.appendChild(tbody1);
        Element tbody2 = Document.get().createTBodyElement();
        tbody2.setId("tbody2");
        e.appendChild(tbody2);

        // Then 2
        assertThat(e.toString()).isEqualTo("<table><thead></thead><tr id=\"r0\"></tr><tbody id=\"tbody1\"></tbody><tbody id=\"tbody2\"></tbody></table>");

        // When 3
        TableRowElement r1 = e.insertRow(0);
        r1.setId("r1");
        TableRowElement r2 = e.insertRow(0);
        r2.setId("r2");

        // Then 3
        assertThat(e.toString()).isEqualTo("<table><thead></thead><tr id=\"r2\"></tr><tr id=\"r1\"></tr><tr id=\"r0\"></tr><tbody id=\"tbody1\"></tbody><tbody id=\"tbody2\"></tbody></table>");

        // When 4
        TableRowElement trBody1 = Document.get().createTRElement();
        trBody1.setId("trBody1");
        tbody1.appendChild(trBody1);

        // Then 4
        assertThat(e.toString()).isEqualTo("<table><thead></thead><tr id=\"r2\"></tr><tr id=\"r1\"></tr><tr id=\"r0\"></tr><tbody id=\"tbody1\"><tr id=\"trBody1\"></tr></tbody><tbody id=\"tbody2\"></tbody></table>");

        // When 5
        TableSectionElement tfoot = e.createTFoot();
        tfoot.setId("tfoot");

        // Then 5
        assertThat(e.toString()).isEqualTo("<table><thead></thead><tfoot id=\"tfoot\"></tfoot><tr id=\"r2\"></tr><tr id=\"r1\"></tr><tr id=\"r0\"></tr><tbody id=\"tbody1\"><tr id=\"trBody1\"></tr></tbody><tbody id=\"tbody2\"></tbody></table>");

        // When 6
        TableRowElement lastRow = e.insertRow(4);
        lastRow.setId("lastRow");

        // Then 6
        assertThat(e.toString()).isEqualTo("<table><thead></thead><tfoot id=\"tfoot\"></tfoot><tr id=\"r2\"></tr><tr id=\"r1\"></tr><tr id=\"r0\"></tr><tbody id=\"tbody1\"><tr id=\"trBody1\"></tr><tr id=\"lastRow\"></tr></tbody><tbody id=\"tbody2\"></tbody></table>");

        assertThat(e.getChildCount()).isEqualTo(7);
        assertThat(e.getRows().getLength()).isEqualTo(5);
    }

    @Test
    public void getTBodies() {
        // Pre Assert
        Element tbody1 = Document.get().createTBodyElement();
        Element tbody2 = Document.get().createTBodyElement();
        e.appendChild(tbody1);
        e.appendChild(tbody2);
        assertThat(e.getChildCount()).isEqualTo(2);

        // When
        NodeList<TableSectionElement> tbodies = e.getTBodies();

        // Then
        assertThat(tbodies.getLength()).isEqualTo(2);
        assertThat(tbodies.getItem(0)).isEqualTo(tbody1);
        assertThat(tbodies.getItem(1)).isEqualTo(tbody2);
    }

    @Test
    public void insertRowEmptyTable() {
        // When
        TableRowElement r0 = e.insertRow(0);

        // Then
        assertThat(e.getChildCount()).isEqualTo(1);
        assertThat(e.getChild(0).getNodeName()).isEqualTo("tbody");
        assertThat(e.getChild(0).getChild(0)).isEqualTo(r0);
    }

    @Test
    public void rows() {
        // Given
        e.createTHead();
        e.createTFoot();

        TableRowElement tr1 = Document.get().createTRElement();
        TableCellElement td1 = Document.get().createTDElement();
        td1.setInnerText("1");
        tr1.appendChild(td1);
        TableCellElement td2 = Document.get().createTDElement();
        td2.setInnerText("2");
        tr1.appendChild(td2);

        TableRowElement tr2 = Document.get().createTRElement();
        TableCellElement td3 = Document.get().createTDElement();
        td3.setInnerText("3");
        tr2.appendChild(td3);
        TableCellElement td4 = Document.get().createTDElement();
        td4.setInnerText("4");
        tr2.appendChild(td4);

        // When
        e.appendChild(tr1);
        e.appendChild(tr2);

        // Then
        assertThat(e.getRows().getLength()).isEqualTo(2);
        assertThat(e.getRows().getItem(0)).isEqualTo(tr1);
        assertThat(e.getChildCount()).isEqualTo(4);
        assertThat(e.toString()).isEqualTo("<table><thead></thead><tfoot></tfoot><tr><td>1</td><td>2</td></tr><tr><td>3</td><td>4</td></tr></table>");

        // When 2
        e.deleteRow(0);

        // Then 2
        assertThat(e.getRows().getLength()).isEqualTo(1);
        assertThat(e.getRows().getItem(0)).isEqualTo(tr2);
        assertThat(e.getChildCount()).isEqualTo(3);
        assertThat(e.toString()).isEqualTo("<table><thead></thead><tfoot></tfoot><tr><td>3</td><td>4</td></tr></table>");
    }

    @Test
    public void tfoot() {
        // Pre Assert
        assertThat(e.getTFoot()).isNull();
        assertThat(e.getChildCount()).isEqualTo(0);

        // When
        TableSectionElement tfoot = e.createTFoot();

        // Then
        assertThat(e.getTFoot()).isEqualTo(tfoot);
        assertThat(tfoot.getTagName()).isEqualTo("tfoot");
        assertThat(e.getChildCount()).isEqualTo(1);
        assertThat(e.getChild(0)).isEqualTo(tfoot);

        // When 2
        e.deleteTFoot();

        // Then2
        assertThat(e.getTFoot()).isNull();
        assertThat(e.getChildCount()).isEqualTo(0);

        // When 3
        TableSectionElement newTFoot = Document.get().createTFootElement();
        newTFoot.setInnerText("new");
        e.setTFoot(newTFoot);

        // Then 3
        assertThat(e.getTFoot()).isEqualTo(newTFoot);
        assertThat(newTFoot.getTagName()).isEqualTo("tfoot");
        assertThat(e.getChildCount()).isEqualTo(1);
        assertThat(e.getChild(0)).isEqualTo(newTFoot);
        assertThat(e.toString()).isEqualTo("<table><tfoot>new</tfoot></table>");

        // When 4
        e.setTFoot(null);

        // Then 4
        assertThat(e.getTFoot()).isNull();
        assertThat(e.getChildCount()).isEqualTo(0);
    }

    @Test
    public void thead() {
        // Pre Assert
        assertThat(e.getTHead()).isNull();
        assertThat(e.getChildCount()).isEqualTo(0);

        // When
        TableSectionElement thead = e.createTHead();

        // Then
        assertThat(e.getTHead()).isEqualTo(thead);
        assertThat(thead.getTagName()).isEqualTo("thead");
        assertThat(e.getChildCount()).isEqualTo(1);
        assertThat(e.getChild(0)).isEqualTo(thead);

        // When 2
        e.deleteTHead();

        // Then 2
        assertThat(e.getTHead()).isNull();
        assertThat(e.getChildCount()).isEqualTo(0);

        // When 3
        TableSectionElement newTHead = Document.get().createTHeadElement();
        newTHead.setInnerText("new");
        e.setTHead(newTHead);

        // Then 3
        assertThat(e.getTHead()).isEqualTo(newTHead);
        assertThat(newTHead.getTagName()).isEqualTo("thead");
        assertThat(e.getChildCount()).isEqualTo(1);
        assertThat(e.getChild(0)).isEqualTo(newTHead);
        assertThat(e.toString()).isEqualTo("<table><thead>new</thead></table>");

        // When 4
        e.setTHead(null);

        // Then 4
        assertThat(e.getTHead()).isNull();
        assertThat(e.getChildCount()).isEqualTo(0);
    }

}

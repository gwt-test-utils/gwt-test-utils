package com.googlecode.gwt.test.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.googlecode.gwt.test.GwtTestTest;

public class TableElementTest extends GwtTestTest {

   private TableElement e;

   @Before
   public void beforeTableElementTest() {
      e = Document.get().createTableElement();
   }

   @Test
   public void caption() {
      // Pre Assert
      assertNull(e.getCaption());
      assertEquals(0, e.getChildCount());

      // Arrange
      e.createTHead();
      e.createTFoot();

      // Act
      TableCaptionElement caption = e.createCaption();

      // Assert
      assertEquals(caption, e.getCaption());
      assertEquals("caption", caption.getTagName());
      assertEquals(3, e.getChildCount());
      // caption should be inserted at first rank
      assertEquals(caption, e.getChild(0));

      // Act 2
      e.deleteCaption();
      assertNull(e.getCaption());
      assertEquals(2, e.getChildCount());
      assertEquals("thead", e.getChild(0).getNodeName());
   }

   @Test
   public void completeTest() {
      // Act 1
      TableRowElement r0 = Document.get().createTRElement();
      r0.setId("r0");
      e.appendChild(r0);

      e.createTHead();

      // Assert 1
      assertEquals("<table><thead></thead><tr id=\"r0\"></tr></table>", e.toString());

      // Act 2
      Element tbody1 = Document.get().createTBodyElement();
      tbody1.setId("tbody1");
      e.appendChild(tbody1);
      Element tbody2 = Document.get().createTBodyElement();
      tbody2.setId("tbody2");
      e.appendChild(tbody2);

      // Assert 2
      assertEquals(
               "<table><thead></thead><tr id=\"r0\"></tr><tbody id=\"tbody1\"></tbody><tbody id=\"tbody2\"></tbody></table>",
               e.toString());

      // Act 3
      TableRowElement r1 = e.insertRow(0);
      r1.setId("r1");
      TableRowElement r2 = e.insertRow(0);
      r2.setId("r2");

      // Assert 3
      assertEquals(
               "<table><thead></thead><tr id=\"r2\"></tr><tr id=\"r1\"></tr><tr id=\"r0\"></tr><tbody id=\"tbody1\"></tbody><tbody id=\"tbody2\"></tbody></table>",
               e.toString());

      // Act 4
      TableRowElement trBody1 = Document.get().createTRElement();
      trBody1.setId("trBody1");
      tbody1.appendChild(trBody1);

      // Assert 4
      assertEquals(
               "<table><thead></thead><tr id=\"r2\"></tr><tr id=\"r1\"></tr><tr id=\"r0\"></tr><tbody id=\"tbody1\"><tr id=\"trBody1\"></tr></tbody><tbody id=\"tbody2\"></tbody></table>",
               e.toString());

      // Act 5
      TableSectionElement tfoot = e.createTFoot();
      tfoot.setId("tfoot");

      // Assert 5
      assertEquals(
               "<table><thead></thead><tfoot id=\"tfoot\"></tfoot><tr id=\"r2\"></tr><tr id=\"r1\"></tr><tr id=\"r0\"></tr><tbody id=\"tbody1\"><tr id=\"trBody1\"></tr></tbody><tbody id=\"tbody2\"></tbody></table>",
               e.toString());

      // Act 6
      TableRowElement lastRow = e.insertRow(4);
      lastRow.setId("lastRow");

      // Assert 6
      assertEquals(
               "<table><thead></thead><tfoot id=\"tfoot\"></tfoot><tr id=\"r2\"></tr><tr id=\"r1\"></tr><tr id=\"r0\"></tr><tbody id=\"tbody1\"><tr id=\"trBody1\"></tr><tr id=\"lastRow\"></tr></tbody><tbody id=\"tbody2\"></tbody></table>",
               e.toString());

      assertEquals(7, e.getChildCount());
      assertEquals(5, e.getRows().getLength());
   }

   @Test
   public void getTBodies() {
      // Pre Assert
      Element tbody1 = Document.get().createTBodyElement();
      Element tbody2 = Document.get().createTBodyElement();
      e.appendChild(tbody1);
      e.appendChild(tbody2);
      assertEquals(2, e.getChildCount());

      // Act
      NodeList<TableSectionElement> tbodies = e.getTBodies();

      // Assert
      assertEquals(2, tbodies.getLength());
      assertEquals(tbody1, tbodies.getItem(0));
      assertEquals(tbody2, tbodies.getItem(1));
   }

   @Test
   public void insertRowEmptyTable() {
      // Act
      TableRowElement r0 = e.insertRow(0);

      // Assert
      assertEquals(1, e.getChildCount());
      assertEquals("tbody", e.getChild(0).getNodeName());
      assertEquals(r0, e.getChild(0).getChild(0));
   }

   @Test
   public void rows() {
      // Arrange
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

      // Act
      e.appendChild(tr1);
      e.appendChild(tr2);

      // Assert
      assertEquals(2, e.getRows().getLength());
      assertEquals(tr1, e.getRows().getItem(0));
      assertEquals(4, e.getChildCount());
      assertEquals(
               "<table><thead></thead><tfoot></tfoot><tr><td>1</td><td>2</td></tr><tr><td>3</td><td>4</td></tr></table>",
               e.toString());

      // Act 2
      e.deleteRow(0);

      // Assert 2
      assertEquals(1, e.getRows().getLength());
      assertEquals(tr2, e.getRows().getItem(0));
      assertEquals(3, e.getChildCount());
      assertEquals("<table><thead></thead><tfoot></tfoot><tr><td>3</td><td>4</td></tr></table>",
               e.toString());
   }

   @Test
   public void tfoot() {
      // Pre Assert
      assertNull(e.getTFoot());
      assertEquals(0, e.getChildCount());

      // Act
      TableSectionElement tfoot = e.createTFoot();

      // Assert
      assertEquals(tfoot, e.getTFoot());
      assertEquals("tfoot", tfoot.getTagName());
      assertEquals(1, e.getChildCount());
      assertEquals(tfoot, e.getChild(0));

      // Act 2
      e.deleteTFoot();

      // Assert2
      assertNull(e.getTFoot());
      assertEquals(0, e.getChildCount());

      // Act 3
      TableSectionElement newTFoot = Document.get().createTFootElement();
      newTFoot.setInnerText("new");
      e.setTFoot(newTFoot);

      // Assert 3
      assertEquals(newTFoot, e.getTFoot());
      assertEquals("tfoot", newTFoot.getTagName());
      assertEquals(1, e.getChildCount());
      assertEquals(newTFoot, e.getChild(0));
      assertEquals("<table><tfoot>new</tfoot></table>", e.toString());

      // Act 4
      e.setTFoot(null);

      // Assert 4
      assertNull(e.getTFoot());
      assertEquals(0, e.getChildCount());
   }

   @Test
   public void thead() {
      // Pre Assert
      assertNull(e.getTHead());
      assertEquals(0, e.getChildCount());

      // Act
      TableSectionElement thead = e.createTHead();

      // Assert
      assertEquals(thead, e.getTHead());
      assertEquals("thead", thead.getTagName());
      assertEquals(1, e.getChildCount());
      assertEquals(thead, e.getChild(0));

      // Act 2
      e.deleteTHead();

      // Assert 2
      assertNull(e.getTHead());
      assertEquals(0, e.getChildCount());

      // Act 3
      TableSectionElement newTHead = Document.get().createTHeadElement();
      newTHead.setInnerText("new");
      e.setTHead(newTHead);

      // Assert 3
      assertEquals(newTHead, e.getTHead());
      assertEquals("thead", newTHead.getTagName());
      assertEquals(1, e.getChildCount());
      assertEquals(newTHead, e.getChild(0));
      assertEquals("<table><thead>new</thead></table>", e.toString());

      // Act 4
      e.setTHead(null);

      // Assert 4
      assertNull(e.getTHead());
      assertEquals(0, e.getChildCount());
   }

}

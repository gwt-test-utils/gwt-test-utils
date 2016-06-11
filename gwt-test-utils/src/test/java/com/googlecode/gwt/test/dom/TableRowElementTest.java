package com.googlecode.gwt.test.dom;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class TableRowElementTest extends GwtTestTest {

    private TableRowElement tr;

    @Before
    public void before() {
        tr = Document.get().createTRElement();
    }

    @Test
    public void deleteCell() {
        // Arrange
        TableCellElement td0 = Document.get().createTDElement();
        tr.appendChild(td0);
        TableCellElement td1 = Document.get().createTDElement();
        tr.appendChild(td1);

        // Act
        tr.deleteCell(0);

        // Assert
        assertThat(tr.getChildCount()).isEqualTo(1);
        assertThat(tr.getChild(0)).isEqualTo(td1);
    }

    @Test
    public void insertCell() {
        // Arrange
        TableCellElement td0 = Document.get().createTDElement();
        tr.appendChild(td0);
        TableCellElement td1 = Document.get().createTDElement();
        tr.appendChild(td1);

        // Act
        TableCellElement insert = tr.insertCell(1);

        // Assert
        assertThat(tr.getChildCount()).isEqualTo(3);
        assertThat(tr.getChild(0)).isEqualTo(td0);
        assertThat(tr.getChild(1)).isEqualTo(insert);
        assertThat(tr.getChild(2)).isEqualTo(td1);
    }

}

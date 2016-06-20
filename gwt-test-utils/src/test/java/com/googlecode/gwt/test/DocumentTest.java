package com.googlecode.gwt.test;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DocumentTest extends GwtTestTest {

    @Test
    public void getDocumentElement() {
        // When
        Element documentElement = Document.get().getDocumentElement();

        // Then
        assertThat(documentElement.getNodeName()).isEqualTo("HTML");
    }

    @Test
    public void getNodeName() {
        // When
        String nodeName = Document.get().getNodeName();

        // Then
        assertThat(nodeName).isEqualTo("#document");
    }

    @Test
    public void title() {
        // When
        Document.get().setTitle("my title");

        // Then
        assertThat(Document.get().getTitle()).isEqualTo("my title");
    }

}

package com.googlecode.gwt.test.utils;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GwtDomUtilsTest extends GwtTestTest {

    @Test
    public void setClientHeight() {
        // Given
        Element e = Document.get().createAnchorElement();

        // When
        GwtDomUtils.setClientHeight(e, 4);

        // Then
        assertThat(e.getClientHeight()).isEqualTo(4);
    }

    @Test
    public void setClientWidth() {
        // Given
        Element e = Document.get().createAnchorElement();

        // When
        GwtDomUtils.setClientWidth(e, 4);

        // Then
        assertThat(e.getClientWidth()).isEqualTo(4);
    }

}

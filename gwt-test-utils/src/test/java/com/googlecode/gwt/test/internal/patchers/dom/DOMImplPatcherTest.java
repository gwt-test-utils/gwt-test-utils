package com.googlecode.gwt.test.internal.patchers.dom;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DOMImplPatcherTest extends GwtTestTest {

    @Test
    public void check_isOrHasChild_on_deep_hierarchy() {
        // Given
        Element grandParent = DOM.createDiv();
        Element parent = DOM.createDiv();

        // Pre-Assert
        assertThat(grandParent.isOrHasChild(parent)).isFalse();

        // When
        grandParent.appendChild(parent);

        // Then
        assertThat(grandParent.isOrHasChild(parent));

        // When
        Element child = DOM.createDiv();

        // Pre-Assert
        assertThat(parent.isOrHasChild(child)).isFalse();

        // When
        parent.appendChild(child);

        // Then
        assertThat(grandParent.isOrHasChild(child));
    }

}
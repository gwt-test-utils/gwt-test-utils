package com.googlecode.gwt.test;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.impl.FocusImpl;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FocusImplTest extends GwtTestTest {

    private final FocusImpl focusImpl = FocusImpl.getFocusImplForWidget();
    private Element e;

    @Before
    public void beforeFocusImplTest() {
        e = Document.get().createAnchorElement().cast();
    }

    @Test
    public void blur() {
        // just check blur(element) does not throw any exception
        focusImpl.blur(e);
    }

    @Test
    public void createFocusable() {
        // When
        Element elem = focusImpl.createFocusable();

        // Then
        assertThat(elem.getTagName()).isEqualTo("div");
    }

    @Test
    public void focus() {
        // just check focus(element) does not throw any exception
        focusImpl.focus(e);
    }

    @Test
    public void tabIndex() {
        // When
        focusImpl.setTabIndex(e, 3);

        // Then
        assertThat(focusImpl.getTabIndex(e)).isEqualTo(3);
    }

}

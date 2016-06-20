package com.googlecode.gwt.test.uibinder;

import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UiBinderWithListBoxTest extends GwtTestTest {

    @Test
    public void content() {
        // When
        UiBinderWithListBox uibinder = new UiBinderWithListBox();

        // Then
        assertThat(uibinder.listBox.getItemCount()).isEqualTo(2);
        assertThat(uibinder.listBox.getItemText(0)).isEqualTo("first");
        assertThat(uibinder.listBox.getValue(0)).isEqualTo("");
        assertThat(uibinder.listBox.getItemText(1)).isEqualTo("second");
        assertThat(uibinder.listBox.getValue(1)).isEqualTo("2");
    }

}

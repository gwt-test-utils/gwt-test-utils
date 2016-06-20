package com.googlecode.gwt.test.deferred;

import com.google.gwt.core.client.GWT;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.deferred.ReplaceWithDefault.ReplaceWithIE;
import com.googlecode.gwt.test.deferred.ReplaceWithDefault.ReplaceWithMozilla;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomDeferredBindingTest extends GwtTestTest {

    @Test
    public void create_generateWith() {
        // When
        IGenerateWith generated = GWT.create(IGenerateWith.class);

        // Then
        assertThat(generated.getClass().getName()).isEqualTo("com.slazzer.MyGeneratedClass");
        assertThat(generated.getMessage()).isEqualTo("generated with MyGenerator class");
    }

    @Test
    public void create_replaceWith_Default() {
        // When
        IReplaceWith replaceWith = GWT.create(IReplaceWith.class);

        // Then
        assertThat(replaceWith instanceof ReplaceWithDefault).isTrue();
    }

    @Test
    public void create_replaceWith_gecko() {
        // Given
        addClientProperty("user.agent", "gecko");

        // When
        IReplaceWith replaceWith = GWT.create(IReplaceWith.class);

        // Then
        assertThat(replaceWith instanceof ReplaceWithMozilla).isTrue();
    }

    @Test
    public void create_replaceWith_gecko1_8() {
        // Given
        addClientProperty("user.agent", "gecko1_8");

        // When
        IReplaceWith replaceWith = GWT.create(IReplaceWith.class);

        // Then
        assertThat(replaceWith instanceof ReplaceWithMozilla).isTrue();
    }

    @Test
    public void create_replaceWith_ie6() {
        // Given
        addClientProperty("user.agent", "ie6");

        // When
        IReplaceWith replaceWith = GWT.create(IReplaceWith.class);

        // Then
        assertThat(replaceWith instanceof ReplaceWithIE).isTrue();
    }

    @Test
    public void create_replaceWith_ie8() {
        // Given
        addClientProperty("user.agent", "ie8");

        // When
        IReplaceWith replaceWith = GWT.create(IReplaceWith.class);

        // Then
        assertThat(replaceWith instanceof ReplaceWithDefault).isTrue();
    }
}

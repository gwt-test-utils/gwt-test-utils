package com.googlecode.gwt.test.deferred;

import com.google.gwt.core.client.GWT;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.deferred.ReplaceWithDefault.ReplaceWithIE;
import com.googlecode.gwt.test.deferred.ReplaceWithDefault.ReplaceWithMozilla;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class CustomDeferredBindingTest extends GwtTestTest {

    @Test
    public void create_generateWith() {
        // Act
        IGenerateWith generated = GWT.create(IGenerateWith.class);

        // Assert
        assertThat(generated.getClass().getName()).isEqualTo("com.slazzer.MyGeneratedClass");
        assertThat(generated.getMessage()).isEqualTo("generated with MyGenerator class");
    }

    @Test
    public void create_replaceWith_Default() {
        // Act
        IReplaceWith replaceWith = GWT.create(IReplaceWith.class);

        // Assert
        assertTrue(replaceWith instanceof ReplaceWithDefault);
    }

    @Test
    public void create_replaceWith_gecko() {
        // Arrange
        addClientProperty("user.agent", "gecko");

        // Act
        IReplaceWith replaceWith = GWT.create(IReplaceWith.class);

        // Assert
        assertTrue(replaceWith instanceof ReplaceWithMozilla);
    }

    @Test
    public void create_replaceWith_gecko1_8() {
        // Arrange
        addClientProperty("user.agent", "gecko1_8");

        // Act
        IReplaceWith replaceWith = GWT.create(IReplaceWith.class);

        // Assert
        assertTrue(replaceWith instanceof ReplaceWithMozilla);
    }

    @Test
    public void create_replaceWith_ie6() {
        // Arrange
        addClientProperty("user.agent", "ie6");

        // Act
        IReplaceWith replaceWith = GWT.create(IReplaceWith.class);

        // Assert
        assertTrue(replaceWith instanceof ReplaceWithIE);
    }

    @Test
    public void create_replaceWith_ie8() {
        // Arrange
        addClientProperty("user.agent", "ie8");

        // Act
        IReplaceWith replaceWith = GWT.create(IReplaceWith.class);

        // Assert
        assertTrue(replaceWith instanceof ReplaceWithDefault);
    }
}

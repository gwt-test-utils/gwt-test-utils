package com.googlecode.gwt.test.uibinder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.uibinder.UiConstructorWidget.Type;

public class UiBinderWithUiConstructorTest extends GwtTestTest {

  @Test
  public void instanciation() throws Exception {
    // Act
    UiBinderWithUiConstructor w = new UiBinderWithUiConstructor();

    // Assert
    assertEquals(5, w.myWidget.size);
    assertEquals(Type.T1, w.myWidget.type);
  }

}

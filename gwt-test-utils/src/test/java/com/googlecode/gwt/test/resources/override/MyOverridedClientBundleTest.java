package com.googlecode.gwt.test.resources.override;

import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MyOverridedClientBundleTest extends GwtTestTest {

    @Test
    public void testDataResource_NoOverride() {
        // Arrange
        DataResource testDataResource = MyOverridedClientBundle.INSTANCE.dataResource();

        // Act
        String name = testDataResource.getName();
        String url = testDataResource.getSafeUri().asString();
        String toString = testDataResource.toString();

        // Assert
        assertEquals("dataResource", name);
        assertEquals("http://127.0.0.1:8888/gwt_test_utils_module/textResourceXml.xml", url);
        assertEquals(
                "com.googlecode.gwt.test.internal.resources.DataResourceCallback generated for 'com.googlecode.gwt.test.resources.override.MyOverridedClientBundle.dataResource()'",
                toString);
        assertEquals(testDataResource, MyOverridedClientBundle.INSTANCE.dataResource());
    }

    @Test
    public void testImageResource_OverrideWithAnnotation() {
        // Arrange
        ImageResource testImageResource = MyOverridedClientBundle.INSTANCE.imageResource();

        // Act
        String name = testImageResource.getName();
        String url = testImageResource.getSafeUri().asString();
        int heigh = testImageResource.getHeight();
        int left = testImageResource.getLeft();
        int width = testImageResource.getWidth();
        int top = testImageResource.getTop();

        // Assert
        assertEquals("imageResource", name);
        assertEquals("http://127.0.0.1:8888/gwt_test_utils_module/override_testImageResource.gif",
                url);
        assertEquals(0, heigh);
        assertEquals(0, left);
        assertEquals(0, width);
        assertEquals(0, top);
        assertEquals(testImageResource, MyOverridedClientBundle.INSTANCE.imageResource());
    }

    @Test
    public void textResourceTxt_OverrideWithoutAnnotation() {
        // Arrange
        TextResource textResource = MyOverridedClientBundle.INSTANCE.textResourceTxt();
        String expectedText = "Overrided text resource !";

        // Act
        String name = textResource.getName();
        String text = textResource.getText();

        // Assert
        assertEquals("textResourceTxt", name);
        assertEquals(expectedText, text);
        assertEquals(textResource, MyOverridedClientBundle.INSTANCE.textResourceTxt());
    }

}

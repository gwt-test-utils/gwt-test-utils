package com.googlecode.gwt.test.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.Tree.Resources;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientBundleTest extends GwtTestTest {

    @Test
    public void cssResource() {
        // Arrange
        MyCssResource cssResource = MyClientBundle.INSTANCE.cssResource();

        // Act
        String name = cssResource.getName();
        String testStyle = cssResource.testStyle();
        String testStyleWithHover = cssResource.testStyleWithHover();
        String testStyleOnSpecificElement = cssResource.testStyleOnSpecificElement();
        String testStyleOnSpecificStyle = cssResource.testStyleOnSpecificStyle();
        String constantValue = cssResource.testConstant();
        String toString = cssResource.toString();

        // Assert
        assertEquals("cssResource", name);
        assertEquals("testStyle", testStyle);
        assertEquals("testStyleWithHover", testStyleWithHover);
        assertEquals("testStyleOnSpecificElement", testStyleOnSpecificElement);
        assertEquals("testStyleOnSpecificStyle", testStyleOnSpecificStyle);
        assertEquals("constant-value", constantValue);
        assertEquals(
                "com.googlecode.gwt.test.internal.resources.CssResourceCallback generated for 'com.googlecode.gwt.test.resources.MyClientBundle.cssResource()'",
                toString);
        assertEquals(cssResource, MyClientBundle.INSTANCE.cssResource());
    }

    @Test
    public void cssResourceEnsureInjected() {
        // Arrange
        MyCssResource testCssResource = MyClientBundle.INSTANCE.cssResource();

        // Act & Assert
        assertTrue(testCssResource.ensureInjected());
        assertFalse(testCssResource.ensureInjected());
    }

    @Test
    public void dataResource() {
        // Arrange
        DataResource testDataResource = MyClientBundle.INSTANCE.dataResource();

        // Act
        String name = testDataResource.getName();
        String url = testDataResource.getSafeUri().asString();
        String toString = testDataResource.toString();

        // Assert
        assertEquals("dataResource", name);
        assertEquals("http://127.0.0.1:8888/gwt_test_utils_module/textResourceXml.xml", url);
        assertEquals(
                "com.googlecode.gwt.test.internal.resources.DataResourceCallback generated for 'com.googlecode.gwt.test.resources.MyClientBundle.dataResource()'",
                toString);
        assertEquals(testDataResource, MyClientBundle.INSTANCE.dataResource());
    }

    @Test
    public void imageResource() {
        // Arrange
        ImageResource testImageResource = MyClientBundle.INSTANCE.imageResource();

        // Act
        String name = testImageResource.getName();
        String url = testImageResource.getSafeUri().asString();
        int heigh = testImageResource.getHeight();
        int left = testImageResource.getLeft();
        int width = testImageResource.getWidth();
        int top = testImageResource.getTop();
        String toString = testImageResource.toString();

        // Assert
        assertEquals("imageResource", name);
        assertEquals("http://127.0.0.1:8888/gwt_test_utils_module/imageResource.gif", url);
        assertEquals(0, heigh);
        assertEquals(0, left);
        assertEquals(0, width);
        assertEquals(0, top);
        assertEquals(
                "com.googlecode.gwt.test.internal.resources.ImageResourceCallback generated for 'com.googlecode.gwt.test.resources.MyClientBundle.imageResource()'",
                toString);
        assertEquals(testImageResource, MyClientBundle.INSTANCE.imageResource());
    }

    @Test
    public void imageResource_FromGwtAPI() {
        // Arrange
        Resources treeResources = GWT.create(Resources.class);

        // Act
        String name = treeResources.treeOpen().getName();
        String url = treeResources.treeLeaf().getSafeUri().asString();

        // Assert
        assertEquals("treeOpen", name);
        assertEquals("http://127.0.0.1:8888/gwt_test_utils_module/treeLeaf.gif", url);
    }

    @Test
    public void imageResource_ShouldThrowExceptionWhenMultipleMatchingResourceFile() {
        // Arrange
        String expectedMessage = "Too many ImageResource files found for method 'com.googlecode.gwt.test.resources.MyClientBundle.doubleShouldThrowException()'";
        try {
            // Act
            MyClientBundle.INSTANCE.doubleShouldThrowException();
            fail("An exception should have been thrown since there are multiple matching file for the tested ClientBundle method");
        } catch (Exception e) {
            // Assert
            assertEquals(expectedMessage, e.getMessage());
        }

    }

    @Test
    public void multipleFileResource() {
        // Arrange
        MultipleFileCssResource multipleFileCssResource = MyClientBundle.INSTANCE.multipleFileCssResource();

        // Act
        String name = multipleFileCssResource.getName();
        String testStyle = multipleFileCssResource.testStyle();
        String testStyleWithHover = multipleFileCssResource.testStyleWithHover();
        String testStyleOnSpecificElement = multipleFileCssResource.testStyleOnSpecificElement();
        String testStyleOnSpecificStyle = multipleFileCssResource.testStyleOnSpecificStyle();
        String addedStyle = multipleFileCssResource.addedStyle();
        String constantValue = multipleFileCssResource.testConstant();
        String addedConstantValue = multipleFileCssResource.addedConstant();
        String toString = multipleFileCssResource.toString();

        // Assert
        assertEquals("multipleFileCssResource", name);
        assertEquals("testStyle", testStyle);
        assertEquals("testStyleWithHover", testStyleWithHover);
        assertEquals("testStyleOnSpecificElement", testStyleOnSpecificElement);
        assertEquals("testStyleOnSpecificStyle", testStyleOnSpecificStyle);
        assertEquals("addedStyle", addedStyle);
        assertEquals("constant-value", constantValue);
        assertEquals("added-constant-value", addedConstantValue);
        assertEquals(
                "com.googlecode.gwt.test.internal.resources.CssResourceCallback generated for 'com.googlecode.gwt.test.resources.MyClientBundle.multipleFileCssResource()'",
                toString);
        assertEquals(multipleFileCssResource, MyClientBundle.INSTANCE.multipleFileCssResource());
    }

    @Test
    public void rootClasspathImg() {
        // Arrange
        ImageResource rootClasspathImg = MyClientBundle.INSTANCE.rootClasspathImg();

        // Act
        String name = rootClasspathImg.getName();
        String url = rootClasspathImg.getSafeUri().asString();
        int heigh = rootClasspathImg.getHeight();
        int left = rootClasspathImg.getLeft();
        int width = rootClasspathImg.getWidth();
        int top = rootClasspathImg.getTop();
        String toString = rootClasspathImg.toString();

        // Assert
        assertEquals("rootClasspathImg", name);
        assertEquals("http://127.0.0.1:8888/gwt_test_utils_module/root-classpath-img.png", url);
        assertEquals(0, heigh);
        assertEquals(0, left);
        assertEquals(0, width);
        assertEquals(0, top);
        assertEquals(
                "com.googlecode.gwt.test.internal.resources.ImageResourceCallback generated for 'com.googlecode.gwt.test.resources.MyClientBundle.rootClasspathImg()'",
                toString);
        assertEquals(rootClasspathImg, MyClientBundle.INSTANCE.rootClasspathImg());
    }

    @Test
    public void textResource_FromGwtAPI() {
        // Arrange
        com.google.gwt.user.client.impl.WindowImplIE.Resources treeResources = GWT.create(com.google.gwt.user.client.impl.WindowImplIE.Resources.class);

        // Act
        String name = treeResources.initWindowCloseHandler().getName();
        String text = treeResources.initWindowCloseHandler().getText();

        // Assert
        assertEquals("initWindowCloseHandler", name);
        assertTrue(text.startsWith("function __gwt_initWindowCloseHandler(beforeunload, unload) {"));
    }

    @Test
    public void textResource_Txt() {
        // Arrange
        TextResource textResource = MyClientBundle.INSTANCE.textResourceTxt();
        String expectedText = "Hello gwt-test-utils !\r\nThis is a test with a simple text file";

        // Act
        String name = textResource.getName();
        String text = textResource.getText();
        String toString = textResource.toString();

        // Assert
        assertEquals("textResourceTxt", name);
        assertEquals(expectedText, text);
        assertEquals(
                "com.googlecode.gwt.test.internal.resources.TextResourceCallback generated for 'com.googlecode.gwt.test.resources.MyClientBundle.textResourceTxt()'",
                toString);
        assertEquals(textResource, MyClientBundle.INSTANCE.textResourceTxt());
    }

    @Test
    public void textResource_Xml() {
        // Arrange
        TextResource textResource = MyClientBundle.INSTANCE.textResourceXml();
        String expectedText = "<gwt-test-utils>\r\n    <test>this is a test</test>\r\n</gwt-test-utils>";

        // Act
        String name = textResource.getName();
        String text = textResource.getText();
        String toString = textResource.toString();

        // Assert
        assertEquals("textResourceXml", name);
        assertEquals(expectedText, text);
        assertEquals(
                "com.googlecode.gwt.test.internal.resources.TextResourceCallback generated for 'com.googlecode.gwt.test.resources.MyClientBundle.textResourceXml()'",
                toString);
        assertEquals(textResource, MyClientBundle.INSTANCE.textResourceXml());
    }

}

package com.googlecode.gwt.test.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.Tree.Resources;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class ClientBundleTest extends GwtTestTest {

    @Test
    public void cssResource() {
        // Given
        MyCssResource cssResource = MyClientBundle.INSTANCE.cssResource();

        // When
        String name = cssResource.getName();
        String testStyle = cssResource.testStyle();
        String testStyleWithHover = cssResource.testStyleWithHover();
        String testStyleOnSpecificElement = cssResource.testStyleOnSpecificElement();
        String testStyleOnSpecificStyle = cssResource.testStyleOnSpecificStyle();
        String constantValue = cssResource.testConstant();
        String toString = cssResource.toString();

        // Then
        assertThat(name).isEqualTo("cssResource");
        assertThat(testStyle).isEqualTo("testStyle");
        assertThat(testStyleWithHover).isEqualTo("testStyleWithHover");
        assertThat(testStyleOnSpecificElement).isEqualTo("testStyleOnSpecificElement");
        assertThat(testStyleOnSpecificStyle).isEqualTo("testStyleOnSpecificStyle");
        assertThat(constantValue).isEqualTo("constant-value");
        assertThat(toString).isEqualTo("com.googlecode.gwt.test.internal.resources.CssResourceCallback generated for 'com.googlecode.gwt.test.resources.MyClientBundle.cssResource()'");
        assertThat(MyClientBundle.INSTANCE.cssResource()).isEqualTo(cssResource);
    }

    @Test
    public void cssResourceEnsureInjected() {
        // Given
        MyCssResource testCssResource = MyClientBundle.INSTANCE.cssResource();

        // When & Then
        assertThat(testCssResource.ensureInjected()).isTrue();
        assertThat(testCssResource.ensureInjected()).isFalse();
    }

    @Test
    public void dataResource() {
        // Given
        DataResource testDataResource = MyClientBundle.INSTANCE.dataResource();

        // When
        String name = testDataResource.getName();
        String url = testDataResource.getSafeUri().asString();
        String toString = testDataResource.toString();

        // Then
        assertThat(name).isEqualTo("dataResource");
        assertThat(url).isEqualTo("http://127.0.0.1:8888/gwt_test_utils_module/textResourceXml.xml");
        assertThat(toString).isEqualTo("com.googlecode.gwt.test.internal.resources.DataResourceCallback generated for 'com.googlecode.gwt.test.resources.MyClientBundle.dataResource()'");
        assertThat(MyClientBundle.INSTANCE.dataResource()).isEqualTo(testDataResource);
    }

    @Test
    public void imageResource() {
        // Given
        ImageResource testImageResource = MyClientBundle.INSTANCE.imageResource();

        // When
        String name = testImageResource.getName();
        String url = testImageResource.getSafeUri().asString();
        int heigh = testImageResource.getHeight();
        int left = testImageResource.getLeft();
        int width = testImageResource.getWidth();
        int top = testImageResource.getTop();
        String toString = testImageResource.toString();

        // Then
        assertThat(name).isEqualTo("imageResource");
        assertThat(url).isEqualTo("http://127.0.0.1:8888/gwt_test_utils_module/imageResource.gif");
        assertThat(heigh).isEqualTo(0);
        assertThat(left).isEqualTo(0);
        assertThat(width).isEqualTo(0);
        assertThat(top).isEqualTo(0);
        assertThat(toString).isEqualTo("com.googlecode.gwt.test.internal.resources.ImageResourceCallback generated for 'com.googlecode.gwt.test.resources.MyClientBundle.imageResource()'");
        assertThat(MyClientBundle.INSTANCE.imageResource()).isEqualTo(testImageResource);
    }

    @Test
    public void imageResource_FromGwtAPI() {
        // Given
        Resources treeResources = GWT.create(Resources.class);

        // When
        String name = treeResources.treeOpen().getName();
        String url = treeResources.treeLeaf().getSafeUri().asString();

        // Then
        assertThat(name).isEqualTo("treeOpen");
        assertThat(url).isEqualTo("http://127.0.0.1:8888/gwt_test_utils_module/treeLeaf.gif");
    }

    @Test
    public void imageResource_ShouldThrowExceptionWhenMultipleMatchingResourceFile() {
        // Given
        String expectedMessage = "Too many ImageResource files found for method 'com.googlecode.gwt.test.resources.MyClientBundle.doubleShouldThrowException()'";
        try {
            // When
            MyClientBundle.INSTANCE.doubleShouldThrowException();
            fail("An exception should have been thrown since there are multiple matching file for the tested ClientBundle method");
        } catch (Exception e) {
            // Then
            assertThat(e.getMessage()).isEqualTo(expectedMessage);
        }

    }

    @Test
    public void multipleFileResource() {
        // Given
        MultipleFileCssResource multipleFileCssResource = MyClientBundle.INSTANCE.multipleFileCssResource();

        // When
        String name = multipleFileCssResource.getName();
        String testStyle = multipleFileCssResource.testStyle();
        String testStyleWithHover = multipleFileCssResource.testStyleWithHover();
        String testStyleOnSpecificElement = multipleFileCssResource.testStyleOnSpecificElement();
        String testStyleOnSpecificStyle = multipleFileCssResource.testStyleOnSpecificStyle();
        String addedStyle = multipleFileCssResource.addedStyle();
        String constantValue = multipleFileCssResource.testConstant();
        String addedConstantValue = multipleFileCssResource.addedConstant();
        String toString = multipleFileCssResource.toString();

        // Then
        assertThat(name).isEqualTo("multipleFileCssResource");
        assertThat(testStyle).isEqualTo("testStyle");
        assertThat(testStyleWithHover).isEqualTo("testStyleWithHover");
        assertThat(testStyleOnSpecificElement).isEqualTo("testStyleOnSpecificElement");
        assertThat(testStyleOnSpecificStyle).isEqualTo("testStyleOnSpecificStyle");
        assertThat(addedStyle).isEqualTo("addedStyle");
        assertThat(constantValue).isEqualTo("constant-value");
        assertThat(addedConstantValue).isEqualTo("added-constant-value");
        assertThat(toString).isEqualTo("com.googlecode.gwt.test.internal.resources.CssResourceCallback generated for 'com.googlecode.gwt.test.resources.MyClientBundle.multipleFileCssResource()'");
        assertThat(MyClientBundle.INSTANCE.multipleFileCssResource()).isEqualTo(multipleFileCssResource);
    }

    @Test
    public void rootClasspathImg() {
        // Given
        ImageResource rootClasspathImg = MyClientBundle.INSTANCE.rootClasspathImg();

        // When
        String name = rootClasspathImg.getName();
        String url = rootClasspathImg.getSafeUri().asString();
        int heigh = rootClasspathImg.getHeight();
        int left = rootClasspathImg.getLeft();
        int width = rootClasspathImg.getWidth();
        int top = rootClasspathImg.getTop();
        String toString = rootClasspathImg.toString();

        // Then
        assertThat(name).isEqualTo("rootClasspathImg");
        assertThat(url).isEqualTo("http://127.0.0.1:8888/gwt_test_utils_module/root-classpath-img.png");
        assertThat(heigh).isEqualTo(0);
        assertThat(left).isEqualTo(0);
        assertThat(width).isEqualTo(0);
        assertThat(top).isEqualTo(0);
        assertThat(toString).isEqualTo("com.googlecode.gwt.test.internal.resources.ImageResourceCallback generated for 'com.googlecode.gwt.test.resources.MyClientBundle.rootClasspathImg()'");
        assertThat(MyClientBundle.INSTANCE.rootClasspathImg()).isEqualTo(rootClasspathImg);
    }

    @Test
    public void textResource_FromGwtAPI() {
        // Given
        com.google.gwt.user.client.impl.WindowImplIE.Resources treeResources = GWT.create(com.google.gwt.user.client.impl.WindowImplIE.Resources.class);

        // When
        String name = treeResources.initWindowCloseHandler().getName();
        String text = treeResources.initWindowCloseHandler().getText();

        // Then
        assertThat(name).isEqualTo("initWindowCloseHandler");
        assertThat(text.startsWith("function __gwt_initWindowCloseHandler(beforeunload, unload) {")).isTrue();
    }

    @Test
    public void textResource_Txt() {
        // Given
        TextResource textResource = MyClientBundle.INSTANCE.textResourceTxt();
        String expectedText = "Hello gwt-test-utils !\r\nThis is a test with a simple text file";

        // When
        String name = textResource.getName();
        String text = textResource.getText();
        String toString = textResource.toString();

        // Then
        assertThat(name).isEqualTo("textResourceTxt");
        assertThat(text).isEqualTo(expectedText);
        assertThat(toString).isEqualTo("com.googlecode.gwt.test.internal.resources.TextResourceCallback generated for 'com.googlecode.gwt.test.resources.MyClientBundle.textResourceTxt()'");
        assertThat(MyClientBundle.INSTANCE.textResourceTxt()).isEqualTo(textResource);
    }

    @Test
    public void textResource_Xml() {
        // Given
        TextResource textResource = MyClientBundle.INSTANCE.textResourceXml();
        String expectedText = "<gwt-test-utils>\r\n    <test>this is a test</test>\r\n</gwt-test-utils>";

        // When
        String name = textResource.getName();
        String text = textResource.getText();
        String toString = textResource.toString();

        // Then
        assertThat(name).isEqualTo("textResourceXml");
        assertThat(text).isEqualTo(expectedText);
        assertThat(toString).isEqualTo("com.googlecode.gwt.test.internal.resources.TextResourceCallback generated for 'com.googlecode.gwt.test.resources.MyClientBundle.textResourceXml()'");
        assertThat(MyClientBundle.INSTANCE.textResourceXml()).isEqualTo(textResource);
    }

}

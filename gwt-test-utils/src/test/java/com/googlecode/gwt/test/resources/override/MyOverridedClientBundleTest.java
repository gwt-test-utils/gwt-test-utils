package com.googlecode.gwt.test.resources.override;

import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MyOverridedClientBundleTest extends GwtTestTest {

    @Test
    public void testDataResource_NoOverride() {
        // Given
        DataResource testDataResource = MyOverridedClientBundle.INSTANCE.dataResource();

        // When
        String name = testDataResource.getName();
        String url = testDataResource.getSafeUri().asString();
        String toString = testDataResource.toString();

        // Then
        assertThat(name).isEqualTo("dataResource");
        assertThat(url).isEqualTo("http://127.0.0.1:8888/gwt_test_utils_module/textResourceXml.xml");
        assertThat(toString).isEqualTo("com.googlecode.gwt.test.internal.resources.DataResourceCallback generated for 'com.googlecode.gwt.test.resources.override.MyOverridedClientBundle.dataResource()'");
        assertThat(MyOverridedClientBundle.INSTANCE.dataResource()).isEqualTo(testDataResource);
    }

    @Test
    public void testImageResource_OverrideWithAnnotation() {
        // Given
        ImageResource testImageResource = MyOverridedClientBundle.INSTANCE.imageResource();

        // When
        String name = testImageResource.getName();
        String url = testImageResource.getSafeUri().asString();
        int heigh = testImageResource.getHeight();
        int left = testImageResource.getLeft();
        int width = testImageResource.getWidth();
        int top = testImageResource.getTop();

        // Then
        assertThat(name).isEqualTo("imageResource");
        assertThat(url).isEqualTo("http://127.0.0.1:8888/gwt_test_utils_module/override_testImageResource.gif");
        assertThat(heigh).isEqualTo(0);
        assertThat(left).isEqualTo(0);
        assertThat(width).isEqualTo(0);
        assertThat(top).isEqualTo(0);
        assertThat(MyOverridedClientBundle.INSTANCE.imageResource()).isEqualTo(testImageResource);
    }

    @Test
    public void textResourceTxt_OverrideWithoutAnnotation() {
        // Given
        TextResource textResource = MyOverridedClientBundle.INSTANCE.textResourceTxt();
        String expectedText = "Overrided text resource !";

        // When
        String name = textResource.getName();
        String text = textResource.getText();

        // Then
        assertThat(name).isEqualTo("textResourceTxt");
        assertThat(text).isEqualTo(expectedText);
        assertThat(MyOverridedClientBundle.INSTANCE.textResourceTxt()).isEqualTo(textResource);
    }

}

package com.googlecode.gwt.test.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GwtReflectionUtilsTest {

    @Test
    public void callPrivateMethod() {
        // Given
        TestBean testBean = new TestBean();

        // When
        GwtReflectionUtils.callPrivateMethod(testBean, "setString", "my string");

        // Then
        assertThat(testBean.string).isEqualTo("my string");
    }

    @Test
    public void callPrivateMethod_Primitive() {
        // Given
        TestBean testBean = new TestBean();

        // When
        GwtReflectionUtils.callPrivateMethod(testBean, "setBool", true);

        // Then
        assertThat(testBean.bool).isTrue();
    }

    @Test
    public void callPrivateMethod_PrimitiveToWrapperType() {
        // Given
        TestBean testBean = new TestBean();

        // When
        GwtReflectionUtils.callPrivateMethod(testBean, "setBoolObject", true);

        // Then
        assertThat(testBean.bool).isTrue();
    }

    @Test
    public void getPrivateFieldValue() {
        // Given
        TestBean testBean = new TestBean();
        testBean.setString("my string");

        // When
        String s = GwtReflectionUtils.getPrivateFieldValue(testBean, "string");

        // Then
        assertThat(s).isEqualTo("my string");
    }

    @Test
    public void getPrivateFieldValue_Primitive() {
        // Given
        TestBean testBean = new TestBean();
        testBean.setBool(true);

        // When
        boolean b = (Boolean) GwtReflectionUtils.getPrivateFieldValue(testBean, "bool");

        // Then
        assertThat(b).isTrue();
    }

    @Test
    public void ok() {
        // When
        String c = GwtReflectionUtils.getStaticFieldValue(TestBean.class, "CONST");

        // Then
        assertThat(c).isEqualTo(TestBean.CONST);
    }

    private static class TestBean {

        private static final String CONST = "TEST BEAN CONST";
        private boolean bool;
        private String string;

        private void setBool(boolean bool) {
            this.bool = bool;
        }

        @SuppressWarnings("unused")
        private void setBoolObject(Boolean bool) {
            this.bool = bool;
        }

        private void setString(String string) {
            this.string = string;
        }
    }

}

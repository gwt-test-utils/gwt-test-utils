package com.googlecode.gwt.test;

import com.googlecode.gwt.test.client.MyObject;
import com.googlecode.gwt.test.i18n.MyConstants;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@GwtModule("com.googlecode.gwt.test.GwtTestUtils")
public class MockitoGwtTest extends GwtTestWithMockito {

    public static class MyBean {

        private final MyConstants contants;
        private final MyObject object;
        private final MyRemoteService remoteService;

        public MyBean(MyConstants contants, MyRemoteService remoteService, MyObject object) {
            this.contants = contants;
            this.remoteService = remoteService;
            this.object = object;
        }

        public String getMyField() {
            return object.getMyField();
        }

        public String goodbye() {
            return contants.goodbye();
        }

        public String myMethod(String param) {
            return remoteService.myMethod(param);
        }

    }

    @InjectMocks
    MyBean bean;

    @Mock
    MyConstants contants;

    @Spy
    MyObject object;

    @org.mockito.Mock
    MyRemoteService remoteService;

    @Test
    public void injectMockitoMock() {
        // Given
        doReturn("mocked method call").when(remoteService).myMethod("testParam");

        // When
        String myMethod = bean.myMethod("testParam");

        // Then
        assertThat(myMethod).isEqualTo("mocked method call");
    }

    @Test
    public void injectGwtMock() {
        // Given
        doReturn("mocked bye !").when(contants).goodbye();

        // When
        String goodbye = bean.goodbye();

        // Then
        assertThat(goodbye).isEqualTo("mocked bye !");
    }

    @Test
    public void injectSpy() {
        // Given
        object.setMyField("my field");

        // When
        String myfield = bean.getMyField();

        // Then
        assertThat(myfield).isEqualTo("my field");
        verify(object).getMyField();
    }

}

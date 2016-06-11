package com.googlecode.gwt.test;

import com.googlecode.gwt.test.client.MyObject;
import com.googlecode.gwt.test.i18n.MyConstants;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;

import static org.fest.assertions.api.Assertions.assertThat;
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
    public void injectGwtMock() {
        // Arrange
        doReturn("mocked method call").when(remoteService).myMethod("testParam");

        // Act
        String myMethod = bean.myMethod("testParam");

        // Assert
        assertThat(myMethod).isEqualTo("mocked method call");
    }

    @Test
    public void injectMockitoMock() {
        // Arrange
        doReturn("mocked bye !").when(contants).goodbye();

        // Act
        String goodbye = bean.goodbye();

        // Assert
        assertThat(goodbye).isEqualTo("mocked bye !");
    }

    @Test
    public void injectSpy() {
        // Arrange
        object.setMyField("my field");

        // Act
        String myfield = bean.getMyField();

        // Assert
        assertThat(myfield).isEqualTo("my field");
        verify(object).getMyField();
    }

}

package com.googlecode.gwt.test.spring;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.client.MyObject;
import com.googlecode.gwt.test.rpc.MyService;
import com.googlecode.gwt.test.rpc.MyServiceAsync;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

@GwtModule("com.googlecode.gwt.test.GwtTestUtils")
@ContextConfiguration(locations = {"classpath:com/googlecode/gwt/test/spring/applicationContext-test.xml"}, loader = GwtTestContextLoader.class)
public class SimpleGwtSpringTest extends GwtSpringTest {

    private boolean success;

    @Test
    public void rpcCall() {
        // Given
        success = false;
        MyObject object = new MyObject("my field initialized during test setup");

        MyServiceAsync myService = GWT.create(MyService.class);

        // When
        myService.update(object, new AsyncCallback<MyObject>() {

            public void onFailure(Throwable caught) {
                fail("onFailure should not be called");
            }

            public void onSuccess(MyObject result) {
                // Then 2
                assertThat(result.getMyField()).isEqualTo("updated field by server side code");
                assertThat(result.getMyTransientField()).isEqualTo("transient field");

                assertThat(result.getMyChildObjects()).hasSize(1);
                assertThat(result.getMyChildObjects().get(0).getMyChildField()).isEqualTo("this is a child !");
                assertThat(result.getMyChildObjects().get(0).getMyChildTransientField()).isEqualTo("child object transient field");

                assertThat(result.getMyChildObjects().get(0).getMyField()).isEqualTo("the field inherited from the parent has been updated !");
                assertThat(result.getMyChildObjects().get(0).getMyTransientField()).isEqualTo("transient field");
                success = true;
            }
        });

        // Then 1
        assertThat(success).isFalse();
        getBrowserSimulator().fireLoopEnd();
        // Then 3
        assertThat(success).isTrue();
    }

    @Override
    protected String getHostPagePath(String moduleFullQualifiedName) {
        return null;
    }

}

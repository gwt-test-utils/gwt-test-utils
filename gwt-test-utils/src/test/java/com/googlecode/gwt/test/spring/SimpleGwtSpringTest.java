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
import static org.junit.Assert.assertEquals;

@GwtModule("com.googlecode.gwt.test.GwtTestUtils")
@ContextConfiguration(locations = {"classpath:com/googlecode/gwt/test/spring/applicationContext-test.xml"}, loader = GwtTestContextLoader.class)
public class SimpleGwtSpringTest extends GwtSpringTest {

    private boolean success;

    @Test
    public void rpcCall() {
        // Arrange
        success = false;
        MyObject object = new MyObject("my field initialized during test setup");

        MyServiceAsync myService = GWT.create(MyService.class);

        // Act
        myService.update(object, new AsyncCallback<MyObject>() {

            public void onFailure(Throwable caught) {
                fail("onFailure should not be called");
            }

            public void onSuccess(MyObject result) {
                // Assert 2
                assertEquals("updated field by server side code", result.getMyField());
                assertEquals("transient field", result.getMyTransientField());

                assertEquals("A single child object should have been instanciate in server code", 1,
                        result.getMyChildObjects().size());
                assertEquals("this is a child !", result.getMyChildObjects().get(0).getMyChildField());
                assertEquals("child object transient field",
                        result.getMyChildObjects().get(0).getMyChildTransientField());

                assertEquals("the field inherited from the parent has been updated !",
                        result.getMyChildObjects().get(0).getMyField());
                assertEquals("transient field", result.getMyChildObjects().get(0).getMyTransientField());
                success = true;
            }
        });

        // Assert 1
        assertThat(success).isFalse();
        getBrowserSimulator().fireLoopEnd();
        // Assert 3
        assertThat(success).isTrue();
    }

    @Override
    protected String getHostPagePath(String moduleFullQualifiedName) {
        return null;
    }

}

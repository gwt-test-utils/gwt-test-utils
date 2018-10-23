package com.googlecode.gwt.test.csv;

import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.csv.data.MyBeautifulApp;
import com.googlecode.gwt.test.csv.data.MyRemoteService;
import com.googlecode.gwt.test.csv.data.MyService;
import com.googlecode.gwt.test.csv.data.MyStringStore;
import com.googlecode.gwt.test.finder.GwtFinder;
import com.googlecode.gwt.test.finder.Node;
import com.googlecode.gwt.test.finder.NodeObjectFinder;
import com.googlecode.gwt.test.rpc.RemoteServiceCreateHandler;
import org.junit.Before;
import org.junit.runner.RunWith;

import static com.googlecode.gwt.test.finder.GwtFinder.object;

@RunWith(GwtCsvRunner.class)
@GwtModule("com.googlecode.gwt.test.csv.GwtCsvTest")
public abstract class MyGwtShell extends GwtCsvTest {

    private MyBeautifulApp app;

    @CsvMethod
    public void append(String s) {
        MyStringStore.appender += s;
    }

    @CsvMethod
    public void detachWidget(String... params) {
        object(params).ofType(Widget.class).removeFromParent();
    }

    @CsvMethod
    public void initApp() {
        app = new MyBeautifulApp();
        app.onModuleLoad();
    }

    @CsvMethod
    public void setId(String newId, String... params) {
        object(params).ofType(Widget.class).getElement().setId(newId);
    }

    @Before
    public void before() {
        registerCustomCreateHandler();
        registerNodeFinder();
    }

    @Override
    protected String getHostPagePath(String moduleFullQualifiedName) {
        return null;
    }

    private void registerCustomCreateHandler() {
        addGwtCreateHandler(new RemoteServiceCreateHandler() {

            @Override
            public Object findService(Class<?> remoteServiceClass, String remoteServiceRelativePath) {
                if (remoteServiceClass == MyRemoteService.class
                        && "myService".equals(remoteServiceRelativePath)) {
                    return new MyService();
                }
                return null;
            }

        });
    }

    private void registerNodeFinder() {

        GwtFinder.registerNodeFinder("app", node -> getCsvRunner().getNodeValue(app, node));

        MyStringStore.appender = "";

        GwtFinder.registerNodeFinder("appender", node -> MyStringStore.appender);
    }

}

package com.googlecode.gwt.test.csv.runner;

import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.test.assertions.GwtAssertions;
import com.googlecode.gwt.test.csv.CsvMethod;
import com.googlecode.gwt.test.csv.GwtTestCsvException;
import com.googlecode.gwt.test.finder.Node;
import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class CsvRunnerTest {

    class A {

        @SuppressWarnings("unused")
        public String zzz = "zzz";

        @SuppressWarnings("unused")
        private final String zz = "zz";

        @CsvMethod
        public Object getMe() {
            return this;
        }

        @CsvMethod
        public String getPublic() {
            return "public";
        }

        @CsvMethod
        Map<String, String> getMap() {
            Map<String, String> map = new HashMap<>();
            map.put("a", "b");
            map.put("c", "d");
            return map;
        }

        @CsvMethod
        String getWidget(String i) {
            return i;
        }

        @CsvMethod
        String getWidgetInt(int i) {
            return Integer.toString(i);
        }

        @CsvMethod
        void meth0() {
            assertThat(true).isTrue();
        }

        @CsvMethod
        void meth1(String p0) {
            assertThat(p0).isEqualTo("p0");
        }

        @CsvMethod
        void meth2(String p0, String p1) {
            assertThat(p0).isEqualTo("p0");
            assertThat(p1).isEqualTo("p1");
        }

        @CsvMethod
        void meth3(String p0, String p1, String p2) {
            assertThat(p0).isEqualTo("p0");
            assertThat(p1).isEqualTo("p1");
            assertThat(p2).isEqualTo("p2");
        }

        @CsvMethod
        void methArray0(String[] p) {
            assertThat(p).containsSequence("p0", "p1", "p2");
        }

        @CsvMethod
        void methArray1(String a, String[] p) {
            assertThat(a).isEqualTo("a");
            assertThat(p).containsSequence("p0", "p1", "p2");
        }

        @CsvMethod
        void methVar0(String... p) {
            assertThat(p).containsSequence("p0", "p1", "p2");
        }

        @CsvMethod
        void methVar1(String a, String... p) {
            assertThat(a).isEqualTo("a");
            assertThat(p).containsSequence("p0", "p1", "p2");
        }

        @CsvMethod
        void runException() {
            throw new UnsupportedOperationException();
        }

        @CsvMethod
        void runMyException() throws MyException {
            throw new MyException();
        }

        @SuppressWarnings("unused")
        private String getPrivate() {
            return "private";
        }
    }

    class B extends A {

    }

    class MyException extends Exception {

        private static final long serialVersionUID = 1L;

    }

    class SimiliWidget {

        private final String id;

        private String label;

        private final List<SimiliWidget> list;

        public SimiliWidget(String id, String label) {
            this.list = new ArrayList<>();
            this.id = id;
            this.label = label;
        }

        public List<SimiliWidget> getCurrentList() {
            return list;
        }

        public String getLabel() {
            return label;
        }

        public String getLabelWithParam(String s) {
            return label;
        }

        public SimiliWidget getWidget(int index) {
            return list.get(index);
        }

        public int getWidgetCount() {
            return list.size();
        }

        public void setLabel(String label) {
            this.label = label;
        }

    }

    class SimiliWidgetContainer {

        private final SimiliWidget widget;

        public SimiliWidgetContainer(SimiliWidget widget) {
            this.widget = widget;
        }

        public SimiliWidget getWidget() {
            return widget;
        }

    }

    private final Object o = new A();

    private final Object oo = new B();
    private final CsvRunner runner = new CsvRunner(ArrayList::new, "Runner");

    @Test
    public void executeLine_Exception() {
        try {
            runner.executeLine("runMyException", new ArrayList<>(), o);
            failBecauseExceptionWasNotThrown(GwtTestCsvException.class);
        } catch (GwtTestCsvException e) {
            assertThat(e.getMessage()).isEqualTo(
                    "Runner error line 0: Error invoking @CsvMethod void com.googlecode.gwt.test.csv.runner.CsvRunnerTest$A.runMyException() throws com.googlecode.gwt.test.csv.runner.CsvRunnerTest$MyException");
        }
    }

    @Test
    public void getInList() {
        SimiliWidgetContainer root = getList();
        assertThat(runner.<SimiliWidget>getNodeValue(root, Node.parse("/widget/widget(2)"))).isSameAs(root.getWidget().list.get(2));
        assertThat(runner.<SimiliWidget>getNodeValue(root, Node.parse("/widget/widget[label=child3]"))).isSameAs(root.getWidget().list.get(2));
        assertThat(runner.<SimiliWidget>getNodeValue(root, Node.parse("/widget/widget[getLabel=child3]"))).isSameAs(root.getWidget().list.get(2));
        assertThat(runner.<SimiliWidget>getNodeValue(root, Node.parse("/widget/widget[id=child3Id]"))).isSameAs(root.getWidget().list.get(2));
        assertThat(runner.<SimiliWidget>getNodeValue(root, Node.parse("/widget/list[id=child3Id]"))).isSameAs(root.getWidget().list.get(2));
        assertThat(runner.<SimiliWidget>getNodeValue(root, Node.parse("/widget/getCurrentlist[id=child3Id]"))).isSameAs(root.getWidget().list.get(2));
    }

    @Test
    public void getInListRecurse() {
        SimiliWidgetContainer root = getList();
        assertThat(runner.<SimiliWidget>getNodeValue(root, Node.parse("/widget/widget[label/toString=child3]"))).isSameAs(root.getWidget().list.get(2));
        assertThat(runner.<SimiliWidget>getNodeValue(root, Node.parse("/widget/widget[labelWithParam(a)/toString=child3]"))).isSameAs(root.getWidget().list.get(2));
        assertThat(runner.<String>getNodeValue(root, Node.parse("/widget/widget[label/toString=child3]/id"))).isSameAs(root.getWidget().list.get(2).id);
    }

    @Test
    public void getMap() {
        assertThat(runner.<String>getNodeValue(o, Node.parse("/map[a]"))).isEqualTo("b");
        assertThat(runner.<String>getNodeValue(o, Node.parse("/map[c]"))).isEqualTo("d");
    }

    @Test
    public void getMapNotFound() {
        assertThat(runner.<Widget>getNodeValue(o, Node.parse("/map[b]"))).isNull();
    }

    @Test
    public void getter() {
        assertThat(runner.<String>getNodeValue(o, Node.parse("/public"))).isEqualTo("public");
        assertThat(runner.<String>getNodeValue(o, Node.parse("/getpublic"))).isEqualTo("public");
        assertThat(runner.<String>getNodeValue(o, Node.parse("/private"))).isEqualTo("private");
        assertThat(runner.<String>getNodeValue(o, Node.parse("/zz"))).isEqualTo("zz");
        assertThat(runner.<String>getNodeValue(o, Node.parse("/zzz"))).isEqualTo("zzz");
        assertThat(runner.<String>getNodeValue(o, Node.parse("/me/ME/getMe/zzz"))).isEqualTo("zzz");
    }

    @Test
    public void getterDerived() {
        assertThat(runner.<String>getNodeValue(oo, Node.parse("/zz"))).isEqualTo("zz");
        assertThat(runner.<String>getNodeValue(oo, Node.parse("/zzz"))).isEqualTo("zzz");
    }

    @Test
    public void getWidget() {
        assertThat(runner.<String>getNodeValue(o, Node.parse("/getWidget(toto)"))).isEqualTo("toto");
        assertThat(runner.<String>getNodeValue(o, Node.parse("/WIDGET(toto)"))).isEqualTo("toto");
    }

    @Test
    public void getWidgetInt() {
        assertThat(runner.<String>getNodeValue(o, Node.parse("/getWidgetInt(0)"))).isEqualTo("0");
        assertThat(runner.<String>getNodeValue(o, Node.parse("/getWidgetInt(12)"))).isEqualTo("12");
    }

    @Test
    public void meth0() throws Exception {
        runner.executeLine("meth0", new ArrayList<>(), o);
    }

    @Test
    public void meth0Derived() throws Exception {
        runner.executeLine("meth0", new ArrayList<>(), oo);
    }

    @Test
    public void meth1() throws Exception {
        runner.executeLine("meth1", Arrays.asList("p0"), o);
    }

    @Test(expected = AssertionError.class)
    public void meth1WrongValue() {
        runner.executeLine("meth1", Arrays.asList("p4"), o);
    }

    @Test
    public void meth2() throws Exception {
        runner.executeLine("meth2", Arrays.asList("p0", "p1"), o);
    }

    @Test
    public void meth3() throws Exception {
        runner.executeLine("meth3", Arrays.asList("p0", "p1", "p2"), o);
    }

    @Test
    public void methArray0() throws Exception {
        runner.executeLine("methArray0", Arrays.asList("p0", "p1", "p2"), o);
    }

    @Test
    public void methArray1() throws Exception {
        runner.executeLine("methArray1", Arrays.asList("a", "p0", "p1", "p2"), o);
    }

    @Test
    public void methVar0() throws Exception {
        runner.executeLine("methVar0", Arrays.asList("p0", "p1", "p2"), o);
    }

    @Test
    public void methVar1() throws Exception {
        runner.executeLine("methVar1", Arrays.asList("a", "p0", "p1", "p2"), o);
    }

    @Test
    public void runtime() {
        try {
            runner.executeLine("runException", new ArrayList<>(), o);
            failBecauseExceptionWasNotThrown(GwtTestCsvException.class);
        } catch (GwtTestCsvException e) {
            assertThat(e.getMessage()).isEqualTo(
                    "Runner error line 0: Error invoking @CsvMethod void com.googlecode.gwt.test.csv.runner.CsvRunnerTest$A.runException()");
        }
    }

    private SimiliWidgetContainer getList() {
        SimiliWidget root = new SimiliWidget("rootId", "root");
        SimiliWidget child1 = new SimiliWidget("child1Id", "child1");
        SimiliWidget child2 = new SimiliWidget("child2Id", "child2");
        SimiliWidget child3 = new SimiliWidget("child3Id", "child3");
        root.list.add(child1);
        root.list.add(child2);
        root.list.add(child3);
        return new SimiliWidgetContainer(root);
    }

}

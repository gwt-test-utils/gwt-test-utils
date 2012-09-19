package com.googlecode.gwt.test.csv.internal;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.googlecode.gwt.test.csv.CsvMethod;
import com.googlecode.gwt.test.csv.GwtTestCsvException;
import com.googlecode.gwt.test.csv.runner.CsvRunner;
import com.googlecode.gwt.test.finder.Node;

public class CsvRunnerTest {

   class A {

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
         Map<String, String> map = new HashMap<String, String>();
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
         assertTrue(true);
      }

      @CsvMethod
      void meth1(String p0) {
         assertEquals("p0", p0);
      }

      @CsvMethod
      void meth2(String p0, String p1) {
         assertEquals("p0", p0);
         assertEquals("p1", p1);
      }

      @CsvMethod
      void meth3(String p0, String p1, String p2) {
         assertEquals("p0", p0);
         assertEquals("p1", p1);
         assertEquals("p2", p2);
      }

      @CsvMethod
      void methArray0(String[] p) {
         assertEquals(3, p.length);
         assertEquals("p0", p[0]);
         assertEquals("p1", p[1]);
         assertEquals("p2", p[2]);

      }

      @CsvMethod
      void methArray1(String a, String[] p) {
         assertEquals("a", a);
         assertEquals(3, p.length);
         assertEquals("p0", p[0]);
         assertEquals("p1", p[1]);
         assertEquals("p2", p[2]);
      }

      @CsvMethod
      void methVar0(String... p) {
         assertEquals(3, p.length);
         assertEquals("p0", p[0]);
         assertEquals("p1", p[1]);
         assertEquals("p2", p[2]);
      }

      @CsvMethod
      void methVar1(String a, String... p) {
         assertEquals("a", a);
         assertEquals(3, p.length);
         assertEquals("p0", p[0]);
         assertEquals("p1", p[1]);
         assertEquals("p2", p[2]);
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
         this.list = new ArrayList<SimiliWidget>();
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

   };

   private final Object o = new A();

   private final Object oo = new B();
   private final CsvRunner runner = new CsvRunner();

   @Test
   public void executeLine_Exception() {
      try {
         runner.executeLine("runMyException", new ArrayList<String>(), o);
         failBecauseExceptionWasNotThrown(GwtTestCsvException.class);
      } catch (GwtTestCsvException e) {
         assertThat(e.getMessage()).isEqualTo(
                  "Error line 0: Error invoking @CsvMethod void com.googlecode.gwt.test.csv.internal.CsvRunnerTest$A.runMyException() throws com.googlecode.gwt.test.csv.internal.CsvRunnerTest$MyException");
      }
   }

   @Test
   public void getInList() {
      SimiliWidgetContainer root = getList();
      assertTrue(root.getWidget().list.get(2) == runner.getNodeValue(root,
               Node.parse("/widget/widget(2)")));
      assertTrue(root.getWidget().list.get(2) == runner.getNodeValue(root,
               Node.parse("/widget/widget[label=child3]")));
      assertTrue(root.getWidget().list.get(2) == runner.getNodeValue(root,
               Node.parse("/widget/widget[getLabel=child3]")));
      assertTrue(root.getWidget().list.get(2) == runner.getNodeValue(root,
               Node.parse("/widget/widget[id=child3Id]")));
      assertTrue(root.getWidget().list.get(2) == runner.getNodeValue(root,
               Node.parse("/widget/list[id=child3Id]")));
      assertTrue(root.getWidget().list.get(2) == runner.getNodeValue(root,
               Node.parse("/widget/getCurrentlist[id=child3Id]")));
   }

   @Test
   public void getInListRecurse() {
      SimiliWidgetContainer root = getList();
      assertTrue(root.getWidget().list.get(2) == runner.getNodeValue(root,
               Node.parse("/widget/widget[label/toString=child3]")));
      assertTrue(root.getWidget().list.get(2) == runner.getNodeValue(root,
               Node.parse("/widget/widget[labelWithParam(a)/toString=child3]")));
      assertTrue(root.getWidget().list.get(2).id == runner.getNodeValue(root,
               Node.parse("/widget/widget[label/toString=child3]/id")));
   }

   @Test
   public void getMap() {
      assertEquals("b", runner.getNodeValue(o, Node.parse("/map[a]")));
      assertEquals("d", runner.getNodeValue(o, Node.parse("/map[c]")));
   }

   @Test
   public void getMapNotFound() {
      assertNull(runner.getNodeValue(o, Node.parse("/map[b]")));
   }

   @Test
   public void getter() {
      assertEquals("public", runner.getNodeValue(o, Node.parse("/public")));
      assertEquals("public", runner.getNodeValue(o, Node.parse("/getpublic")));
      assertEquals("private", runner.getNodeValue(o, Node.parse("/private")));
      assertEquals("zz", runner.getNodeValue(o, Node.parse("/zz")));
      assertEquals("zzz", runner.getNodeValue(o, Node.parse("/zzz")));
      assertEquals("zzz", runner.getNodeValue(o, Node.parse("/me/ME/getMe/zzz")));
      assertNotNull("zz", runner.getNodeValue(o, Node.parse("/toString")));
   }

   @Test
   public void getterDerived() {
      assertEquals("zz", runner.getNodeValue(oo, Node.parse("/zz")));
      assertEquals("zzz", runner.getNodeValue(oo, Node.parse("/zzz")));
   }

   @Test
   public void getWidget() {
      assertEquals("toto", runner.getNodeValue(o, Node.parse("/getWidget(toto)")));
      assertEquals("toto", runner.getNodeValue(o, Node.parse("/WIDGET(toto)")));
   }

   @Test
   public void getWidgetInt() {
      assertEquals("0", runner.getNodeValue(o, Node.parse("/getWidgetInt(0)")));
      assertEquals("12", runner.getNodeValue(o, Node.parse("/getWidgetInt(12)")));
   }

   @Test
   public void meth0() throws Exception {
      runner.executeLine("meth0", new ArrayList<String>(), o);
   }

   @Test
   public void meth0Derived() throws Exception {
      runner.executeLine("meth0", new ArrayList<String>(), oo);
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
         runner.executeLine("runException", new ArrayList<String>(), o);
         failBecauseExceptionWasNotThrown(GwtTestCsvException.class);
      } catch (GwtTestCsvException e) {
         assertThat(e.getMessage()).isEqualTo(
                  "Error line 0: Error invoking @CsvMethod void com.googlecode.gwt.test.csv.internal.CsvRunnerTest$A.runException()");
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

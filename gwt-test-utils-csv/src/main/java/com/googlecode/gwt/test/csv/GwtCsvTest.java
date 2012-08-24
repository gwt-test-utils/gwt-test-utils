package com.googlecode.gwt.test.csv;

import static com.googlecode.gwt.test.assertions.GwtAssertions.assertThat;
import static com.googlecode.gwt.test.finder.GwtFinder.object;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Fail.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.fest.assertions.api.Fail;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.csv.internal.DirectoryTestReader;
import com.googlecode.gwt.test.csv.runner.CsvRunner;
import com.googlecode.gwt.test.csv.tools.DefaultWidgetVisitor;
import com.googlecode.gwt.test.csv.tools.WidgetVisitor;
import com.googlecode.gwt.test.finder.GwtFinder;
import com.googlecode.gwt.test.finder.GwtInstance;
import com.googlecode.gwt.test.finder.Node;
import com.googlecode.gwt.test.finder.NodeObjectFinder;
import com.googlecode.gwt.test.internal.GwtConfig;
import com.googlecode.gwt.test.internal.utils.ArrayUtils;
import com.googlecode.gwt.test.internal.utils.GwtStringUtils;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import com.googlecode.gwt.test.utils.WidgetUtils;
import com.googlecode.gwt.test.utils.events.Browser;
import com.googlecode.gwt.test.utils.events.Browser.BrowserErrorHandler;

@RunWith(GwtCsvRunner.class)
public abstract class GwtCsvTest extends GwtTest {

   private static class MacroReader {

      private final static Logger logger = LoggerFactory.getLogger(MacroReader.class);

      private final HashMap<String, List<List<String>>> macroList;

      public MacroReader() {
         macroList = new HashMap<String, List<List<String>>>();
      }

      public List<List<String>> getMacro(String macroName) {
         return macroList.get(macroName);
      }

      public void read(List<List<String>> sheet) {
         logger.info("Start reading macro");
         String currentMacroName = null;
         List<List<String>> currentMacroContent = null;
         for (List<String> row : sheet) {
            if (currentMacroName != null) {
               if (row.size() > 0 && "endmacro".equals(row.get(0))) {
                  logger.info("End macro, length : " + currentMacroContent.size());
                  macroList.put(currentMacroName, currentMacroContent);
                  currentMacroName = null;
               } else {
                  currentMacroContent.add(row);
               }
            } else {
               if (row.size() > 0 && "macro".equals(row.get(0))) {
                  currentMacroName = row.size() > 1 ? row.get(1) : null;
                  assertThat(currentMacroName).as("You have to specified a macro name").isNotNull();
                  logger.info("Starting reading " + currentMacroName);
                  currentMacroContent = new ArrayList<List<String>>();
               }
            }
         }
      }

   }

   // MODE INTERACTIF
   private static final Class<?>[] baseList = {String.class, Integer.class, int.class, Class.class};

   protected CsvRunner csvRunner;

   private MacroReader macroReader;

   private DirectoryTestReader reader;

   private final NodeObjectFinder rootObjectFinder = new NodeObjectFinder() {

      public Object find(Node node) {
         return csvRunner.getNodeValue(RootPanel.get(), node);
      }
   };

   public GwtCsvTest() {
      setCanDispatchEventsOnDetachedWidgets(false);
   }

   @CsvMethod
   public void assertBiggerThan(String value, String... params) {
      GwtInstance gwtInstance = object(params);
      Object actual = gwtInstance.getRaw();

      if (actual == null) {
         fail(prefix() + "number is null: " + gwtInstance.identifierToString());
      } else if (actual instanceof Integer) {
         assertThat((Integer) actual).as(prefix() + "Integer").isGreaterThan(
                  Integer.parseInt(value));
      } else if (actual instanceof Long) {
         assertThat((Long) actual).as(prefix() + "Long").isGreaterThan(Long.parseLong(value));
      } else if (actual instanceof Double) {
         assertThat((Double) actual).as(prefix() + "Double").isGreaterThan(
                  Double.parseDouble(value));
      } else if (actual instanceof Float) {
         assertThat((Float) actual).as(prefix() + "Float").isGreaterThan(Float.parseFloat(value));
      } else {
         fail(prefix() + "cannot compare object of type " + actual.getClass().getName() + " to <"
                  + value + ">");
      }
   }

   /**
    * 
    * @param containedValue
    * @param params
    */
   @CsvMethod
   public void assertContains(String containedValue, String... params) {
      containedValue = GwtStringUtils.resolveBackSlash(containedValue);
      String actual = getString(params);
      assertThat(actual).as(prefix() + "String").contains(containedValue);
   }

   @CsvMethod
   public void assertEmpty(String... params) {
      assertExact("", params);
   }

   /**
    * 
    * @param expected
    * @param params
    */
   @CsvMethod
   public void assertExact(String expected, String... params) {
      expected = GwtStringUtils.resolveBackSlash(expected);

      String actual = getString(params);

      if (expected == null) {
         assertThat(actual).as(prefix() + "String").isNull();
      } else {
         assertThat(actual).as(prefix() + "String").isEqualTo(expected);
      }

   }

   @CsvMethod
   public void assertFalse(String... params) {
      assertThat(object(params).ofType(Boolean.class)).as(prefix() + "Boolean").isNotNull().isFalse();
   }

   @CsvMethod
   public void assertHTML(String html, String... params) {
      assertThat(object(params).ofType(UIObject.class)).withPrefix(prefix()).htmlEquals(html);
   }

   @CsvMethod
   public void assertInstanceOf(String className, String... params) {
      try {
         Class<?> clazz = GwtReflectionUtils.getClass(className);
         assertThat(object(params)).withPrefix(prefix()).isInstanceOf(clazz);
      } catch (ClassNotFoundException e) {
         fail(prefix() + "Cannot assert instance of [" + className
                  + "] because the class cannot be found");
      }
   }

   @CsvMethod
   public void assertListBoxDataEquals(String commaSeparatedContent, String... params) {
      String[] content = commaSeparatedContent.split("\\s*,\\s*");
      assertThat(object(params).ofType(ListBox.class)).withPrefix(prefix()).dataMatches(content);
   }

   @CsvMethod
   public void assertListBoxSelectedValueIs(String expected, String... params) {
      assertThat(object(params).ofType(ListBox.class)).withPrefix(prefix()).selectedValueEquals(
               expected);
   }

   @CsvMethod
   public void assertNotExist(String... params) {
      assertThat(object(params)).withPrefix(prefix()).isNull();
   }

   @CsvMethod
   public void assertNumberExact(String value, String... params) {
      GwtInstance gwtInstance = object(params);
      Object actual = gwtInstance.getRaw();

      if (actual == null) {
         fail(prefix() + "number is null: " + gwtInstance.identifierToString());
      } else if (actual instanceof Integer) {
         assertThat((Integer) actual).as(prefix() + "Integer").isEqualTo(Integer.parseInt(value));
      } else if (actual instanceof Long) {
         assertThat((Long) actual).as(prefix() + "Long").isEqualTo(Long.parseLong(value));
      } else if (actual instanceof Double) {
         assertThat((Double) actual).as(prefix() + "Double").isEqualTo(Double.parseDouble(value));
      } else if (actual instanceof Float) {
         assertThat((Float) actual).as(prefix() + "Float").isEqualTo(Float.parseFloat(value));
      } else {
         fail(prefix() + "cannot compare object of type " + actual.getClass().getName() + " to <"
                  + value + ">");
      }
   }

   @CsvMethod
   public void assertSmallerThan(String value, String... params) {
      GwtInstance gwtInstance = object(params);
      Object actual = gwtInstance.getRaw();

      if (actual == null) {
         fail(prefix() + "number is null: " + gwtInstance.identifierToString());
      } else if (actual instanceof Integer) {
         assertThat((Integer) actual).as(prefix() + "Integer").isLessThan(Integer.parseInt(value));
      } else if (actual instanceof Long) {
         assertThat((Long) actual).as(prefix() + "Long").isLessThan(Long.parseLong(value));
      } else if (actual instanceof Double) {
         assertThat((Double) actual).as(prefix() + "Double").isLessThan(Double.parseDouble(value));
      } else if (actual instanceof Float) {
         assertThat((Float) actual).as(prefix() + "Float").isLessThan(Float.parseFloat(value));
      } else {
         fail(prefix() + "cannot compare object of type " + actual.getClass().getName() + " to <"
                  + value + ">");
      }
   }

   @CsvMethod
   public void assertText(String text, String... params) {
      assertThat(object(params).ofType(UIObject.class)).withPrefix(prefix()).textEquals(text);
   }

   @CsvMethod
   public void assertTrue(String... params) {
      assertThat(object(params).ofType(Boolean.class)).as(prefix() + "Boolean").isNotNull().isTrue();
   }

   @CsvMethod
   public void blur(String... params) {
      Browser.blur(object(params).ofType(Widget.class));
   }

   @CsvMethod
   public void callMethod(String... params) {
      object(params);
   }

   @CsvMethod
   public void change(String... params) {
      Browser.change(object(params).ofType(Widget.class));
   }

   @CsvMethod
   public void click(String... params) {
      Browser.click(object(params).ofType(Widget.class));
   }

   @CsvMethod
   public void clickComplexPanel(String index, String... params) {
      Browser.click(object(params).ofType(ComplexPanel.class), Integer.parseInt(index));
   }

   @CsvMethod
   public void clickGridCell(String rowIndex, String columnIndex, String... params) {
      int row = Integer.parseInt(rowIndex);
      int column = Integer.parseInt(columnIndex);
      Browser.click(object(params).ofType(Grid.class), row, column);
   }

   @CsvMethod
   public void clickMenuItem(String index, String... params) {
      Browser.click(object(params).ofType(MenuBar.class), Integer.parseInt(index));
   }

   @CsvMethod
   public void emptyTextBox(String... params) {
      Browser.emptyText(object(params).ofType(TextBox.class));
   }

   @CsvMethod
   public void fillAndSelectInSuggestBoxByIndex(String content, String index, String... params) {
      SuggestBox suggestBox = object(params).ofType(SuggestBox.class);

      Browser.fillText(suggestBox, content);
      Browser.click(suggestBox, Integer.parseInt(index));
   }

   @CsvMethod
   public void fillAndSelectInSuggestBoxByText(String content, String selected, String... params) {
      SuggestBox suggestBox = object(params).ofType(SuggestBox.class);
      Browser.fillText(suggestBox, content);

      List<MenuItem> menuItems = WidgetUtils.getMenuItems(suggestBox);
      int i = 0;
      int index = -1;

      while (i < menuItems.size() && index == -1) {
         MenuItem item = menuItems.get(i);
         if (selected.equals(item.getHTML()) || selected.equals(item.getText())) {
            index = i;
            Browser.click(suggestBox, item);
         }
         i++;
      }

      assertThat(index).as(prefix() + "Cannot find '" + selected + "' in suggested choices").isGreaterThan(
               -1);
   }

   @CsvMethod
   public void fillInvisibleTextBox(String value, String... params) {
      Browser.fillText(object(params).ofType(TextBox.class), false, value);
   }

   @CsvMethod
   public void fillTextBox(String value, String... params) {
      Browser.fillText(object(params).ofType(TextBox.class), value);
   }

   @CsvMethod
   public void focus(String... params) {
      Browser.focus(object(params).ofType(Widget.class));
   }

   @CsvMethod
   public void hasStyle(String style, String... params) {
      assertThat(object(params).ofType(UIObject.class)).withPrefix(prefix()).hasStyle(style);
   }

   @CsvMethod
   public void interactive() {
      try {
         PrintStream os = System.out;
         BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
         os.println("Welcome in interactive mode !");
         while (true) {
            os.print("> ");
            os.flush();
            String line = br.readLine();
            if ("quit".equals(line) || "q".equals(line)) {
               os.println("Bye bye !");
               return;
            }
            int index = line.indexOf(" ");
            if (index == -1) {
               os.println("Parse error");
            } else {
               String cmd = line.substring(0, index);
               String param = line.substring(index + 1);
               os.println("Command <" + cmd + ">, params : " + param);
               if ("go".equals(cmd) || "getObject".equals(cmd)) {
                  try {
                     getObject(param, os);
                  } catch (Throwable e) {
                     os.println("Not found : " + e.getMessage());
                  }
               } else if ("lc".equals(cmd) || "listContent".equals(cmd)) {
                  try {
                     Object o = getObject(param, os);
                     printGetter(o, o.getClass(), os);
                  } catch (Throwable e) {
                     os.println("Not found : " + e.getMessage());
                  }
               } else if ("click".equals(cmd)) {
                  try {
                     click(param);
                     os.println("Click successful");
                  } catch (Throwable e) {
                     os.println("Unable to click : " + e.getMessage());
                  }
               } else {
                  os.println("Unknown command : " + cmd);
               }
            }
         }
      } catch (IOException e) {
         Fail.fail("IO error " + e.toString());
      }
   }

   @CsvMethod
   public void isChecked(String... params) {
      assertThat(object(params).ofType(CheckBox.class)).withPrefix(prefix()).isChecked();
   }

   @CsvMethod
   public void isEnabled(String... params) {
      assertThat(getFocusWidget(params)).withPrefix(prefix()).isEnabled();
   }

   @CsvMethod
   public void isNotChecked(String... params) {
      assertThat(object(params).ofType(CheckBox.class)).withPrefix(prefix()).isNotChecked();
   }

   @CsvMethod
   public void isNotEnabled(String... params) {
      assertThat(getFocusWidget(params)).withPrefix(prefix()).isNotEnabled();
   }

   @CsvMethod
   public void isNotVisible(String... params) {
      GwtInstance gwtInstance = object(params);
      Object actual = gwtInstance.getRaw();

      if (actual == null || !UIObject.class.isInstance(actual)) {
         return;
      }

      UIObject object = (UIObject) actual;

      boolean visible = false;
      visible = WidgetUtils.isWidgetVisible(object);
      if (visible && object instanceof Widget) {
         Widget w = (Widget) object;
         visible = w.isAttached();
      }
      assertThat(visible).overridingErrorMessage("%s [%s] should not be visible", prefix(),
               object.getClass().getSimpleName()).isFalse();
   }

   @CsvMethod
   public void isVisible(String... params) {
      UIObject uiObject = object(params).ofType(UIObject.class);

      assertThat(uiObject).withPrefix(prefix()).isVisible();

      if (!GwtConfig.get().getModuleRunner().canDispatchEventsOnDetachedWidgets()
               && Widget.class.isInstance(uiObject)) {

         assertThat((Widget) uiObject).withPrefix(prefix()).isAttached();
      }
   }

   public void launchTest(String testName) throws Exception {
      csvRunner.runSheet(reader.getTest(testName), this);
   }

   @CsvMethod
   public void mouseDown(String... params) {
      Browser.mouseDown(object(params).ofType(Widget.class));
   }

   @CsvMethod
   public void mouseMove(String... params) {
      Browser.mouseMove(object(params).ofType(Widget.class));
   }

   @CsvMethod
   public void mouseOut(String... params) {
      Browser.mouseOut(object(params).ofType(Widget.class));
   }

   @CsvMethod
   public void mouseOver(String... params) {
      Browser.mouseOver(object(params).ofType(Widget.class));
   }

   @CsvMethod
   public void mouseUp(String... params) {
      Browser.mouseUp(object(params).ofType(Widget.class));
   }

   @CsvMethod
   public void mouseWheel(String... params) {
      Browser.mouseWheel(object(params).ofType(Widget.class));
   }

   @CsvMethod
   public void runmacro(String macroName, String... params) throws Exception {
      List<List<String>> macro = macroReader.getMacro(macroName);
      assertThat(macro).as(prefix() + "CsvMacro '" + macroName + "' has not been found").isNotNull();
      int i = 0;
      for (List<String> line : macro) {
         List<String> l = new ArrayList<String>();
         for (String s : line) {
            String replaced = s;
            for (int z = 0; z < params.length; z++) {
               String param = params[z];
               if (param == null)
                  param = "*null*";
               else if ("".equals(param))
                  param = "*empty*";

               replaced = replaced.replaceAll("\\{" + z + "\\}", param);
            }
            l.add(replaced);
         }
         csvRunner.setExtendedLineInfo(macroName + " line " + (i + 1));
         csvRunner.executeRow(l, this);
         i++;
      }
      csvRunner.setExtendedLineInfo(null);
   }

   @CsvMethod
   public void selectInListBox(String value, String... params) {
      selectInListBox(object(params).ofType(ListBox.class), value, params);
   }

   @CsvMethod
   public void selectInListBoxByIndex(String index, String... params) {
      selectInListBox(index, params);
   }

   @CsvMethod
   public void selectInListBoxByText(String regex, String... params) {
      selectInListBox(regex, params);
   }

   public void setReader(DirectoryTestReader reader) {
      this.reader = reader;
      csvRunner = new CsvRunner();
      macroReader = new MacroReader();
      for (String name : reader.getMacroFileList()) {
         macroReader.read(reader.getMacroFile(name));
      }

      GwtFinder.registerNodeFinder("root", rootObjectFinder);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.googlecode.gwt.test.GwtModuleRunnerAdapter#getDefaultBrowserErrorHandler ()
    */
   @Override
   protected BrowserErrorHandler getDefaultBrowserErrorHandler() {
      return new BrowserErrorHandler() {

         public void onError(String errorMessage) {
            Fail.fail(prefix() + errorMessage);
         }
      };
   }

   protected FocusWidget getFocusWidget(String... params) {
      return getFocusWidget(params);
   }

   /**
    * 
    * @param clazz
    * @param failOnError
    * @param params
    * @return
    * 
    * @deprecated use {@link GwtFinder#object(String...)} instead
    */
   @Deprecated
   protected <T> T getObject(Class<T> clazz, boolean failOnError, String... params) {
      return csvRunner.getObject(clazz, failOnError, params);
   }

   /**
    * 
    * @param clazz
    * @param params
    * @return
    * 
    * @deprecated use {@link GwtFinder#object(String...)} instead
    */
   @Deprecated
   protected <T> T getObject(Class<T> clazz, String... params) {
      return csvRunner.getObject(clazz, params);
   }

   protected String getString(Object o) {
      String actualValue;
      if (o == null) {
         return null;
      } else if (HasHTML.class.isInstance(o)) {
         HasHTML hasHTML = (HasHTML) o;
         String html = hasHTML.getHTML();
         actualValue = (html != null && html.length() > 0) ? html : hasHTML.getText();
      } else if (HasText.class.isInstance(o)) {
         actualValue = ((HasText) o).getText();
      } else {
         actualValue = "" + o;
      }

      return actualValue;
   }

   protected String getString(String... params) {
      return getString(object(params).getRaw());
   }

   protected WidgetVisitor getWidgetVisitor() {
      return new DefaultWidgetVisitor();
   }

   protected String prefix() {
      return csvRunner.getAssertionErrorMessagePrefix();
   }

   protected void selectInListBox(ListBox listBox, String regex, String... params) {
      int selectedIndex;
      String errorMessage;
      if (regex.matches("^\\d*$")) {
         selectedIndex = Integer.parseInt(regex);
         errorMessage = "Cannot select negative index in ListBox <" + regex
                  + "> in ListBox with values : ";
      } else {
         selectedIndex = WidgetUtils.getIndexInListBox(listBox, regex);
         errorMessage = "Regex <" + regex + "> has not been matched in ListBox values : ";
      }

      if (selectedIndex > -1) {
         listBox.setSelectedIndex(selectedIndex);
         Browser.click(listBox);
         Browser.change(listBox);
      } else {
         errorMessage += WidgetUtils.getListBoxContentToString(listBox);
         Fail.fail(prefix() + errorMessage);
      }
   }

   private Object getObject(String param, PrintStream os) {
      Object o = object(param).ofType(Object.class);
      os.println("Object found, class " + o.getClass().getCanonicalName());
      if (ArrayUtils.contains(baseList, o.getClass())) {
         os.println("Value : " + o.toString());
      }
      return o;
   }

   private void printGetter(Object o, Class<?> clazz, PrintStream os) {
      for (Method m : clazz.getDeclaredMethods()) {
         if (m.getName().startsWith("get")) {
            os.print("Getter [" + clazz.getSimpleName() + "] " + m.getName());
            if (ArrayUtils.contains(baseList, m.getReturnType())
                     && m.getParameterTypes().length == 0) {
               try {
                  Object res = m.invoke(o);
                  os.print(", value " + res);
               } catch (Throwable e) {
               }
            }
            os.println();
         }
      }
      for (Field f : clazz.getDeclaredFields()) {
         if (!Modifier.isStatic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers())) {
            os.print("Field [" + clazz.getSimpleName() + "] [" + f.getClass().getSimpleName()
                     + "] " + f.getName());
            if (ArrayUtils.contains(baseList, f.getType())) {
               try {
                  Object res = f.get(o);
                  os.print(", value " + res);
               } catch (Throwable e) {
               }
            }
            os.println();
         }
      }
      if (clazz.getSuperclass() != null) {
         printGetter(o, clazz.getSuperclass(), os);
      }
   }

}

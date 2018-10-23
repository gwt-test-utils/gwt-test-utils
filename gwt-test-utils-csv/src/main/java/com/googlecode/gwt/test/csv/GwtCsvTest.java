package com.googlecode.gwt.test.csv;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.user.client.ui.*;
import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.csv.internal.CsvTestsProvider;
import com.googlecode.gwt.test.csv.runner.CsvRunner;
import com.googlecode.gwt.test.csv.runner.CsvTestExecutionHandler;
import com.googlecode.gwt.test.csv.runner.HasCsvTestExecutionHandlers;
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
import org.assertj.core.api.Fail;
import org.junit.After;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.googlecode.gwt.test.assertions.GwtAssertions.assertThat;
import static com.googlecode.gwt.test.finder.GwtFinder.object;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

@RunWith(GwtCsvRunner.class)
public abstract class GwtCsvTest extends GwtTest implements HasCsvTestExecutionHandlers {

    private static class MacroReader {

        private final static Logger logger = LoggerFactory.getLogger(MacroReader.class);

        private final HashMap<String, List<List<String>>> macroList;

        public MacroReader() {
            macroList = new HashMap<>();
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
                        currentMacroContent = new ArrayList<>();
                    }
                }
            }
        }

    }

    // MODE INTERACTIF
    private static final Class<?>[] baseList = {String.class, Integer.class, int.class, Class.class};

    private CsvRunner csvRunner;

    private final List<CsvTestExecutionHandler> csvTestExecutionHandlers = new ArrayList<>();

    private MacroReader macroReader;

    private CsvTestsProvider csvTestsProvider;

    private final NodeObjectFinder rootObjectFinder = new NodeObjectFinder() {

        public Object find(Node node) {
            return csvRunner.getNodeValue(RootPanel.get(), node);
        }
    };

    public GwtCsvTest() {
        setCanDispatchEventsOnDetachedWidgets(false);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.googlecode.gwt.test.csv.runner.HasCsvTestExecutionHandlers#getCsvTestExecutionHandlers()
     */
    public List<CsvTestExecutionHandler> getCsvTestExecutionHandlers() {
        return Collections.unmodifiableList(csvTestExecutionHandlers);
    }

    @After
    public final void afterGwtCsvTest() {
        csvTestExecutionHandlers.clear();
    }

    @CsvMethod
    public void assertBiggerThan(String value, String... identifier) {
        GwtInstance gwtInstance = object(identifier);
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
     * @param containedValue
     * @param identifier
     */
    @CsvMethod
    public void assertContains(String containedValue, String... identifier) {
        containedValue = GwtStringUtils.resolveBackSlash(containedValue);
        String actual = getString(identifier);
        assertThat(actual).as(prefix() + "String").contains(containedValue);
    }

    @CsvMethod
    public void assertEmpty(String... identifier) {
        assertExact("", identifier);
    }

    /**
     * @param expected
     * @param identifier
     */
    @CsvMethod
    public void assertExact(String expected, String... identifier) {
        expected = GwtStringUtils.resolveBackSlash(expected);

        String actual = getString(identifier);

        if (expected == null) {
            assertThat(actual).as(prefix() + "String").isNull();
        } else {
            assertThat(actual).as(prefix() + "String").isEqualTo(expected);
        }

    }

    @CsvMethod
    public void assertFalse(String... identifier) {
        assertThat(object(identifier).ofType(Boolean.class)).as(prefix() + "Boolean").isNotNull().isFalse();
    }

    @CsvMethod
    public void assertHTML(String html, String... identifier) {
        assertThat(object(identifier).ofType(UIObject.class)).withPrefix(prefix()).htmlEquals(html);
    }

    @CsvMethod
    public void assertInstanceOf(String className, String... identifier) {
        try {
            Class<?> clazz = GwtReflectionUtils.getClass(className);
            assertThat(object(identifier)).withPrefix(prefix()).isInstanceOf(clazz);
        } catch (ClassNotFoundException e) {
            fail(prefix() + "Cannot assert instance of [" + className
                    + "] because the class cannot be found");
        }
    }

    @CsvMethod
    public void assertListBoxDataEquals(String commaSeparatedContent, String... identifier) {
        String[] content = commaSeparatedContent.split("\\s*,\\s*");
        assertThat(object(identifier).ofType(ListBox.class)).withPrefix(prefix()).dataMatches(content);
    }

    @CsvMethod
    public void assertListBoxSelectedValueIs(String expected, String... identifier) {
        assertThat(object(identifier).ofType(ListBox.class)).withPrefix(prefix()).selectedValueEquals(
                expected);
    }

    @CsvMethod
    public void assertNotExist(String... identifier) {
        assertThat(object(identifier)).withPrefix(prefix()).isNull();
    }

    @CsvMethod
    public void assertNumberExact(String value, String... identifier) {
        GwtInstance gwtInstance = object(identifier);
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
    public void assertSmallerThan(String value, String... identifier) {
        GwtInstance gwtInstance = object(identifier);
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
    public void assertText(String text, String... identifier) {
        Object raw = object(identifier).getRaw();
        if (raw instanceof UIObject) {
            assertThat((UIObject) raw).withPrefix(prefix()).textEquals(text);
        } else if (raw instanceof Element) {
            assertThat((Element) raw).withPrefix(prefix()).textEquals(text);
        }
    }

    @CsvMethod
    public void assertTrue(String... identifier) {
        assertThat(object(identifier).ofType(Boolean.class)).as(prefix() + "Boolean").isNotNull().isTrue();
    }

    @CsvMethod
    public void blur(String... identifier) {
        Browser.blur(object(identifier).ofType(Widget.class));
    }

    @CsvMethod
    public void callMethod(String... identifier) {
        object(identifier);
    }

    @CsvMethod
    public void change(String... identifier) {
        Browser.change(object(identifier).ofType(Widget.class));
    }

    @CsvMethod
    public void click(String... identifier) {
        Object object = object(identifier).ofType(Object.class);
        if (object instanceof CheckBox) {
            Widget widget = (Widget) object;
            Browser.click(widget, widget.getElement().getFirstChildElement());
        } else if (object instanceof Widget) {
            Widget widget = (Widget) object;
            if (identifier.length == 1) {
                Element element = Document.get().getElementById(identifier[0]);
                Browser.click(widget, element);
            } else {
                Browser.click(widget);
            }
        } else if (object instanceof Element) {
            Element element = (Element) object;
            Browser.click(element);
            if (element.getTagName().equalsIgnoreCase("label")) {
                LabelElement labelElement = (LabelElement) element;
                String targetIdentifierForLabel = labelElement.getHtmlFor();
                Element target = object(targetIdentifierForLabel).ofType(Element.class);
                Browser.click(target);
            }
        }
    }

    @CsvMethod
    public void clickComplexPanel(String index, String... identifier) {
        Browser.click(object(identifier).ofType(ComplexPanel.class), Integer.parseInt(index));
    }

    @CsvMethod
    public void clickGridCell(String rowIndex, String columnIndex, String... identifier) {
        int row = Integer.parseInt(rowIndex);
        int column = Integer.parseInt(columnIndex);
        Browser.click(object(identifier).ofType(Grid.class), row, column);
    }

    @CsvMethod
    public void clickMenuItem(String index, String... identifier) {
        Browser.click(object(identifier).ofType(MenuBar.class), Integer.parseInt(index));
    }

    @CsvMethod
    public void emptyTextBox(String... identifier) {
        Browser.emptyText(object(identifier).ofType(TextBox.class));
    }

    @CsvMethod
    public void fillAndSelectInSuggestBoxByIndex(String content, String index, String... identifier) {
        SuggestBox suggestBox = object(identifier).ofType(SuggestBox.class);

        Browser.fillText(content, suggestBox);
        assertThat(WidgetUtils.isShowingItems(suggestBox)).describedAs(prefix()).isTrue();
        Browser.click(suggestBox, Integer.parseInt(index));
    }

    @CsvMethod
    public void fillAndSelectInSuggestBoxByText(String content, String selected,
                                                String... identifier) {
        SuggestBox suggestBox = object(identifier).ofType(SuggestBox.class);

        Browser.fillText(content, suggestBox);
        assertThat(WidgetUtils.isShowingItems(suggestBox)).isTrue();

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
    public void fillInvisibleTextBox(String value, String... identifier) {
        Browser.fillText(value, false, identifier);
    }

    @CsvMethod
    public void fillTextBox(String value, String... identifier) {
        Browser.fillText(value, identifier);
    }

    @CsvMethod
    public void focus(String... identifier) {
        Browser.focus(object(identifier).ofType(Widget.class));
    }

    @CsvMethod
    public void hasStyle(String style, String... identifier) {
        Object raw = object(identifier).getRaw();
        if (raw instanceof UIObject) {
            assertThat((UIObject) raw).withPrefix(prefix()).hasStyle(style);
        } else if (raw instanceof Element) {
            assertThat((Element) raw).withPrefix(prefix()).hasStyle(style);
        }
    }

    @CsvMethod
    public void doesNotHaveStyle(String style, String... identifier) {
        Object raw = object(identifier).getRaw();
        if (raw instanceof UIObject) {
            assertThat((UIObject) raw).withPrefix(prefix()).doesNotHaveStyle(style);
        } else if (raw instanceof Element) {
            assertThat((Element) raw).withPrefix(prefix()).doesNotHaveStyle(style);
        }
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
                    os.println("Command <" + cmd + ">, identifier : " + param);
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
    public void isChecked(String... identifier) {
        assertThat(object(identifier).ofType(CheckBox.class)).withPrefix(prefix()).isChecked();
    }

    @CsvMethod
    public void isEnabled(String... identifier) {
        assertThat(getFocusWidget(identifier)).withPrefix(prefix()).isEnabled();
    }

    @CsvMethod
    public void isNotChecked(String... identifier) {
        assertThat(object(identifier).ofType(CheckBox.class)).withPrefix(prefix()).isNotChecked();
    }

    @CsvMethod
    public void isNotEnabled(String... identifier) {
        assertThat(getFocusWidget(identifier)).withPrefix(prefix()).isNotEnabled();
    }

    @CsvMethod
    public void isNotVisible(String... identifier) {
        GwtInstance gwtInstance = object(identifier);
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
    public void isVisible(String... identifier) {
        UIObject uiObject = object(identifier).ofType(UIObject.class);

        assertThat(uiObject).withPrefix(prefix()).isVisible();

        if (!GwtConfig.get().getModuleRunner().canDispatchEventsOnDetachedWidgets()
                && Widget.class.isInstance(uiObject)) {

            assertThat((Widget) uiObject).withPrefix(prefix()).isAttached();
        }
    }

    @CsvMethod
    public void mouseDown(String... identifier) {
        Browser.mouseDown(object(identifier).ofType(Widget.class));
    }

    @CsvMethod
    public void mouseMove(String... identifier) {
        Browser.mouseMove(object(identifier).ofType(Widget.class));
    }

    @CsvMethod
    public void mouseOut(String... identifier) {
        Browser.mouseOut(object(identifier).ofType(Widget.class));
    }

    @CsvMethod
    public void mouseOver(String... identifier) {
        Browser.mouseOver(object(identifier).ofType(Widget.class));
    }

    @CsvMethod
    public void mouseUp(String... identifier) {
        Browser.mouseUp(object(identifier).ofType(Widget.class));
    }

    @CsvMethod
    public void mouseWheel(String... identifier) {
        Browser.mouseWheel(object(identifier).ofType(Widget.class));
    }

    @CsvMethod
    public void runmacro(String macroName, String... identifier) throws Exception {
        List<List<String>> macro = macroReader.getMacro(macroName);
        assertThat(macro).as(prefix() + "CsvMacro '" + macroName + "' has not been found").isNotNull();
        int i = 0;
        for (List<String> line : macro) {
            List<String> l = new ArrayList<>();
            for (String s : line) {
                String replaced = s;
                for (int z = 0; z < identifier.length; z++) {
                    String param = identifier[z];
                    if (param == null) {
                        param = "*null*";
                    } else if ("".equals(param)) {
                        param = "*empty*";
                    }

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
    public void selectInListBox(String value, String... identifier) {
        selectInListBox(object(identifier).ofType(ListBox.class), value);
    }

    @CsvMethod
    public void selectInListBoxByIndex(String index, String... identifier) {
        selectInListBox(index, identifier);
    }

    @CsvMethod
    public void selectInListBoxByText(String regex, String... identifier) {
        selectInListBox(regex, identifier);
    }

    public void setReader(CsvTestsProvider csvTestsProvider) {
        this.csvTestsProvider = csvTestsProvider;
        macroReader = new MacroReader();
        for (String name : csvTestsProvider.getMacroFileList()) {
            macroReader.read(csvTestsProvider.getMacroFile(name));
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
        return errorMessage -> Fail.fail(prefix() + errorMessage);
    }

    protected CsvRunner getCsvRunner() {
        return csvRunner;
    }

    protected FocusWidget getFocusWidget(String... identifier) {
        return object(identifier).ofType(FocusWidget.class);
    }

    protected String getString(Object o) {
        String actualValue;
        if (o == null) {
            return null;
        } else if (HasHTML.class.isInstance(o)) {
            HasHTML hasHTML = (HasHTML) o;
            String html = hasHTML.getHTML();
            actualValue = html != null && html.length() > 0 ? html : hasHTML.getText();
        } else if (HasText.class.isInstance(o)) {
            actualValue = ((HasText) o).getText();
        } else {
            actualValue = "" + o;
        }

        return actualValue;
    }

    protected String getString(String... identifier) {
        return getString(object(identifier).getRaw());
    }

    protected WidgetVisitor getWidgetVisitor() {
        return new DefaultWidgetVisitor();
    }

    /**
     * Called by generated JUnit test class.
     *
     * @param csvTestFileAbsolutePath absolute path of current CSV test file.
     * @throws Exception
     */
    protected final void launchTest(String csvTestFileAbsolutePath) throws Exception {
        csvRunner = new CsvRunner(this, csvTestsProvider.getTestName(csvTestFileAbsolutePath));


        File testFile = new File(URLDecoder.decode(csvTestFileAbsolutePath, "utf-8"));
        for (CsvTestExecutionHandler handler : getCsvTestExecutionHandlers()) {
            handler.beforeCsvTestExecution(testFile);
        }

        csvRunner.runSheet(csvTestsProvider.getTest(csvTestFileAbsolutePath), this);

        for (CsvTestExecutionHandler handler : getCsvTestExecutionHandlers()) {
            handler.afterCsvTestExecution(testFile);
        }
    }

    protected String prefix() {
        return csvRunner.getAssertionErrorMessagePrefix();
    }

    protected final void registerCsvInvocationHandler(
            CsvTestExecutionHandler csvMethodInvocationHandler) {
        csvTestExecutionHandlers.add(csvMethodInvocationHandler);
    }

    protected void selectInListBox(ListBox listBox, String regex) {
        int selectedIndex;
        String errorMessage = "";
        selectedIndex = WidgetUtils.getIndexInListBox(listBox, regex);
        if (selectedIndex == -1) {
            if (regex.matches("^\\d*$")) {
                selectedIndex = Integer.parseInt(regex);
                errorMessage = "Cannot select negative index in ListBox <" + regex + "> in ListBox with values : ";
            } else {
                errorMessage = "Regex <" + regex + "> has not been matched in ListBox values : ";
            }
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

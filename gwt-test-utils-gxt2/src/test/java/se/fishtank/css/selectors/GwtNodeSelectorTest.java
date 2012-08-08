package se.fishtank.css.selectors;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.googlecode.gwt.test.gxt2.GwtGxtTest;

public class GwtNodeSelectorTest extends GwtGxtTest {

   private static Logger LOGGER = LoggerFactory.getLogger(GwtNodeSelectorTest.class);

   private static final Map<String, Integer> testDataMap = new LinkedHashMap<String, Integer>();

   static {
      testDataMap.put("*", 251);
      testDataMap.put(":root", 1);
      testDataMap.put(":empty", 3);
      testDataMap.put("div:first-child", 51);
      testDataMap.put("div:nth-child(even)", 106);
      testDataMap.put("div:nth-child(2n)", 106);
      testDataMap.put("div:nth-child(odd)", 137);
      testDataMap.put("div:nth-child(2n+1)", 137);
      testDataMap.put("div:nth-child(n)", 243);
      testDataMap.put("script:first-of-type", 1);
      testDataMap.put("div:last-child", 53);
      testDataMap.put("script:last-of-type", 1);
      testDataMap.put("script:nth-last-child(odd)", 1);
      testDataMap.put("script:nth-last-child(even)", 1);
      testDataMap.put("script:nth-last-child(5)", 0);
      testDataMap.put("script:nth-of-type(2)", 1);
      testDataMap.put("script:nth-last-of-type(n)", 2);
      testDataMap.put("div:only-child", 22);
      testDataMap.put("meta:only-of-type", 1);
      testDataMap.put("div > div", 242);
      testDataMap.put("div + div", 190);
      testDataMap.put("div ~ div", 190);
      testDataMap.put("body", 1);
      testDataMap.put("body div", 243);
      testDataMap.put("div", 243);
      testDataMap.put("div div", 242);
      testDataMap.put("div div div", 241);
      testDataMap.put("div, div, div", 243);
      testDataMap.put("div, a, span", 243);
      testDataMap.put(".dialog", 51);
      testDataMap.put("div.dialog", 51);
      testDataMap.put("div .dialog", 51);
      testDataMap.put("div.character, div.dialog", 99);
      testDataMap.put("#speech5", 1);
      testDataMap.put("div#speech5", 1);
      testDataMap.put("div #speech5", 1);
      testDataMap.put("div.scene div.dialog", 49);
      testDataMap.put("div#scene1 div.dialog div", 142);
      testDataMap.put("#scene1 #speech1", 1);
      testDataMap.put("div[class]", 103);
      testDataMap.put("div[class=dialog]", 50);
      testDataMap.put("div[class^=dia]", 51);
      testDataMap.put("div[class$=log]", 50);
      testDataMap.put("div[class*=sce]", 1);
      testDataMap.put("div[class|=dialog]", 50);
      testDataMap.put("div[class~=dialog]", 51);
      testDataMap.put("head > :not(meta)", 2);
      testDataMap.put("head > :not(:last-child)", 2);
      testDataMap.put("div:not(div.dialog)", 192);
   }

   private GwtNodeSelector nodeSelector;

   @Before
   public void beforeDOMNodeSelectorTest() throws Exception {
      nodeSelector = new GwtNodeSelector(Document.get());
   }

   @Test
   public void root() throws NodeSelectorException {
      Node root = nodeSelector.querySelector(":root");
      assertEquals(Node.ELEMENT_NODE, root.getNodeType());
      assertEquals("HTML", root.getNodeName());

      GwtNodeSelector subSelector = new GwtNodeSelector(nodeSelector.querySelector("div#scene1"));
      Set<Node> subRoot = subSelector.querySelectorAll(":root");
      assertEquals(1, subRoot.size());
      Element subChild = subRoot.iterator().next().cast();
      assertEquals("scene1", subChild.getAttribute("id"));
      assertEquals((int) testDataMap.get("div#scene1 div.dialog div"),
               subSelector.querySelectorAll(":root div.dialog div").size());

      Node meta = nodeSelector.querySelector(":root > head > meta");
      assertEquals(meta, new GwtNodeSelector(meta).querySelector(":root"));
   }

   @Test
   public void selectors() throws Exception {
      for (Map.Entry<String, Integer> entry : testDataMap.entrySet()) {
         LOGGER.info("selector: " + entry.getKey() + ", expected: " + entry.getValue());
         Set<Node> result = nodeSelector.querySelectorAll(entry.getKey());
         assertEquals(entry.getKey(), (int) entry.getValue(), result.size());
      }
   }

}

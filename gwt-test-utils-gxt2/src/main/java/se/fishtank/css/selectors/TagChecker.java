package se.fishtank.css.selectors;

import java.util.LinkedHashSet;
import java.util.Set;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

/**
 * A {@link NodeTraversalChecker} that check if a node matches the
 * {@linkplain Selector#getTagName() tag name} and {@linkplain Selector#getCombinator() combinator}
 * of the {@link Selector} set.
 * 
 * @author Christer Sandberg
 * @author Gael Lazzari
 */
class TagChecker extends NodeTraversalChecker {

   /** The set of nodes to check. */
   private Set<Node> nodes;

   /** The result of the checks. */
   private Set<Node> result;

   /** The selector to check against. */
   private final Selector selector;

   /**
    * Create a new instance.
    * 
    * @param selector The selector to check against.
    */
   public TagChecker(Selector selector) {
      Assert.notNull(selector, "selector is null!");
      this.selector = selector;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Set<Node> check(Set<Node> nodes, Node root) throws NodeSelectorException {
      Assert.notNull(nodes, "nodes is null!");
      this.nodes = nodes;
      result = new LinkedHashSet<Node>();
      switch (selector.getCombinator()) {
         case DESCENDANT:
            addDescentantElements();
            break;
         case CHILD:
            addChildElements();
            break;
         case ADJACENT_SIBLING:
            addAdjacentSiblingElements();
            break;
         case GENERAL_SIBLING:
            addGeneralSiblingElements();
            break;
      }

      return result;
   }

   /**
    * Add adjacent sibling elements.
    * 
    * @see <a href="http://www.w3.org/TR/css3-selectors/#adjacent-sibling-combinators">Adjacent
    *      sibling combinator</a>
    */
   private void addAdjacentSiblingElements() {
      for (Node node : nodes) {
         Node n = GwtDomHelper.getNextSiblingElement(node);
         if (n != null) {
            String tag = selector.getTagName();
            if (tag.equalsIgnoreCase(n.getNodeName()) || tag.equals(Selector.UNIVERSAL_TAG)) {
               result.add(n);
            }
         }
      }
   }

   /**
    * Add child elements.
    * 
    * @see <a href="http://www.w3.org/TR/css3-selectors/#child-combinators">Child combinators</a>
    */
   private void addChildElements() {
      for (Node node : nodes) {
         NodeList<Node> nl = node.getChildNodes();
         for (int i = 0; i < nl.getLength(); i++) {
            node = nl.getItem(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
               continue;
            }

            String tag = selector.getTagName();
            if (tag.equalsIgnoreCase(node.getNodeName()) || tag.equals(Selector.UNIVERSAL_TAG)) {
               result.add(node);
            }
         }
      }
   }

   /**
    * Add descendant elements.
    * 
    * @see <a href="http://www.w3.org/TR/css3-selectors/#descendant-combinators">Descendant
    *      combinator</a>
    * 
    * @throws NodeSelectorException If one of the nodes have an illegal type.
    */
   private void addDescentantElements() throws NodeSelectorException {
      for (Node node : nodes) {
         NodeList<Element> nl;
         if (node.getNodeType() == Node.DOCUMENT_NODE) {
            Document document = node.cast();
            nl = document.getElementsByTagName(selector.getTagName());
         } else if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = node.cast();
            nl = element.getElementsByTagName(selector.getTagName());
         } else {
            throw new NodeSelectorException("Only document and element nodes allowed!");
         }

         for (int i = 0; i < nl.getLength(); i++) {
            result.add(nl.getItem(i));
         }
      }
   }

   /**
    * Add general sibling elements.
    * 
    * @see <a href="http://www.w3.org/TR/css3-selectors/#general-sibling-combinators">General
    *      sibling combinator</a>
    */
   private void addGeneralSiblingElements() {
      for (Node node : nodes) {
         Node n = GwtDomHelper.getNextSiblingElement(node);
         while (n != null) {
            if (selector.getTagName().equalsIgnoreCase(n.getNodeName())
                     || selector.getTagName().equals(Selector.UNIVERSAL_TAG)) {
               result.add(n);
            }

            n = GwtDomHelper.getNextSiblingElement(n);
         }
      }
   }

}

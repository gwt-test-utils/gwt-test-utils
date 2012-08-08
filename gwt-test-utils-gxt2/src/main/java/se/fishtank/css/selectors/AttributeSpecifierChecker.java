package se.fishtank.css.selectors;

import java.util.LinkedHashSet;
import java.util.Set;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;

/**
 * A {@link NodeTraversalChecker} that check if a node's attribute matches the
 * {@linkplain AttributeSpecifier attribute specifier} set.
 * 
 * @author Christer Sandberg
 * @author Gael Lazzari
 */
class AttributeSpecifierChecker extends NodeTraversalChecker {

   /** The attribute specifier to check against. */
   private final AttributeSpecifier specifier;

   /**
    * Create a new instance.
    * 
    * @param specifier The attribute specifier to check against.
    */
   public AttributeSpecifierChecker(AttributeSpecifier specifier) {
      Assert.notNull(specifier, "specifier is null!");
      this.specifier = specifier;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Set<Node> check(Set<Node> nodes, Node root) throws NodeSelectorException {
      Assert.notNull(nodes, "nodes is null!");
      Set<Node> result = new LinkedHashSet<Node>();
      for (Node node : nodes) {
         if (Node.ELEMENT_NODE != node.getNodeType()) {
            continue;
         }

         Element e = node.cast();
         String value = e.getAttribute(specifier.getName());

         if (value == null || "".equals(value))
            continue;

         value = value.trim();

         // It just have to be present.
         if (specifier.getValue() == null) {
            result.add(node);
            continue;
         }

         String val = specifier.getValue();
         switch (specifier.getMatch()) {
            case EXACT:
               if (value.equals(val)) {
                  result.add(node);
               }

               break;
            case HYPHEN:
               if (value.equals(val) || value.startsWith(val + '-')) {
                  result.add(node);
               }

               break;
            case PREFIX:
               if (value.startsWith(val)) {
                  result.add(node);
               }

               break;
            case SUFFIX:
               if (value.endsWith(val)) {
                  result.add(node);
               }

               break;
            case CONTAINS:
               if (value.contains(val)) {
                  result.add(node);
               }

               break;
            case LIST:
               for (String v : value.split("\\s+")) {
                  if (v.equals(val)) {
                     result.add(node);
                  }
               }

               break;
         }
      }

      return result;
   }

}

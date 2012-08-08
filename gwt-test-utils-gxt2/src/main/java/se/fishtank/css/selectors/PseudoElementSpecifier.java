package se.fishtank.css.selectors;

/**
 * An implementation of {@link Specifier} for pseudo-elements.
 * 
 * @see <a href="http://www.w3.org/TR/css3-selectors/#pseudo-elements">Pseudo-elements</a>
 * 
 * @author Christer Sandberg
 */
class PseudoElementSpecifier implements Specifier {

   /** The pseudo-element value. */
   private final String value;

   /**
    * Create a new pseudo-element specifier with the specified value.
    * 
    * @param value The pseudo-element value.
    */
   public PseudoElementSpecifier(String value) {
      Assert.notNull(value, "value is null!");
      this.value = value;
   }

   /**
    * {@inheritDoc}
    */
   public Type getType() {
      return Type.PSEUDO;
   }

   /**
    * Get the pseudo-element value.
    * 
    * @return The pseudo-element value.
    */
   public String getValue() {
      return value;
   }

}

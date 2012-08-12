package com.googlecode.gwt.test.assertions;

import static org.fest.util.Objects.namesOf;

import java.util.Arrays;

/**
 * Assertions for <code>{@link GwtInstance}</code> wrapped object.
 * <p>
 * To create a new instance of this class use the method
 * <code>{@link GwtAssertions#assertThat(GwtInstance))}</code>.
 * </p>
 * 
 * @author Gael Lazzari
 */
public class GwtInstanceAssert extends GwtGenericAssert<GwtInstanceAssert, Object> {

   /**
    * Creates a new </code>{@link GwtInstanceAssert}</code>.
    * 
    * @param actual the wrapper arround the target to verify.
    */
   protected GwtInstanceAssert(GwtInstance instance) {
      super(instance.getInstance(), GwtInstanceAssert.class);
   }

   /**
    * Verifies that the actual {@code Object} is an instance of the given type.
    * 
    * @param type the type to check the actual {@code Object} against.
    * @return this assertion object.
    * @throws AssertionError if the actual {@code Object} is {@code null}.
    * @throws AssertionError if the actual {@code Object} is not an instance of the given type.
    * @throws NullPointerException if the given type is {@code null}.
    */
   @Override
   public GwtInstanceAssert isInstanceOf(Class<?> type) {
      assertNotNull();
      GwtInstance.validateNotNull(type);
      Class<?> current = actual.getClass();
      if (type.isAssignableFrom(current))
         return this;

      throw failWithMessage("expected instance of:<%s> but was instance of:<%s>", type, current);
   }

   /**
    * Verifies that the actual {@code Object} is an instance of any of the given types.
    * 
    * @param types the types to check the actual {@code Object} against.
    * @return this assertion object.
    * @throws AssertionError if the actual {@code Object} is {@code null}.
    * @throws AssertionError if the actual {@code Object} is not an instance of any of the given
    *            types.
    * @throws NullPointerException if the given array of types is {@code null}.
    * @throws NullPointerException if the given array of types contains {@code null}s.
    */
   @Override
   public GwtInstanceAssert isInstanceOfAny(Class<?>... types) {
      isNotNull();
      if (types == null)
         throw new NullPointerException("The given array of types should not be null");
      if (!foundInstanceOfAny(types))
         throw failWithMessage("expected instance of any:<%s> but was instance of:<%s>",
                  typeNames(types), actual.getClass().getName());
      return this;
   }

   private void assertNotNull() {
      if (actual == null) {
         throw failWithMessage("expecting actual value not to be null");
      }
   }

   private boolean foundInstanceOfAny(Class<?>... types) {
      Class<?> current = actual.getClass();
      for (Class<?> type : types) {
         GwtInstance.validateNotNull(type);
         if (type.isAssignableFrom(current))
            return true;
      }
      return false;
   }

   private String typeNames(Class<?>... types) {
      return Arrays.toString(namesOf(types));
   }

}

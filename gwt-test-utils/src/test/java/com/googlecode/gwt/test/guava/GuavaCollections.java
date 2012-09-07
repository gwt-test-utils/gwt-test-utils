package com.googlecode.gwt.test.guava;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class GuavaCollections {

   public static Collection<String> filterIslandCandidates(List<String> names) {

      return Collections2.filter(names, new Predicate<String>() {

         public boolean apply(String input) {
            return input.equals("Jack Shephard") || input.equals("Hurley Reyes");
         }
      });
   }
}

package com.googlecode.gwt.test.guava;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.googlecode.gwt.test.GwtTestTest;

public class GuavaCollectionsTest extends GwtTestTest {

   @Test
   public void filterIslandCandidates() {
      // Arrange
      List<String> list = new ArrayList<String>();
      list.add("Jack Shephard");
      list.add("John Locke");
      list.add("Hurley Reyes");

      // Act
      Collection<String> candidates = GuavaCollections.filterIslandCandidates(list);

      // Assert
      assertThat(candidates).hasSize(2).doesNotContain("John Locke").contains("Jack Shephard").contains(
               "Hurley Reyes");
   }

}

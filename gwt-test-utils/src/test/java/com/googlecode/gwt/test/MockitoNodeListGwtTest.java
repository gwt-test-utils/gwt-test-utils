package com.googlecode.gwt.test;

import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

@GwtModule("com.googlecode.gwt.test.GwtTestUtils")
public class MockitoNodeListGwtTest extends GwtTestWithMockito {

  /**
   * We create a mock object of a class with a method that uses NodeList as parameter.
   *
   * The NodeList has NO generic parameter and we expect the Test to succeed.
   */
  @Test
  public void noGenericsTest() {
    NodeListNoGenericsContainer container = Mockito.mock(NodeListNoGenericsContainer.class);
    assertThat(container).isNotNull();
  }

  /**
   * We create a mock object of a class with a method that uses NodeList as parameter.
   *
   * The NodeList HAS a generic parameter and we expect the Test to succeed, but atm it fails.
   * Only the generic parameter of NodeList is the problem.
   */
  @Test
  public void withGenericsTest() {
    NodeListWithGenericsContainer container = Mockito.mock(NodeListWithGenericsContainer.class);
    assertThat(container).isNotNull();
  }

  private class NodeListWithGenericsContainer {

    public int length(NodeList<Node> list) {
      return list.getLength();
    }

  }

  private class NodeListNoGenericsContainer {

    public int length(NodeList list) {
      return list.getLength();
    }

  }

}

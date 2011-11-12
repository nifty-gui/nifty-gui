package de.lessvoid.nifty.elements;

import junit.framework.TestCase;
import de.lessvoid.xml.tools.MethodResolver;

public class MethodResolverTest extends TestCase {

  public void testEmptyParameters() {
    assertEquals(0, MethodResolver.extractParameters("empty()").length);
  }

  public void testWithOne() {
    String[] result = MethodResolver.extractParameters("empty(one)");
    assertEquals(1, result.length);
    assertEquals("one", result[0]);
  }

  public void testWithThree() {
    String[] result = MethodResolver.extractParameters("empty(one, two, three)");
    assertEquals(3, result.length);
    assertEquals("one", result[0]);
    assertEquals("two", result[1]);
    assertEquals("three", result[2]);
  }
}

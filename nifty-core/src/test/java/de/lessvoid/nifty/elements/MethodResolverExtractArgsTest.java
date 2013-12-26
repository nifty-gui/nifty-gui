package de.lessvoid.nifty.elements;

import de.lessvoid.xml.tools.MethodResolver;
import junit.framework.TestCase;

public class MethodResolverExtractArgsTest extends TestCase {

  public void testEmptyParameters() {
    assertEquals("", MethodResolver.extractArgs("empty()"));
  }

  public void testWithOne() {
    assertEquals("one", MethodResolver.extractArgs("empty(one)"));
  }

  public void testWithThree() {
    assertEquals("one, two, three", MethodResolver.extractArgs("empty(one, two, three)"));
  }
}

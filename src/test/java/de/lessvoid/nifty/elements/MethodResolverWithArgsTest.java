package de.lessvoid.nifty.elements;

import junit.framework.TestCase;
import de.lessvoid.xml.tools.MethodResolver;

public class MethodResolverWithArgsTest extends TestCase {

  public void testMethodClassNull() {
    assertNull(MethodResolver.findMethodWithArgs(null, "methodThatDoesNotExist()"));
  }

  public void testMethodDoesNotExist() {
    assertNull(MethodResolver.findMethodWithArgs(MethodResolverWithArgsTest.class, "methodThatDoesNotExist()"));
  }

  public void testMethodDoesExistNoParamerts() {
    assertNotNull(MethodResolver.findMethodWithArgs(MethodResolverWithArgsTest.class, "methodWithoutParameters()"));
  }

  public void testMethodDoesExistSingleParameter() {
    assertNotNull(MethodResolver.findMethodWithArgs(MethodResolverWithArgsTest.class, "methodWithSingleParameter()", Object.class));
  }

  public void testMethodDoesExistTwoParameters() {
    assertNotNull(MethodResolver.findMethodWithArgs(MethodResolverWithArgsTest.class, "methodWithTwoParameters()", Object.class, String.class));
  }

  public void methodWithoutParameters() {
  }

  public void methodWithSingleParameter(final Object o) {
  }

  public void methodWithTwoParameters(final Object o, final String s) {
  }

}

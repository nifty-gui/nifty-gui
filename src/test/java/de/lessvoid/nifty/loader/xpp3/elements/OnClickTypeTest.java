package de.lessvoid.nifty.loader.xpp3.elements;

import junit.framework.TestCase;

public class OnClickTypeTest extends TestCase {
  
  public void testValid() {
    OnClickType onClick = new OnClickType("quit()");
    assertTrue(onClick.isValid());
  }

  public void testValidWithSingleParam() {
    OnClickType onClick = new OnClickType("quit(2)");
    assertTrue(onClick.isValid());
  }

  public void testValidWithParams() {
    OnClickType onClick = new OnClickType("quit(2,huhu)");
    assertTrue(onClick.isValid());
  }

  public void testInvalid() {
    OnClickType onClick = new OnClickType("quit");
    assertFalse(onClick.isValid());
  }
}

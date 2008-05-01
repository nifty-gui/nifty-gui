package de.lessvoid.nifty.loader.xpp3.elements;

import junit.framework.TestCase;

public class ColorTypeTest extends TestCase {

  public void testInvalid() {
    ColorType color = new ColorType("123");
    assertFalse(color.isValid());
  }

  public void testValidShort() {
    ColorType color = new ColorType("#fff2");
    assertTrue(color.isValid());
  }

  public void testValidLong() {
    ColorType color = new ColorType("#239020ff");
    assertTrue(color.isValid());
  }

  public void testInvalidNull() {
    ColorType color = new ColorType(null);
    assertFalse(color.isValid());
  }
}

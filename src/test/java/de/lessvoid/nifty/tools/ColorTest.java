package de.lessvoid.nifty.tools;

import junit.framework.TestCase;

public class ColorTest extends TestCase {

  public void testMultiply() {
    Color c = new Color(1.0f, 0.5f, 0.6f, 0.8f);
    Color m = c.mutiply(0.5f);
    assertEquals(0.5f, m.getRed());
    assertEquals(0.25f, m.getGreen());
    assertEquals(0.3f, m.getBlue());
    assertEquals(0.4f, m.getAlpha());
  }
}

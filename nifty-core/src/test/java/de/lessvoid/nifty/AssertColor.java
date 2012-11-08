package de.lessvoid.nifty;

import static org.junit.Assert.assertEquals;

public class AssertColor {
  public static void assertColor(final float r, final float g, final float b, final float a, final NiftyColor c) {
    assertEquals(r, c.getRed());
    assertEquals(g, c.getGreen());
    assertEquals(b, c.getBlue());
    assertEquals(a, c.getAlpha());
  }
}

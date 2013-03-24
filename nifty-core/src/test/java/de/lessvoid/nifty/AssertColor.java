package de.lessvoid.nifty;

import static org.junit.Assert.assertEquals;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyMutableColor;

public class AssertColor {
  public static void assertColor(final double r, final double g, final double b, final double a, final NiftyColor c) {
    assertEquals(r, c.getRed());
    assertEquals(g, c.getGreen());
    assertEquals(b, c.getBlue());
    assertEquals(a, c.getAlpha());
  }

  public static void assertColor(final double r, final double g, final double b, final double a, final NiftyMutableColor c) {
    assertEquals(r, c.getRed());
    assertEquals(g, c.getGreen());
    assertEquals(b, c.getBlue());
    assertEquals(a, c.getAlpha());
  }
}

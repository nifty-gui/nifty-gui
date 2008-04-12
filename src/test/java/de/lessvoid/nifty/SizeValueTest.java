package de.lessvoid.nifty;

import de.lessvoid.nifty.tools.SizeValue;
import junit.framework.TestCase;

/**
 * Testcases for SizeValue class.
 * @author void
 */
public class SizeValueTest extends TestCase {

  /**
   * Test percent.
   */
  public final void testPercent() {
    SizeValue value = new SizeValue("10%");
    assertEquals(10, value.getValueAsInt(100));
    assertEquals(100, value.getValueAsInt(1000));
    assertTrue(value.isPercentOrPixel());
  }

  /**
   * Test default values.
   */
  public final void testDefault() {
    SizeValue value = new SizeValue(null);
    assertEquals(-1, value.getValueAsInt(1000));
    assertEquals(-1, value.getValueAsInt(100));
    assertFalse(value.isPercentOrPixel());
  }

  /**
   * Test pixel value.
   */
  public final void testPixel() {
    SizeValue value = new SizeValue("10px");
    assertEquals(10, value.getValueAsInt(1000));
    assertEquals(10, value.getValueAsInt(100));
    assertTrue(value.isPercentOrPixel());
  }
}

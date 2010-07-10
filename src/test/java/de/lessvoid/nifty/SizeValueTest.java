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
  public void testPercent() {
    SizeValue value = new SizeValue("10%");
    assertEquals(10, value.getValueAsInt(100));
    assertEquals(100, value.getValueAsInt(1000));
    assertTrue(value.isPercentOrPixel());
  }

  /**
   * Test default values.
   */
  public void testDefault() {
    SizeValue value = new SizeValue(null);
    assertEquals(-1, value.getValueAsInt(1000));
    assertEquals(-1, value.getValueAsInt(100));
    assertFalse(value.isPercentOrPixel());
  }

  /**
   * Test pixel value.
   */
  public void testPixel() {
    SizeValue value = new SizeValue("10px");
    assertEquals(10, value.getValueAsInt(1000));
    assertEquals(10, value.getValueAsInt(100));
    assertTrue(value.isPercentOrPixel());
  }

  public void testWidthSuffix() {
    SizeValue value = new SizeValue("20%w");
    assertEquals(20, value.getValueAsInt(100));
    assertTrue(value.hasWidthSuffix());
    assertFalse(value.hasHeightSuffix());
  }

  public void testHeightSuffix() {
    SizeValue value = new SizeValue("20%h");
    assertEquals(20, value.getValueAsInt(100));
    assertTrue(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }
}

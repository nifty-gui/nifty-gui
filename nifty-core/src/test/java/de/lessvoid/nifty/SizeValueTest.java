package de.lessvoid.nifty;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.lessvoid.nifty.tools.SizeValue;

/**
 * Testcases for SizeValue class.
 * @author void
 */
public class SizeValueTest {

  @Test
  public void testPercent() {
    SizeValue value = new SizeValue("10%");
    assertEquals(10, value.getValueAsInt(100));
    assertEquals(100, value.getValueAsInt(1000));
    assertTrue(value.isPercentOrPixel());
  }

  @Test
  public void testDefault() {
    SizeValue value = new SizeValue(null);
    assertEquals(-1, value.getValueAsInt(1000));
    assertEquals(-1, value.getValueAsInt(100));
    assertFalse(value.isPercentOrPixel());
  }

  @Test
  public void testPixel() {
    SizeValue value = new SizeValue("10px");
    assertEquals(10, value.getValueAsInt(1000));
    assertEquals(10, value.getValueAsInt(100));
    assertTrue(value.isPercentOrPixel());
  }

  @Test
  public void testPixelWithoutPx() {
    SizeValue value = new SizeValue("10");
    assertEquals(10, value.getValueAsInt(1000));
    assertEquals(10, value.getValueAsInt(100));
    assertTrue(value.isPercentOrPixel());
  }

  @Test
  public void testWildcard() {
    SizeValue value = new SizeValue("*");
    assertEquals(-1, value.getValueAsInt(1000));
    assertEquals(-1, value.getValueAsInt(100));
    assertFalse(value.isPercentOrPixel());
  }

  @Test
  public void testWidthSuffix() {
    SizeValue value = new SizeValue("20%w");
    assertEquals(20, value.getValueAsInt(100));
    assertTrue(value.hasWidthSuffix());
    assertFalse(value.hasHeightSuffix());
  }

  @Test
  public void testHeightSuffix() {
    SizeValue value = new SizeValue("20%h");
    assertEquals(20, value.getValueAsInt(100));
    assertTrue(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test
  public void testPixelFactoryMethod() {
    SizeValue value = SizeValue.px(10);
    assertEquals(10, value.getValueAsInt(100));
  }

  @Test
  public void testPercentageFactoryMethod() {
    SizeValue value = SizeValue.percent(10);
    assertEquals(10, value.getValueAsInt(100));
  }

  @Test
  public void testWildcardFactoryMethod() {
    SizeValue value = SizeValue.wildcard();
    assertTrue(value.hasWildcard());
  }

  @Test
  public void testEqualsDifferentPxIsFalse() {
    SizeValue a = SizeValue.px(10);
    SizeValue b = SizeValue.px(12);
    assertFalse(a.equals(b));
  }

  @Test
  public void testEqualsSamePxIsTrue() {
    SizeValue a = SizeValue.px(10);
    SizeValue b = SizeValue.px(10);
    assertTrue(a.equals(b));
  }

  @Test
  public void testEqualsDifferentPercentIsFalse() {
    SizeValue a = SizeValue.percent(10);
    SizeValue b = SizeValue.percent(12);
    assertFalse(a.equals(b));
  }

  @Test
  public void testEqualsDifferentSuffixSameValueIsFalse() {
    SizeValue a = SizeValue.px(10);
    SizeValue b = SizeValue.percent(10);
    assertFalse(a.equals(b));
  }

  @Test
  public void testEqualsSamePercentIsTrue() {
    SizeValue a = SizeValue.percent(10);
    SizeValue b = SizeValue.percent(10);
    assertTrue(a.equals(b));
  }
}

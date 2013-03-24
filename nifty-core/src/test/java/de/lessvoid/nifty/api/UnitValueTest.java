package de.lessvoid.nifty.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.lessvoid.nifty.api.UnitValue;

/**
 * Testcases for SizeValue class.
 * @author void
 */
public class UnitValueTest {

  @Test
  public void testPercent() {
    UnitValue value = new UnitValue("10%");
    assertEquals(10, value.getValueAsInt(100));
    assertEquals(100, value.getValueAsInt(1000));
    assertTrue(value.isPercentOrPixel());
  }

  @Test
  public void testDefault() {
    UnitValue value = new UnitValue(null);
    assertEquals(-1, value.getValueAsInt(1000));
    assertEquals(-1, value.getValueAsInt(100));
    assertFalse(value.isPercentOrPixel());
  }

  @Test
  public void testPixel() {
    UnitValue value = new UnitValue("10px");
    assertEquals(10, value.getValueAsInt(1000));
    assertEquals(10, value.getValueAsInt(100));
    assertTrue(value.isPercentOrPixel());
  }

  @Test
  public void testPixelWithoutPx() {
    UnitValue value = new UnitValue("10");
    assertEquals(10, value.getValueAsInt(1000));
    assertEquals(10, value.getValueAsInt(100));
    assertTrue(value.isPercentOrPixel());
  }

  @Test
  public void testWildcard() {
    UnitValue value = new UnitValue("*");
    assertEquals(-1, value.getValueAsInt(1000));
    assertEquals(-1, value.getValueAsInt(100));
    assertFalse(value.isPercentOrPixel());
  }

  @Test
  public void testWidthSuffix() {
    UnitValue value = new UnitValue("20%w");
    assertEquals(20, value.getValueAsInt(100));
    assertTrue(value.hasWidthSuffix());
    assertFalse(value.hasHeightSuffix());
  }

  @Test
  public void testHeightSuffix() {
    UnitValue value = new UnitValue("20%h");
    assertEquals(20, value.getValueAsInt(100));
    assertTrue(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test
  public void testPixelFactoryMethod() {
    UnitValue value = UnitValue.px(10);
    assertEquals(10, value.getValueAsInt(100));
  }

  @Test
  public void testPercentageFactoryMethod() {
    UnitValue value = UnitValue.percent(10);
    assertEquals(10, value.getValueAsInt(100));
  }

  @Test
  public void testWildcardFactoryMethod() {
    UnitValue value = UnitValue.wildcard();
    assertTrue(value.hasWildcard());
  }

  @Test
  public void testEqualsDifferentPxIsFalse() {
    UnitValue a = UnitValue.px(10);
    UnitValue b = UnitValue.px(12);
    assertFalse(a.equals(b));
  }

  @Test
  public void testEqualsSamePxIsTrue() {
    UnitValue a = UnitValue.px(10);
    UnitValue b = UnitValue.px(10);
    assertTrue(a.equals(b));
  }

  @Test
  public void testEqualsDifferentPercentIsFalse() {
    UnitValue a = UnitValue.percent(10);
    UnitValue b = UnitValue.percent(12);
    assertFalse(a.equals(b));
  }

  @Test
  public void testEqualsDifferentSuffixSameValueIsFalse() {
    UnitValue a = UnitValue.px(10);
    UnitValue b = UnitValue.percent(10);
    assertFalse(a.equals(b));
  }

  @Test
  public void testEqualsSamePercentIsTrue() {
    UnitValue a = UnitValue.percent(10);
    UnitValue b = UnitValue.percent(10);
    assertTrue(a.equals(b));
  }
}

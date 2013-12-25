package de.lessvoid.nifty;

import de.lessvoid.nifty.tools.SizeValue;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Testcases for SizeValue class.
 *
 * @author void
 */
public class SizeValueTest {

  @Test
  public void testPercent() {
    SizeValue value = new SizeValue("10%");
    assertEquals(10, value.getValueAsInt(100));
    assertEquals(100, value.getValueAsInt(1000));
    assertTrue(value.hasValue());
    assertTrue(value.isPercent());
    assertFalse(value.isPixel());
    assertFalse(value.isIndependentFromParent());
  }

  @Test
  public void testDefault() {
    SizeValue value = new SizeValue(null);
    assertEquals(-1, value.getValueAsInt(1000));
    assertEquals(-1, value.getValueAsInt(100));
    assertTrue(value.hasDefault());
    assertFalse(value.hasValue());
    assertTrue(value.isIndependentFromParent());
  }

  @Test
  public void testDefaultGeneratedValue() {
    SizeValue value = new SizeValue("5d");
    assertEquals(5, value.getValueAsInt(1000));
    assertEquals(5, value.getValueAsInt(100));
    assertTrue(value.hasDefault());
    assertTrue(value.hasValue());
    assertTrue(value.isPixel());
    assertTrue(value.isIndependentFromParent());
  }

  @Test
  public void testPixel() {
    SizeValue value = new SizeValue("10px");
    assertEquals(10, value.getValueAsInt(1000));
    assertEquals(10, value.getValueAsInt(100));
    assertTrue(value.hasValue());
    assertTrue(value.isPixel());
    assertTrue(value.isIndependentFromParent());
  }

  @Test
  public void testPixelWithoutPx() {
    SizeValue value = new SizeValue("10");
    assertEquals(10, value.getValueAsInt(1000));
    assertEquals(10, value.getValueAsInt(100));
    assertTrue(value.hasValue());
    assertTrue(value.isPixel());
    assertTrue(value.isIndependentFromParent());
  }

  @Test
  public void testSum() {
    SizeValue value = new SizeValue("sum");
    assertEquals(-1, value.getValueAsInt(1000));
    assertEquals(-1, value.getValueAsInt(100));
    assertFalse(value.hasValue());
    assertFalse(value.isPixel());
    assertTrue(value.isIndependentFromParent());
    assertTrue(value.hasSum());
  }

  @Test
  public void testSumGeneratedValue() {
    SizeValue value = new SizeValue("500s");
    assertEquals(500, value.getValueAsInt(1000));
    assertEquals(500, value.getValueAsInt(100));
    assertTrue(value.hasValue());
    assertTrue(value.isPixel());
    assertTrue(value.isIndependentFromParent());
    assertTrue(value.hasSum());
  }

  @Test
  public void testMax() {
    SizeValue value = new SizeValue("max");
    assertEquals(-1, value.getValueAsInt(1000));
    assertEquals(-1, value.getValueAsInt(100));
    assertFalse(value.hasValue());
    assertFalse(value.isPixel());
    assertTrue(value.isIndependentFromParent());
    assertTrue(value.hasMax());
  }

  @Test
  public void testMaxGeneratedValue() {
    SizeValue value = new SizeValue("500m");
    assertEquals(500, value.getValueAsInt(1000));
    assertEquals(500, value.getValueAsInt(100));
    assertTrue(value.hasValue());
    assertTrue(value.isPixel());
    assertTrue(value.isIndependentFromParent());
    assertTrue(value.hasMax());
  }

  @Test
  public void testWildcard() {
    SizeValue value = new SizeValue("*");
    assertEquals(-1, value.getValueAsInt(1000));
    assertEquals(-1, value.getValueAsInt(100));
    assertFalse(value.hasValue());
    assertFalse(value.isPixel());
    assertFalse(value.isIndependentFromParent());
  }

  @Test
  public void testWidthSuffix() {
    SizeValue value = new SizeValue("20%w");
    assertEquals(20, value.getValueAsInt(100));
    assertTrue(value.hasWidthSuffix());
    assertFalse(value.hasHeightSuffix());
    assertFalse(value.isIndependentFromParent());
  }

  @Test
  public void testHeightSuffix() {
    SizeValue value = new SizeValue("20%h");
    assertEquals(20, value.getValueAsInt(100));
    assertTrue(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
    assertFalse(value.isIndependentFromParent());
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

  @Test
  public void testOffByOneCastErrorExists() {
    SizeValue a = SizeValue.percent(100);
    assertTrue(((int) a.getValue(212)) == 211);
  }

  @Test
  public void testOffByOneCastErrorFixedByUsingGetValueAsInt() {
    SizeValue a = SizeValue.percent(100);
    assertTrue(a.getValueAsInt(212) == 212);
  }
}

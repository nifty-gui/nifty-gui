package de.lessvoid.nifty.tools;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Testcases for SizeValue class.
 *
 * @author void &lt;void@lessvoid.com&gt;
 * @author Joris van der Wel &lt;joris@jorisvanderwel.com&gt;
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class SizeValueTest {
  @Test
  public void testPercent() {
    SizeValue value = SizeValue.percent(10);
    assertEquals(100, value.getValueAsInt(1000.f));
    assertEquals(10, value.getValueAsInt(100.f));
    assertEquals(100.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(10.f, value.getValue(100.f), 1.0e-7);
    assertTrue(value.hasValue());
    assertTrue(value.isPercent());
    assertFalse(value.isPixel());
    assertFalse(value.isIndependentFromParent());
    assertFalse(value.hasDefault());
    assertFalse(value.hasWildcard());
    assertFalse(value.hasMax());
    assertFalse(value.hasSum());
    assertFalse(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());

  }

  @Test
  public void testPercentString() {
    SizeValue value = new SizeValue("10%");
    assertEquals(100, value.getValueAsInt(1000.f));
    assertEquals(10, value.getValueAsInt(100.f));
    assertEquals(100.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(10.f, value.getValue(100.f), 1.0e-7);
    assertTrue(value.hasValue());
    assertTrue(value.isPercent());
    assertFalse(value.isPixel());
    assertFalse(value.isIndependentFromParent());
    assertFalse(value.hasDefault());
    assertFalse(value.hasWildcard());
    assertFalse(value.hasMax());
    assertFalse(value.hasSum());
    assertFalse(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPercentStringException() {
    new SizeValue("%");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPercentIllegalStringException() {
    new SizeValue("ab%");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPercentNoValueException() {
    new SizeValue(SizeValueType.Percent);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPercentComputedException() {
    new SizeValue(SizeValueType.Percent, 5);
  }

  @Test
  public void testDefault() {
    SizeValue value = SizeValue.def();
    assertEquals(-1, value.getValueAsInt(1000.f));
    assertEquals(-1, value.getValueAsInt(100.f));
    assertEquals(-1.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(-1.f, value.getValue(100.f), 1.0e-7);
    assertFalse(value.hasValue());
    assertFalse(value.isPercent());
    assertFalse(value.isPixel());
    assertTrue(value.isIndependentFromParent());
    assertTrue(value.hasDefault());
    assertFalse(value.hasWildcard());
    assertFalse(value.hasMax());
    assertFalse(value.hasSum());
    assertFalse(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test
  public void testDefaultString() {
    SizeValue value = new SizeValue("d");
    assertEquals(-1, value.getValueAsInt(1000.f));
    assertEquals(-1, value.getValueAsInt(100.f));
    assertEquals(-1.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(-1.f, value.getValue(100.f), 1.0e-7);
    assertFalse(value.hasValue());
    assertFalse(value.isPercent());
    assertFalse(value.isPixel());
    assertTrue(value.isIndependentFromParent());
    assertTrue(value.hasDefault());
    assertFalse(value.hasWildcard());
    assertFalse(value.hasMax());
    assertFalse(value.hasSum());
    assertFalse(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test
  public void testDefaultGeneratedValue() {
    SizeValue value = SizeValue.def(5);
    assertEquals(5, value.getValueAsInt(1000.f));
    assertEquals(5, value.getValueAsInt(100.f));
    assertEquals(5.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(5.f, value.getValue(100.f), 1.0e-7);
    assertTrue(value.hasValue());
    assertFalse(value.isPercent());
    assertTrue(value.isPixel());
    assertTrue(value.isIndependentFromParent());
    assertTrue(value.hasDefault());
    assertFalse(value.hasWildcard());
    assertFalse(value.hasMax());
    assertFalse(value.hasSum());
    assertFalse(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDefaultStringException() {
    new SizeValue("5d");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDefaultValueException() {
    new SizeValue(5, SizeValueType.Default);
  }

  @Test
  public void testPixel() {
    SizeValue value = SizeValue.px(10);
    assertEquals(10, value.getValueAsInt(1000.f));
    assertEquals(10, value.getValueAsInt(100.f));
    assertEquals(10.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(10.f, value.getValue(100.f), 1.0e-7);
    assertTrue(value.hasValue());
    assertFalse(value.isPercent());
    assertTrue(value.isPixel());
    assertTrue(value.isIndependentFromParent());
    assertFalse(value.hasDefault());
    assertFalse(value.hasWildcard());
    assertFalse(value.hasMax());
    assertFalse(value.hasSum());
    assertFalse(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test
  public void testPixelString() {
    SizeValue value = new SizeValue("10px");
    assertEquals(10, value.getValueAsInt(1000.f));
    assertEquals(10, value.getValueAsInt(100.f));
    assertEquals(10.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(10.f, value.getValue(100.f), 1.0e-7);
    assertTrue(value.hasValue());
    assertFalse(value.isPercent());
    assertTrue(value.isPixel());
    assertTrue(value.isIndependentFromParent());
    assertFalse(value.hasDefault());
    assertFalse(value.hasWildcard());
    assertFalse(value.hasMax());
    assertFalse(value.hasSum());
    assertFalse(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test
  public void testPixelStringWithoutPx() {
    SizeValue value = new SizeValue("10");
    assertEquals(10, value.getValueAsInt(1000.f));
    assertEquals(10, value.getValueAsInt(100.f));
    assertEquals(10.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(10.f, value.getValue(100.f), 1.0e-7);
    assertTrue(value.hasValue());
    assertFalse(value.isPercent());
    assertTrue(value.isPixel());
    assertTrue(value.isIndependentFromParent());
    assertFalse(value.hasDefault());
    assertFalse(value.hasWildcard());
    assertFalse(value.hasMax());
    assertFalse(value.hasSum());
    assertFalse(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPixelStringException() {
    new SizeValue("px");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPixelIllegalStringException() {
    new SizeValue("abcpx");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPixelIllegalStringException2() {
    new SizeValue("abc");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPixelNoValueException() {
    new SizeValue(SizeValueType.Pixel);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPixelComputedException() {
    new SizeValue(SizeValueType.Pixel, 5);
  }

  @Test
  public void testSum() {
    SizeValue value = SizeValue.sum();
    assertEquals(-1, value.getValueAsInt(1000.f));
    assertEquals(-1, value.getValueAsInt(100.f));
    assertEquals(-1.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(-1.f, value.getValue(100.f), 1.0e-7);
    assertFalse(value.hasValue());
    assertFalse(value.isPercent());
    assertFalse(value.isPixel());
    assertTrue(value.isIndependentFromParent());
    assertFalse(value.hasDefault());
    assertFalse(value.hasWildcard());
    assertFalse(value.hasMax());
    assertTrue(value.hasSum());
    assertFalse(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test
  public void testSumString() {
    SizeValue value = new SizeValue("s");
    assertEquals(-1, value.getValueAsInt(1000.f));
    assertEquals(-1, value.getValueAsInt(100.f));
    assertEquals(-1.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(-1.f, value.getValue(100.f), 1.0e-7);
    assertFalse(value.hasValue());
    assertFalse(value.isPercent());
    assertFalse(value.isPixel());
    assertTrue(value.isIndependentFromParent());
    assertFalse(value.hasDefault());
    assertFalse(value.hasWildcard());
    assertFalse(value.hasMax());
    assertTrue(value.hasSum());
    assertFalse(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test
  public void testSumStringLong() {
    SizeValue value = new SizeValue("sum");
    assertEquals(-1, value.getValueAsInt(1000.f));
    assertEquals(-1, value.getValueAsInt(100.f));
    assertEquals(-1.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(-1.f, value.getValue(100.f), 1.0e-7);
    assertFalse(value.hasValue());
    assertFalse(value.isPercent());
    assertFalse(value.isPixel());
    assertTrue(value.isIndependentFromParent());
    assertFalse(value.hasDefault());
    assertFalse(value.hasWildcard());
    assertFalse(value.hasMax());
    assertTrue(value.hasSum());
    assertFalse(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test
  public void testSumGeneratedValue() {
    SizeValue value = SizeValue.sum(500);
    assertEquals(500, value.getValueAsInt(1000.f));
    assertEquals(500, value.getValueAsInt(100.f));
    assertEquals(500.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(500.f, value.getValue(100.f), 1.0e-7);
    assertTrue(value.hasValue());
    assertFalse(value.isPercent());
    assertTrue(value.isPixel());
    assertTrue(value.isIndependentFromParent());
    assertFalse(value.hasDefault());
    assertFalse(value.hasWildcard());
    assertFalse(value.hasMax());
    assertTrue(value.hasSum());
    assertFalse(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSumStringException() {
    new SizeValue("5s");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSumValueException() {
    new SizeValue(5, SizeValueType.Sum);
  }

  @Test
  public void testMax() {
    SizeValue value = SizeValue.max();
    assertEquals(-1, value.getValueAsInt(1000.f));
    assertEquals(-1, value.getValueAsInt(100.f));
    assertEquals(-1.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(-1.f, value.getValue(100.f), 1.0e-7);
    assertFalse(value.hasValue());
    assertFalse(value.isPercent());
    assertFalse(value.isPixel());
    assertTrue(value.isIndependentFromParent());
    assertFalse(value.hasDefault());
    assertFalse(value.hasWildcard());
    assertTrue(value.hasMax());
    assertFalse(value.hasSum());
    assertFalse(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test
  public void testMaxString() {
    SizeValue value = new SizeValue("m");
    assertEquals(-1, value.getValueAsInt(1000.f));
    assertEquals(-1, value.getValueAsInt(100.f));
    assertEquals(-1.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(-1.f, value.getValue(100.f), 1.0e-7);
    assertFalse(value.hasValue());
    assertFalse(value.isPercent());
    assertFalse(value.isPixel());
    assertTrue(value.isIndependentFromParent());
    assertFalse(value.hasDefault());
    assertFalse(value.hasWildcard());
    assertTrue(value.hasMax());
    assertFalse(value.hasSum());
    assertFalse(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test
  public void testMaxStringLong() {
    SizeValue value = new SizeValue("max");
    assertEquals(-1, value.getValueAsInt(1000.f));
    assertEquals(-1, value.getValueAsInt(100.f));
    assertEquals(-1.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(-1.f, value.getValue(100.f), 1.0e-7);
    assertFalse(value.hasValue());
    assertFalse(value.isPercent());
    assertFalse(value.isPixel());
    assertTrue(value.isIndependentFromParent());
    assertFalse(value.hasDefault());
    assertFalse(value.hasWildcard());
    assertTrue(value.hasMax());
    assertFalse(value.hasSum());
    assertFalse(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test
  public void testMaxGeneratedValue() {
    SizeValue value = SizeValue.max(500);
    assertEquals(500, value.getValueAsInt(1000.f));
    assertEquals(500, value.getValueAsInt(100.f));
    assertEquals(500.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(500.f, value.getValue(100.f), 1.0e-7);
    assertTrue(value.hasValue());
    assertFalse(value.isPercent());
    assertTrue(value.isPixel());
    assertTrue(value.isIndependentFromParent());
    assertFalse(value.hasDefault());
    assertFalse(value.hasWildcard());
    assertTrue(value.hasMax());
    assertFalse(value.hasSum());
    assertFalse(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMaxStringException() {
    new SizeValue("5m");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMaxValueException() {
    new SizeValue(5, SizeValueType.Maximum);
  }

  @Test
  public void testWildcard() {
    SizeValue value = SizeValue.wildcard();
    assertEquals(-1, value.getValueAsInt(1000.f));
    assertEquals(-1, value.getValueAsInt(100.f));
    assertEquals(-1.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(-1.f, value.getValue(100.f), 1.0e-7);
    assertFalse(value.hasValue());
    assertFalse(value.isPercent());
    assertFalse(value.isPixel());
    assertFalse(value.isIndependentFromParent());
    assertFalse(value.hasDefault());
    assertTrue(value.hasWildcard());
    assertFalse(value.hasMax());
    assertFalse(value.hasSum());
    assertFalse(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test
  public void testWildcardString() {
    SizeValue value = new SizeValue("*");
    assertEquals(-1, value.getValueAsInt(1000.f));
    assertEquals(-1, value.getValueAsInt(100.f));
    assertEquals(-1.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(-1.f, value.getValue(100.f), 1.0e-7);
    assertFalse(value.hasValue());
    assertFalse(value.isPercent());
    assertFalse(value.isPixel());
    assertFalse(value.isIndependentFromParent());
    assertFalse(value.hasDefault());
    assertTrue(value.hasWildcard());
    assertFalse(value.hasMax());
    assertFalse(value.hasSum());
    assertFalse(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test
  public void testWildcardComputedValue() {
    SizeValue value = SizeValue.wildcard(5);
    assertEquals(5, value.getValueAsInt(1000.f));
    assertEquals(5, value.getValueAsInt(100.f));
    assertEquals(5.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(5.f, value.getValue(100.f), 1.0e-7);
    assertTrue(value.hasValue());
    assertFalse(value.isPercent());
    assertTrue(value.isPixel());
    assertFalse(value.isIndependentFromParent());
    assertFalse(value.hasDefault());
    assertTrue(value.hasWildcard());
    assertFalse(value.hasMax());
    assertFalse(value.hasSum());
    assertFalse(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWildcardStringException() {
    new SizeValue("5*");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWildcardValueException() {
    new SizeValue(5, SizeValueType.Wildcard);
  }

  @Test
  public void testWidthSuffix() {
    SizeValue value = SizeValue.percentWidth(20);
    assertEquals(200, value.getValueAsInt(1000.f));
    assertEquals(20, value.getValueAsInt(100.f));
    assertEquals(200.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(20.f, value.getValue(100.f), 1.0e-7);
    assertTrue(value.hasValue());
    assertTrue(value.isPercent());
    assertFalse(value.isPixel());
    assertFalse(value.isIndependentFromParent());
    assertFalse(value.hasDefault());
    assertFalse(value.hasWildcard());
    assertFalse(value.hasMax());
    assertFalse(value.hasSum());
    assertFalse(value.hasHeightSuffix());
    assertTrue(value.hasWidthSuffix());
  }

  @Test
  public void testWidthSuffixString() {
    SizeValue value = new SizeValue("20%w");
    assertEquals(200, value.getValueAsInt(1000.f));
    assertEquals(20, value.getValueAsInt(100.f));
    assertEquals(200.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(20.f, value.getValue(100.f), 1.0e-7);
    assertTrue(value.hasValue());
    assertTrue(value.isPercent());
    assertFalse(value.isPixel());
    assertFalse(value.isIndependentFromParent());
    assertFalse(value.hasDefault());
    assertFalse(value.hasWildcard());
    assertFalse(value.hasMax());
    assertFalse(value.hasSum());
    assertFalse(value.hasHeightSuffix());
    assertTrue(value.hasWidthSuffix());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWidthSuffixStringException() {
    new SizeValue("%w");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWidthSuffixComputedValueException() {
    new SizeValue(SizeValueType.PercentWidth, 20);
  }

  @Test
  public void testHeightSuffix() {
    SizeValue value = SizeValue.percentHeight(20);
    assertEquals(200, value.getValueAsInt(1000.f));
    assertEquals(20, value.getValueAsInt(100.f));
    assertEquals(200.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(20.f, value.getValue(100.f), 1.0e-7);
    assertTrue(value.hasValue());
    assertTrue(value.isPercent());
    assertFalse(value.isPixel());
    assertFalse(value.isIndependentFromParent());
    assertFalse(value.hasDefault());
    assertFalse(value.hasWildcard());
    assertFalse(value.hasMax());
    assertFalse(value.hasSum());
    assertTrue(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test
  public void testHeightSuffixString() {
    SizeValue value = new SizeValue("20%h");
    assertEquals(200, value.getValueAsInt(1000.f));
    assertEquals(20, value.getValueAsInt(100.f));
    assertEquals(200.f, value.getValue(1000.f), 1.0e-7);
    assertEquals(20.f, value.getValue(100.f), 1.0e-7);
    assertTrue(value.hasValue());
    assertTrue(value.isPercent());
    assertFalse(value.isPixel());
    assertFalse(value.isIndependentFromParent());
    assertFalse(value.hasDefault());
    assertFalse(value.hasWildcard());
    assertFalse(value.hasMax());
    assertFalse(value.hasSum());
    assertTrue(value.hasHeightSuffix());
    assertFalse(value.hasWidthSuffix());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHeightSuffixStringException() {
    new SizeValue("%h");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHeightSuffixComputedValueException() {
    new SizeValue(SizeValueType.PercentHeight, 20);
  }

  @Test
  public void testPixelEquals() {
    SizeValue a = SizeValue.px(10);
    SizeValue b = new SizeValue(10, SizeValueType.Pixel);
    SizeValue c = new SizeValue("10px");
    SizeValue d = new SizeValue("10");

    assertEquals(a, b);
    assertEquals(a, c);
    assertEquals(a, d);
  }

  @Test
  public void testPercentEquals() {
    SizeValue a = SizeValue.percent(10);
    SizeValue b = new SizeValue(10, SizeValueType.Percent);
    SizeValue c = new SizeValue("10%");

    assertEquals(a, b);
    assertEquals(a, c);
  }

  @Test
  public void testDefaultEquals() {
    SizeValue a = SizeValue.def();
    SizeValue b = new SizeValue(SizeValueType.Default);
    SizeValue c = new SizeValue("d");
    SizeValue d = new SizeValue("default");
    SizeValue e = new SizeValue("");
    SizeValue f = new SizeValue((String) null);

    assertEquals(a, b);
    assertEquals(a, c);
    assertEquals(a, d);
    assertEquals(a, e);
    assertEquals(a, f);
  }

  @Test
  public void testDefaultComputedEquals() {
    SizeValue a = SizeValue.def(5);
    SizeValue b = new SizeValue(SizeValueType.Default, 5);

    assertEquals(a, b);
  }

  @Test
  public void testSumEquals() {
    SizeValue a = SizeValue.sum();
    SizeValue b = new SizeValue(SizeValueType.Sum);
    SizeValue c = new SizeValue("s");
    SizeValue d = new SizeValue("sum");

    assertEquals(a, b);
    assertEquals(a, c);
    assertEquals(a, d);
  }

  @Test
  public void testSumComputedEquals() {
    SizeValue a = SizeValue.sum(5);
    SizeValue b = new SizeValue(SizeValueType.Sum, 5);

    assertEquals(a, b);
  }

  @Test
  public void testMaxEquals() {
    SizeValue a = SizeValue.max();
    SizeValue b = new SizeValue(SizeValueType.Maximum);
    SizeValue c = new SizeValue("m");
    SizeValue d = new SizeValue("max");

    assertEquals(a, b);
    assertEquals(a, c);
    assertEquals(a, d);
  }

  @Test
  public void testComputedEquals() {
    SizeValue a = SizeValue.max(5);
    SizeValue b = new SizeValue(SizeValueType.Maximum, 5);

    assertEquals(a, b);
  }

  @Test
  public void testWildcardEquals() {
    SizeValue a = SizeValue.wildcard();
    SizeValue b = new SizeValue(SizeValueType.Wildcard);
    SizeValue c = new SizeValue("*");

    assertEquals(a, b);
    assertEquals(a, c);
  }

  @Test
  public void testWildcardComputedEquals() {
    SizeValue a = SizeValue.wildcard(5);
    SizeValue b = new SizeValue(SizeValueType.Wildcard, 5);

    assertEquals(a, b);
  }

  @Test
  public void testPercentHeightEquals() {
    SizeValue a = SizeValue.percentHeight(10);
    SizeValue b = new SizeValue(10, SizeValueType.PercentHeight);
    SizeValue c = new SizeValue("10%h");

    assertEquals(a, b);
    assertEquals(a, c);
  }

  @Test
  public void testPercentWidthEquals() {
    SizeValue a = SizeValue.percentWidth(10);
    SizeValue b = new SizeValue(10, SizeValueType.PercentWidth);
    SizeValue c = new SizeValue("10%w");

    assertEquals(a, b);
    assertEquals(a, c);
  }

  @Test
  public void testSameValueNotEquals() {
    SizeValue[] values = new SizeValue[] {
        SizeValue.px(10),
        SizeValue.percent(10),
        SizeValue.percentHeight(10),
        SizeValue.percentWidth(10),
        SizeValue.def(10),
        SizeValue.wildcard(10),
        SizeValue.sum(10),
        SizeValue.max(10)
    };

    for (int i = 0; i < values.length; i++) {
      SizeValue first = values[i];
      for (int j = 0; j < values.length; j++) {
        if (i == j) {
          continue;
        }
        SizeValue second = values[j];
        assertNotEquals(first.toString() + " <=> " + second.toString(), first, second);
      }
    }
  }

  @Test
  public void testSameNoValueNotEquals() {
    SizeValue[] values = new SizeValue[] {
        SizeValue.def(),
        SizeValue.wildcard(),
        SizeValue.sum(),
        SizeValue.max()
    };

    for (int i = 0; i < values.length; i++) {
      SizeValue first = values[i];
      for (int j = 0; j < values.length; j++) {
        if (i == j) {
          continue;
        }
        SizeValue second = values[j];
        assertNotEquals(first.toString() + " <=> " + second.toString(), first, second);
      }
    }
  }

  @Test
  public void testDifferentValueNotEquals() {
    SizeValue[] values1 = new SizeValue[] {
        SizeValue.px(10),
        SizeValue.percent(10),
        SizeValue.percentHeight(10),
        SizeValue.percentWidth(10),
        SizeValue.def(10),
        SizeValue.wildcard(10),
        SizeValue.sum(10),
        SizeValue.max(10)
    };
    SizeValue[] values2 = new SizeValue[] {
        SizeValue.px(20),
        SizeValue.percent(20),
        SizeValue.percentHeight(20),
        SizeValue.percentWidth(20),
        SizeValue.def(20),
        SizeValue.wildcard(20),
        SizeValue.sum(20),
        SizeValue.max(20)
    };

    for (int i = 0; i < values1.length; i++) {
      SizeValue first = values1[i];
      SizeValue second = values2[i];
      assertNotEquals(first.toString() + " <=> " + second.toString(), first, second);
    }
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

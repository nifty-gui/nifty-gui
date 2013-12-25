package de.lessvoid.nifty.tools;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ColorValueParserTest {

  @Test
  public void testNoColor() {
    ColorValueParser parser = new ColorValueParser();
    assertResult(parser, parser.isColor("hello world", 0), false, null, -1);
  }

  @Test
  public void testContainsColor() {
    ColorValueParser parser = new ColorValueParser();
    assertResult(parser, parser.isColor("\\#ffff#world", 0), true, "(1.0,1.0,1.0,1.0)", 7);
  }

  @Test
  public void testContainsColorThatDoesNotEnd() {
    ColorValueParser parser = new ColorValueParser();
    assertResult(parser, parser.isColor("\\#ffff", 0), false, null, -1);
  }

  private void assertResult(
      final ColorValueParser parser,
      final boolean result,
      final boolean expectedIsColor,
      final String expectedColor,
      final int expectedNextIndex) {
    assertEquals(expectedIsColor, parser.isColor());
    assertEquals(expectedIsColor, result);

    if (expectedColor == null) {
      assertNull(parser.getColor());
    } else {
      assertEquals(expectedColor, parser.getColor().toString());
    }
    assertEquals(expectedNextIndex, parser.getNextIndex());
  }

}

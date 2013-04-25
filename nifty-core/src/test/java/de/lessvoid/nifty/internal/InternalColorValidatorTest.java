package de.lessvoid.nifty.internal;

import org.junit.Test;

import de.lessvoid.nifty.internal.InternalColorValidator;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class InternalColorValidatorTest {
  private InternalColorValidator validator = new InternalColorValidator();

  private String[] shortColor = new String[] { "#000", "#fff", "#FFF", "#0af", "#0AF", "#0aF", "#0Af", "#aaa", "#157" };

  private String[] shortAlphaColor = new String[] { "#0000", "#ffff", "#FFFF", "#0afb", "#0AFB", "#0aFb", "#0AfB",
      "#aaaa", "#1579" };

  private String[] longColor = new String[] { "#000000", "#ffffff", "#FFFFFF", "#00aaff", "#00AAFF", "#00aaFF",
      "#00AAff", "#aaaaaa", "#115577" };

  private String[] longAlphaColor = new String[] { "#00000000", "#ffffffff", "#FFFFFFFF", "#00aaffbb", "#00AAFFBB",
      "#00aaFFbb", "#00AAffBB", "#aaaaaaaa", "#11557799" };

  @Test
  public void testInvalid() {
    testInvalid(null);
    testInvalid("#");
    testInvalid("#0");
    testInvalid("#00");
    testInvalid("#00000");
    testInvalid("#0000000");
    testInvalid("g#000");
    testInvalid(" #000");
    testInvalid("#000j");
    testInvalid("#000 ");
    testInvalid("#000 ");
  }

  private void testInvalid(final String text) {
    assertFalse(generateInvalidColorMsg(text), validator.isValid(text));
  }

  private String generateInvalidColorMsg(final String color) {
    final StringBuilder builder = new StringBuilder();
    builder.append("Invalid color \"");
    builder.append(String.valueOf(color));
    builder.append("\" was detected as valid.");
    return builder.toString();
  }

  @Test
  public void testValid() {
    for (final String color : shortColor) {
      testValid(color);
    }
    for (final String color : shortAlphaColor) {
      testValid(color);
    }
    for (final String color : longColor) {
      testValid(color);
    }
    for (final String color : longAlphaColor) {
      testValid(color);
    }
  }

  private void testValid(final String text) {
    assertTrue(generateValidColorMsg(text), validator.isValid(text));
  }

  private String generateValidColorMsg(final String color) {
    final StringBuilder builder = new StringBuilder();
    builder.append("Valid color \"");
    builder.append(String.valueOf(color));
    builder.append("\" was detected as invalid.");
    return builder.toString();
  }

  @Test
  public void testShortMode() {
    for (final String color : shortColor) {
      assertTrue(generateValidColorMsg(color), validator.isShortModeWithoutAlpha(color));
    }

    for (final String color : shortAlphaColor) {
      assertFalse(generateInvalidColorMsg(color), validator.isShortModeWithoutAlpha(color));
    }

    for (final String color : longColor) {
      assertFalse(generateInvalidColorMsg(color), validator.isShortModeWithoutAlpha(color));
    }

    for (final String color : longAlphaColor) {
      assertFalse(generateInvalidColorMsg(color), validator.isShortModeWithoutAlpha(color));
    }
  }

  @Test
  public void testShortAlphaMode() {
    for (final String color : shortColor) {
      assertFalse(generateInvalidColorMsg(color), validator.isShortMode(color));
    }

    for (final String color : shortAlphaColor) {
      assertTrue(generateValidColorMsg(color), validator.isShortMode(color));
    }

    for (final String color : longColor) {
      assertFalse(generateInvalidColorMsg(color), validator.isShortMode(color));
    }

    for (final String color : longAlphaColor) {
      assertFalse(generateInvalidColorMsg(color), validator.isShortMode(color));
    }
  }

  @Test
  public void testLongMode() {
    for (final String color : shortColor) {
      assertFalse(generateInvalidColorMsg(color), validator.isLongModeWithoutAlpha(color));
    }

    for (final String color : shortAlphaColor) {
      assertFalse(generateInvalidColorMsg(color), validator.isLongModeWithoutAlpha(color));
    }

    for (final String color : longColor) {
      assertTrue(generateValidColorMsg(color), validator.isLongModeWithoutAlpha(color));
    }

    for (final String color : longAlphaColor) {
      assertFalse(generateInvalidColorMsg(color), validator.isLongModeWithoutAlpha(color));
    }
  }

  @Test
  public void testLongAlphaMode() {
    for (final String color : shortColor) {
      assertFalse(generateInvalidColorMsg(color), validator.isLongMode(color));
    }

    for (final String color : shortAlphaColor) {
      assertFalse(generateInvalidColorMsg(color), validator.isLongMode(color));
    }

    for (final String color : longColor) {
      assertFalse(generateInvalidColorMsg(color), validator.isLongMode(color));
    }

    for (final String color : longAlphaColor) {
      assertTrue(generateValidColorMsg(color), validator.isLongMode(color));
    }
  }
}

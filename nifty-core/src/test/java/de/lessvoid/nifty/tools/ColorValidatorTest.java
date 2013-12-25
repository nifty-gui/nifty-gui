package de.lessvoid.nifty.tools;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ColorValidatorTest {
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
    assertFalse(generateInvalidColorMsg(text), ColorValidator.isValid(text));
  }

  private String generateInvalidColorMsg(final String color) {
    return "Invalid color \"" + String.valueOf(color) + "\" was detected as valid.";
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
    assertTrue(generateValidColorMsg(text), ColorValidator.isValid(text));
  }

  private String generateValidColorMsg(final String color) {
    return "Valid color \"" + String.valueOf(color) + "\" was detected as invalid.";
  }

  @Test
  public void testShortMode() {
    for (final String color : shortColor) {
      assertTrue(generateValidColorMsg(color), ColorValidator.isShortModeWithoutAlpha(color));
    }

    for (final String color : shortAlphaColor) {
      assertFalse(generateInvalidColorMsg(color), ColorValidator.isShortModeWithoutAlpha(color));
    }

    for (final String color : longColor) {
      assertFalse(generateInvalidColorMsg(color), ColorValidator.isShortModeWithoutAlpha(color));
    }

    for (final String color : longAlphaColor) {
      assertFalse(generateInvalidColorMsg(color), ColorValidator.isShortModeWithoutAlpha(color));
    }
  }

  @Test
  public void testShortAlphaMode() {
    for (final String color : shortColor) {
      assertFalse(generateInvalidColorMsg(color), ColorValidator.isShortMode(color));
    }

    for (final String color : shortAlphaColor) {
      assertTrue(generateValidColorMsg(color), ColorValidator.isShortMode(color));
    }

    for (final String color : longColor) {
      assertFalse(generateInvalidColorMsg(color), ColorValidator.isShortMode(color));
    }

    for (final String color : longAlphaColor) {
      assertFalse(generateInvalidColorMsg(color), ColorValidator.isShortMode(color));
    }
  }

  @Test
  public void testLongMode() {
    for (final String color : shortColor) {
      assertFalse(generateInvalidColorMsg(color), ColorValidator.isLongModeWithoutAlpha(color));
    }

    for (final String color : shortAlphaColor) {
      assertFalse(generateInvalidColorMsg(color), ColorValidator.isLongModeWithoutAlpha(color));
    }

    for (final String color : longColor) {
      assertTrue(generateValidColorMsg(color), ColorValidator.isLongModeWithoutAlpha(color));
    }

    for (final String color : longAlphaColor) {
      assertFalse(generateInvalidColorMsg(color), ColorValidator.isLongModeWithoutAlpha(color));
    }
  }

  @Test
  public void testLongAlphaMode() {
    for (final String color : shortColor) {
      assertFalse(generateInvalidColorMsg(color), ColorValidator.isLongMode(color));
    }

    for (final String color : shortAlphaColor) {
      assertFalse(generateInvalidColorMsg(color), ColorValidator.isLongMode(color));
    }

    for (final String color : longColor) {
      assertFalse(generateInvalidColorMsg(color), ColorValidator.isLongMode(color));
    }

    for (final String color : longAlphaColor) {
      assertTrue(generateValidColorMsg(color), ColorValidator.isLongMode(color));
    }
  }
}

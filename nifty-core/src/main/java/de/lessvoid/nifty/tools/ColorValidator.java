package de.lessvoid.nifty.tools;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Pattern;

/**
 * This checks a given String that represents a color for being valid. Supported
 * are both short mode "#f12f" and long mode "#ff1122ff"
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class ColorValidator {
  /**
   * The pattern used to check if the string is valid to be a color definition.
   */
  @Nonnull
  private static final Pattern colorPattern = Pattern.compile("#\\p{XDigit}{3,8}");

  /**
   * Check if a string fits any type of color definition string.
   *
   * @param toCheck the text to check
   * @return {@code true} in case the text is a color definition
   */
  public static boolean isValid(@Nullable final String toCheck) {
    if (toCheck == null) {
      return false;
    }

    if (!toCheck.startsWith("#")) {
      return false;
    }

    final int digits = toCheck.length() - 1;

    if (digits == 3 || digits == 4 || digits == 6 || digits == 8) {
      return checkSyntax(toCheck);
    }

    return false;
  }

  /**
   * Check if the color text is written in the short form with alpha. The text
   * would have to look like this: {@code #rgba}
   *
   * @param toCheck the text to check
   * @return {@code true} in case the text is a short form color definition
   * with alpha
   */
  public static boolean isShortMode(@Nullable final String toCheck) {
    return isColor(toCheck, 4);
  }

  /**
   * Check if the color text is written in the short form without alpha. The
   * text would have to look like this: {@code #rgb}
   *
   * @param toCheck the text to check
   * @return {@code true} in case the text is a short form color definition
   * without alpha
   */
  public static boolean isShortModeWithoutAlpha(@Nullable final String toCheck) {
    return isColor(toCheck, 3);
  }

  /**
   * Check if the color text is written in the long form with alpha. The text
   * would have to look like this: {@code #rrggbbaa}
   *
   * @param toCheck the text to check
   * @return {@code true} in case the text is a long form color definition
   * with alpha
   */
  public static boolean isLongMode(@Nullable final String toCheck) {
    return isColor(toCheck, 8);
  }

  /**
   * Check if the color text is written in the long form without alpha. The text
   * would have to look like this: {@code #rrggbb}
   *
   * @param toCheck the text to check
   * @return {@code true} in case the text is a long form color definition
   * without alpha
   */
  public static boolean isLongModeWithoutAlpha(@Nullable final String toCheck) {
    return isColor(toCheck, 6);
  }

  /**
   * Test of a text contains a color definition with a specified amount of color
   * digits.
   *
   * @param toCheck    the text to check
   * @param components the amount of digits to be used to define the color
   * @return {@code true} if the text fits a color text and meets the
   * required length exactly
   */
  private static boolean isColor(@Nullable final String toCheck, final int components) {
    return (toCheck != null && toCheck.length() == (components + 1) && checkSyntax(toCheck));
  }

  /**
   * Check if the general syntax of the color string fits. That test does not
   * validate that the string has the required length.
   *
   * @param toCheck the text to check
   * @return {@code true} in case the text matches the required syntax
   */
  private static boolean checkSyntax(@Nonnull final String toCheck) {
    return colorPattern.matcher(toCheck).matches();
  }

  private ColorValidator() {
  }
}

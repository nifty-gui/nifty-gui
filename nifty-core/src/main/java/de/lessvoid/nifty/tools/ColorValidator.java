package de.lessvoid.nifty.tools;

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
  private static final Pattern colorPattern = Pattern.compile("#\\p{XDigit}{3,8}");

  /**
   * Check if a string fits any type of color definition string.
   * 
   * @param toCheck
   *          the text to check
   * @return <code>true</code> in case the text is a color definition
   */
  public boolean isValid(final String toCheck) {
    if (toCheck == null) {
      return false;
    }

    return colorPattern.matcher(toCheck).matches();
  }

  /**
   * Check if the color text is written in the short form with alpha. The text
   * would have to look like this: <code>#rgba</code>
   * 
   * @param toCheck
   *          the text to check
   * @return <code>true</code> in case the text is a short form color definition
   *         with alpha
   */
  public boolean isShortMode(final String toCheck) {
    return isColor(toCheck, 4);
  }

  /**
   * Check if the color text is written in the short form without alpha. The
   * text would have to look like this: <code>#rgb</code>
   * 
   * @param toCheck
   *          the text to check
   * @return <code>true</code> in case the text is a short form color definition
   *         without alpha
   */
  public boolean isShortModeWithoutAlpha(final String toCheck) {
    return isColor(toCheck, 3);
  }

  /**
   * Check if the color text is written in the long form with alpha. The text
   * would have to look like this: <code>#rrggbbaa</code>
   * 
   * @param toCheck
   *          the text to check
   * @return <code>true</code> in case the text is a long form color definition
   *         with alpha
   */
  public boolean isLongMode(final String toCheck) {
    return isColor(toCheck, 8);
  }

  /**
   * Check if the color text is written in the long form without alpha. The text
   * would have to look like this: <code>#rrggbb</code>
   * 
   * @param toCheck
   *          the text to check
   * @return <code>true</code> in case the text is a long form color definition
   *         without alpha
   */
  public boolean isLongModeWithoutAlpha(final String toCheck) {
    return isColor(toCheck, 6);
  }

  /**
   * Test of a text contains a color definition with a specified amount of color
   * digits.
   * 
   * @param toCheck
   *          the text to check
   * @param components
   *          the amount of digits to be used to define the color
   * @return <code>true</code> if the text fits a color text and meets the
   *         required length exactly
   */
  private boolean isColor(final String toCheck, final int components) {
    return (toCheck.length() == (components + 1) && isValid(toCheck));
  }
}

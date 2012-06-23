package de.lessvoid.nifty.tools;

import de.lessvoid.nifty.tools.Color;

/**
 * Checks a String if it contains a Nifty inline color. This class is used while
 * parsing text strings for colors.
 * 
 * This has now been changed to not return an Result instance anymore but simply
 * remembers the last result in instance variables. This makes the class not
 * thread-safe but it was not shared between threads anyway.
 * 
 * @author void
 */
public class ColorValueParser {
  private boolean isColor;
  private int nextIndex;
  private Color color;

  public ColorValueParser() {
    setNoResult();
  }

  public boolean isColor() {
    return isColor;
  }

  public int getNextIndex() {
    return nextIndex;
  }

  public Color getColor() {
    return color;
  }

  public boolean isColor(final String text, final int startIdx) {
    if (text.startsWith("\\#", startIdx)) {
      int endIdx = text.indexOf('#', startIdx + 2);
      if (endIdx != -1) {
        setResult(text.substring(startIdx + 1, endIdx), endIdx + 1);
        return isColor;
      }
    }
    setNoResult();
    return false;
  }

  private void setNoResult() {
    nextIndex = -1;
    color = null;
    isColor = false;
  }

  private void setResult(final String value, final int endIdx) {
    nextIndex = -1;
    color = null;
    isColor = Color.check(value);
    if (isColor) {
      color = new Color(value);
      nextIndex = endIdx;
    }
  }
}

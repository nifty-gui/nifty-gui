package de.lessvoid.nifty.tools;

/**
 * This checks a given String that represents a color for being valid. Supported
 * are both short mode "#f12f" and long mode "#ff1122ff"
 * @author void
 */
public class ColorValidator {
  public boolean isValid(final String toCheck) {
    if (toCheck == null) {
      return false;
    }
    if (isShortMode(toCheck)) {
      return true;
    }
    if (isLongMode(toCheck)) {
      return true;
    }
    return false;
  }

  public boolean isShortMode(final String toCheck) {
    return toCheck.matches("#[0-9a-fA-F]{4}");
  }  

  public boolean isShortModeWithoutAlpha(final String toCheck) {
    return toCheck.matches("#[0-9a-fA-F]{3}");
  }  

  public boolean isLongMode(final String toCheck) {
    return toCheck.matches("#[0-9a-fA-F]{8}");
  }

  public boolean isLongModeWithoutAlpha(final String toCheck) {
    return toCheck.matches("#[0-9a-fA-F]{6}");
  }
}

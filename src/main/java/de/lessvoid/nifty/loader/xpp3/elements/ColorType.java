package de.lessvoid.nifty.loader.xpp3.elements;

import de.lessvoid.nifty.tools.Color;

/**
 * ColorType.
 * @author void
 */
public class ColorType {

  /**
   * actual value.
   */
  private String value;

  /**
   * Construct new ColorType.
   * @param input input string
   */
  public ColorType(final String input) {
    value = input;
  }

  /**
   * default constructor.
   */
  public ColorType() {
    this.value = null;
  }

  /**
   * Check if valid color.
   * @return true if valid, false if not
   */
  public boolean isValid() {
    if (value == null) {
      return false;
    }

    if (value.matches("#[a-z,0-9]{8,8}")) {
      return true;
    }

    if (value.matches("#[a-z,0-9]{4,4}")) {
      return true;
    }

    return false;
  }

  /**
   * create nifty color from data.
   * @return color.
   */
  public Color createColor() {
    if (value == null) {
      return null;
    }
    return new Color(value);
  }
}

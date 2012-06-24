package de.lessvoid.nifty.tools;

public final class StringHelper {
  private StringHelper() {
  }

  /**
   * output length whitespaces.
   * @param length number of whitespaces
   * @return string with whitespaces length times.
   */
  public static String whitespace(final int length) {
    StringBuilder b = new StringBuilder();
    for (int i = 0; i < length; i++) {
      b.append(" ");
    }
    return b.toString();
  }

}

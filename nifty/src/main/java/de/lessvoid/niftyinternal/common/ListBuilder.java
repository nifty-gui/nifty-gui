package de.lessvoid.niftyinternal.common;

import java.util.List;


public class ListBuilder {

  private ListBuilder() {
  }

  /**
   * Make a comma separated String from the list of given Strings.
   * @param values the values to convert into a comma separated String
   * @return the String
   */
  public static String makeString(final List<?> values){
    StringBuilder result = new StringBuilder("[");
    boolean first = true;
    for (Object value : values) {
      if (!first) {
        result.append(", ");
      }
      if (first) {
        first = false;
      }
      result.append(value);
    }
    result.append("]");
    return result.toString();
  }
}

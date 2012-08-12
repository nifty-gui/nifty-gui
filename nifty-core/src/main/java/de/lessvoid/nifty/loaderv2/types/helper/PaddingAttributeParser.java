package de.lessvoid.nifty.loaderv2.types.helper;

/**
 * one value:    [applied to all]
 * two values:   [top and bottom], [left and right]
 * three values: [top], [left and right], [bottom]
 * four values:  [top], [right], [bottom], [left]
 * @author void
 */
public class PaddingAttributeParser {
  private final String left;
  private final String right;
  private final String top;
  private final String bottom;

  public PaddingAttributeParser(final String input) throws Exception {
    if (input == null) {
      throw new Exception("parsing error, paddingString is null");
    }

    String[] values = input.split(",");
    if (values == null || values.length == 0) {
      throw new Exception("parsing error, paddingString is empty");
    }

    int valueCount = values.length;
    if (valueCount == 1) {
      if (values[0].length() == 0) {
        throw new Exception("parsing error, paddingString is empty");
      }
      left = values[0];
      right = values[0];
      top = values[0];
      bottom = values[0];
    } else if (valueCount == 2) {
      left = values[1];
      right = values[1];
      top = values[0];
      bottom = values[0];
    } else if (valueCount == 3) {
      left = values[1];
      right = values[1];
      top = values[0];
      bottom = values[2];
    } else if (valueCount == 4) {
      left = values[3];
      right = values[1];
      top = values[0];
      bottom = values[2];
    } else {
      throw new Exception("parsing error, paddingString count error (" + valueCount + ") expecting value from 1 to 4");
    }
  }

  public String getLeft() {
    return left;
  }

  public String getTop() {
    return top;
  }

  public String getRight() {
    return right;
  }

  public String getBottom() {
    return bottom;
  }
}

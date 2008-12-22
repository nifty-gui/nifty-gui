package de.lessvoid.nifty.loader.xpp3.elements.helper;


public class PaddingAttributeParser {
  private String paddingLeft;
  private String paddingRight;
  private String paddingTop;
  private String paddingBottom;

  public PaddingAttributeParser(final String paddingString) throws Exception {
    if (paddingString == null) {
      throw new Exception("parsing error, paddingString is null");
    }

    String[] values = paddingString.split(",");
    if (values == null || values.length == 0) {
      throw new Exception("parsing error, paddingString is empty");
    }

    int valueCount = values.length;
    if (valueCount == 1) {
      if (values[0].length() == 0) {
        throw new Exception("parsing error, paddingString is empty");
      }
      paddingLeft = values[0];
      paddingRight = values[0];
      paddingTop = values[0];
      paddingBottom = values[0];
    } else if (valueCount == 2) {
      paddingLeft = values[1];
      paddingRight = values[1];
      paddingTop = values[0];
      paddingBottom = values[0];
    } else if (valueCount == 3) {
      paddingLeft = values[1];
      paddingRight = values[1];
      paddingTop = values[0];
      paddingBottom = values[2];
    } else if (valueCount == 4) {
      paddingLeft = values[3];
      paddingRight = values[1];
      paddingTop = values[0];
      paddingBottom = values[2];
    } else {
      throw new Exception("parsing error, paddingString count error (" + valueCount + ") expecting value from 1 to 4");
    }
  }

  public String getPaddingLeft() {
    return paddingLeft;
  }

  public String getPaddingTop() {
    return paddingTop;
  }

  public String getPaddingRight() {
    return paddingRight;
  }

  public String getPaddingBottom() {
    return paddingBottom;
  }
}

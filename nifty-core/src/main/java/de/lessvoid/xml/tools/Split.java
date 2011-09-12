package de.lessvoid.xml.tools;

import java.util.ArrayList;
import java.util.List;

public class Split {
  private static final String BEGIN_KEY = "${";
  private static final String END_KEY = "}";

  public static List<String> split(final String value) {
    List<String> result = new ArrayList<String>();
    if (value == null) {
      return result;
    }

    String remaining = value;
    int startIdx = 0;
    while (true) {
      remaining = remaining.substring(startIdx);
      if (remaining.length() == 0) {
        break;
      }

      boolean parsingKey = remaining.startsWith(BEGIN_KEY);
      int endIdx = findEndIdx(remaining, parsingKey);
      if (endIdx == -1) {
        result.add(remaining);
        break;
      }

      if (parsingKey) {
        result.add(remaining.substring(0, endIdx + 1));
        startIdx = endIdx + 1;
      } else {
        result.add(remaining.substring(0, endIdx));
        startIdx = endIdx;
      }
    }

    return result;
  }

  public static String join(final List<String> parts) {
    StringBuffer result = new StringBuffer();
    for (String part : parts) {
      result.append(part);
    }
    return result.toString();
  }

  private static int findEndIdx(final String remaining, final boolean parsingKey) {
    if (parsingKey) {
      return remaining.indexOf(END_KEY);
    } else {
      return remaining.indexOf(BEGIN_KEY);
    }
  }
}

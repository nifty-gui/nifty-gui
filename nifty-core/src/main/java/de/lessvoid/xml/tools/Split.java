package de.lessvoid.xml.tools;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Split {
  private static final String BEGIN_KEY = "${";
  private static final String END_KEY = "}";

  public static boolean containsKey(@Nullable final String value) {
    if (value == null) {
      return false;
    }
    return value.contains(BEGIN_KEY);
  }

  @Nonnull
  public static List<String> split(@Nullable final String value) {

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

  @Nonnull
  public static String join(@Nonnull final List<String> parts) {
    StringBuilder result = new StringBuilder();
    for (String part : parts) {
      result.append(part);
    }
    return result.toString();
  }

  private static int findEndIdx(@Nonnull final String remaining, final boolean parsingKey) {
    if (parsingKey) {
      return remaining.indexOf(END_KEY);
    } else {
      return remaining.indexOf(BEGIN_KEY);
    }
  }
}

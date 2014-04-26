package de.lessvoid.xml.xpp3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControlParameter {
  private Pattern pattern = Pattern.compile(".*\\$\\((.*)\\).*");

  public boolean isParameter(final String value) {
    if (value.startsWith("${")) {
      return false;
    }
    if (value.startsWith("$")) {
      return true;
    }
    return pattern.matcher(value).find();
  }

  public String extractParameter(final String value) {
    Matcher matcher = pattern.matcher(value);
    if (matcher.find()) {
      return matcher.group(1);
    }
    if (value.startsWith("$")) {
      return value.replaceFirst("\\$", "");
    }
    return value;
  }

  public String applyParameter(final String originalValue, final String key, final String value) {
    return originalValue.replaceAll("\\$" + key, value).replaceAll("\\$\\(" + key + "\\)", value);
  }
}

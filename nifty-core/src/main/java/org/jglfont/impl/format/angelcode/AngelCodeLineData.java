package org.jglfont.impl.format.angelcode;

import java.util.Hashtable;
import java.util.Map;

/**
 * LineData
 * @author void
 */
public class AngelCodeLineData {
  private Map<String, String> map = new Hashtable<String, String>();

  public void put(final String key, final String value) {
    map.put(key, value);
  }

  public String getString(final String key) {
    return map.get(key);
  }

  public int getInt(final String key) {
    String value = map.get(key);
    if (value == null) {
      return 0;
    }
    try {
      return Integer.valueOf(value);
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  public void clear() {
    map.clear();
  }

  public boolean hasValue(final String key) {
    return map.containsKey(key);
  }

  public String toString() {
    return map.toString();
  }
}
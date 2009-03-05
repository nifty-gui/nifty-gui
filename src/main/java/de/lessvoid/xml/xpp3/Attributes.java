package de.lessvoid.xml.xpp3;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;

/**
 * XPP Attributes in a nicer form.
 * @author void
 */
public class Attributes {
  private Map < String, String > attributes = new Hashtable < String, String >();

  public Attributes() {
  }

  /**
   * Get Attributes from XmlParser.
   * @param xpp xpp
   */
  public Attributes(final XmlPullParser xpp) {
    initAttributes(xppToMap(xpp));
  }

  /**
   * copy constructor.
   * @param source source
   */
  public Attributes(final Attributes source) {
    attributes = new Hashtable < String, String >();
    attributes.putAll(source.attributes);
  }

  /**
   * Is the given attribute available.
   * @param name name
   * @return true if available false otherwise
   */
  public boolean isSet(final String name) {
    return attributes.get(name) != null;
  }

  /**
   * Return the attribute with the given name.
   * @param name name of attibute
   * @return value
   */
  public String get(final String name) {
    return attributes.get(name);
  }

  /**
   * Create Properties instance from attributes.
   * @return a filled Properties instance
   */
  public Properties createProperties() {
    Properties props = new Properties();
    props.putAll(attributes);
    return props;
  }

  /**
   * Get as Boolean helper.
   * @param name name of attribute
   * @param defaultValue default value
   * @return boolen value
   */
  public boolean getAsBoolean(final String name, final boolean defaultValue) {
    String value = get(name);
    if (value == null) {
      return defaultValue;
    }
    if (value.equals("true")) {
      return true;
    } else if (value.equals("false")) {
      return false;
    }
    return defaultValue;
  }

  /**
   * get as integer.
   * @param name name
   * @return Integer
   */
  public Integer getAsInteger(final String name) {
    String value = get(name);
    if (value == null) {
      return null;
    }
    return Integer.valueOf(value);
  }

  /**
   * Set an attribute.
   * @param name name of the attribute to set
   * @param value value to set attribute to
   */
  public void set(final String name, final String value) {
    attributes.put(name, value);
  }

  /**
   * merge.
   * @param src source attributes
   */
  public void merge(final Attributes src) {
    Map < String, String > srcAttributes = src.attributes;

    Set < String > srcKeys = srcAttributes.keySet();
    for (String srcKey : srcKeys) {
      if (!attributes.containsKey(srcKey)) {
        attributes.put(srcKey, srcAttributes.get(srcKey));
      }
    }
  }

  private Map < String, String > xppToMap(final XmlPullParser xpp) {
    Map < String, String > result = new Hashtable < String, String >();
    for (int i = 0; i < xpp.getAttributeCount(); i++) {
      String key = xpp.getAttributeName(i);
      String value = xpp.getAttributeValue(i);
      result.put(key, value);
    }
    return result;
  }

  private void initAttributes(final Map < String, String > source) {
    attributes = new Hashtable < String, String >();

    Set < Map.Entry < String, String >> entries = source.entrySet();
    for (Map.Entry < String, String > entry : entries) {
      String key = entry.getKey();
      String value = entry.getValue();
      attributes.put(key, value);
    }
  }

  public String toString() {
    String result = "";
    boolean first = true;
    for (String key : attributes.keySet()) {
      if (!first) {
        result += ", ";
      }
      if (first) {
        first = false;
      }
      result += key + " => " + attributes.get(key);
    }
    return result;
  }

  public Properties extractParameters() {
    Properties parameters = new Properties();

    for (Map.Entry < String, String > entry : attributes.entrySet()) {
      String key = entry.getKey(); // like key="$value"
      String value = entry.getValue();
      if (value.startsWith("$")) {
        parameters.put(value.replaceFirst("\\$", ""), key);
      }
    }

    return parameters;
  }

  public Map < String, String > getAttributes() {
    return attributes;
  }

  public void remove(final String key) {
    attributes.remove(key);
  }
}

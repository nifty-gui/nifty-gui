package de.lessvoid.nifty.loader.xpp3;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;

import de.lessvoid.nifty.loader.xpp3.elements.AlignType;
import de.lessvoid.nifty.loader.xpp3.elements.HoverFalloffConstraintType;
import de.lessvoid.nifty.loader.xpp3.elements.HoverFalloffType;
import de.lessvoid.nifty.loader.xpp3.elements.LayoutType;
import de.lessvoid.nifty.loader.xpp3.elements.OnClickType;
import de.lessvoid.nifty.loader.xpp3.elements.ValignType;

/**
 * Attributes.
 * @author void
 */
public final class Attributes {

  private Map < String, String > attributes = new Hashtable < String, String >();
  private Map < String, String > parameters = new Hashtable < String, String >();

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
    parameters = new Hashtable < String, String >();
    attributes.putAll(source.attributes);
    parameters.putAll(source.parameters);
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
   * Overwrite key with value.
   * @param key key
   * @param value value
   */
  public void overwriteAttribute(final String key, final String value) {
    attributes.put(key, value);
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
   * OnClickType.
   * @param name name
   * @return instance
   */
  public OnClickType getAsOnClickType(final String name) {
    String value = get(name);
    if (value == null) {
      return null;
    }
    return new OnClickType(value);
  }

  /**
   * HoverFalloffType.
   * @param name name
   * @return instance
   */
  public HoverFalloffType getAsHoverFalloffType(final String name) {
    String value = get(name);
    if (value == null) {
      return null;
    }
    return HoverFalloffType.valueOf(value);
  }

  /**
   * HoverFalloffConstraintType.
   * @param name name
   * @return instance
   */
  public HoverFalloffConstraintType getAsHoverFalloffConstraintType(final String name) {
    String value = get(name);
    if (value == null) {
      return null;
    }
    return HoverFalloffConstraintType.valueOf(value);
  }

  /**
   * get as boolean.
   * @param name name
   * @return boolean value
   */
  public Boolean getAsBoolean(final String name) {
    String value = get(name);
    if (value == null) {
      return null;
    }
    if (value.equals("true")) {
      return new Boolean(true);
    } else if (value.equals("false")) {
      return new Boolean(false);
    }
    return null;
  }

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
   * Get value from attributes as AlignType.
   * @param name attribute name
   * @return AlignType
   */
  public AlignType getAsAlignType(final String name) {
    String value = get(name);
    if (value == null) {
      return null;
    }
    return AlignType.valueOf(value);
  }

  /**
   * Get value from attributes as VAlignType.
   * @param name attribute name
   * @return ValignType
   */
  public ValignType getAsVAlignType(final String name) {
    String value = get(name);
    if (value == null) {
      return null;
    }
    return ValignType.valueOf(value);
  }

  /**
   * LayoutType.
   * @param name name
   * @return LayoutType
   */
  public LayoutType getAsLayoutType(final String name) {
    String value = get(name);
    if (value == null) {
      return null;
    }
    return LayoutType.valueOf(value);
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
   * this looks for all attributes with a value of $name.
   * @return map with attributes
   */
  public Map < String, String > getParameterAttributes() {
    return parameters;
  }

  /**
   * merge.
   * @param src source attributes
   */
  public void merge(final Attributes src) {
    attributes.putAll(src.attributes);
    parameters.putAll(src.parameters);
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
    parameters = new Hashtable < String, String >();

    Set < Map.Entry < String, String >> entries = source.entrySet();
    for (Map.Entry < String, String > entry : entries) {
      String key = entry.getKey();
      String value = entry.getValue();
      if (value.startsWith("$")) {
        parameters.put(key, value.replaceFirst("\\$", ""));
        value = "";
      }
      attributes.put(key, value);
    }
  }
}

package de.lessvoid.nifty.loader.xpp3;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.xmlpull.v1.XmlPullParser;

/**
 * Attributes.
 * @author void
 */
public final class Attributes {

  /**
   * attribute map.
   */
  private Map < String, String > attributes = new Hashtable < String, String >();

  /**
   * Get Attributes from XmlParser.
   * @param xpp xpp
   */
  public Attributes(final XmlPullParser xpp) {
    this.attributes = getAttributes(xpp);
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
   * Get Attributes as Map.
   * @param xpp xpp
   * @return Attributes as Map
   */
  private static Map < String, String > getAttributes(final XmlPullParser xpp) {
    Map < String, String > attributeMap = new Hashtable < String, String >();
    for (int i = 0; i < xpp.getAttributeCount(); i++) {
      attributeMap.put(xpp.getAttributeName(i), xpp.getAttributeValue(i));
    }
    return attributeMap;
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

}

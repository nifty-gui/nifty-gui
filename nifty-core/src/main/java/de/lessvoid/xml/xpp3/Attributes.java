package de.lessvoid.xml.xpp3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;

import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.xml.tools.SpecialValuesReplace;

/**
 * XPP Attributes in a nicer form.
 * @author void
 */
// TODO: This class should be modified to implement the Map<String, String> interface (renaming all concerned method),
// and should not expose its attributes member anymore.
public class Attributes {
  private Map < String, String > attributes = new HashMap < String, String >();
  private Map < String, Set < String >> taggedAttributes = new HashMap < String, Set < String >>();

  public Attributes() {
  }

  public Attributes(final String ... values) {
    for (int i=0; i<values.length/2; i++) {
      set(values[i*2+0], values[i*2+1]);
    }
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
    attributes = new HashMap < String, String >();
    attributes.putAll(source.attributes);

    taggedAttributes = new HashMap < String, Set < String >>();
    taggedAttributes.putAll(source.taggedAttributes);
  }

  public void translateSpecialValues(final Map<String, ResourceBundle> resourceBundle, final ScreenController screenController, final Properties globalProperties) {
    for (Map.Entry<String, String> value : attributes.entrySet()) {
      attributes.put(value.getKey(), SpecialValuesReplace.replace(value.getValue(), resourceBundle, screenController, globalProperties));
    }
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

  public String getWithDefault(final String name, final String defaultValue) {
    String value = attributes.get(name);
    if (value == null) {
      value = defaultValue;
    }
    return value;
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

  public Integer getAsInteger(final String name) {
    String value = get(name);
    if (value == null) {
      return null;
    }
    return Integer.valueOf(value);
  }

  public Integer getAsInteger(final String name, final int defaultValue) {
    String value = get(name);
    if (value == null) {
      return defaultValue;
    }
    return Integer.valueOf(value);
  }

  public Float getAsFloat(final String name) {
    String value = get(name);
    if (value == null) {
      return null;
    }
    return Float.valueOf(value);
  }

  public Color getAsColor(final String name) {
    String value = get(name);
    if (value == null) {
      return null;
    }
    return new Color(value);
  }

  /**
   * Set an attribute.
   * @param name name of the attribute to set
   * @param value value to set attribute to
   */
  public void set(final String name, final String value) {
    setAttribute(name, value);
  }

  public void merge(final Attributes src) {
    Map < String, String > srcAttributes = src.attributes;

    Set < String > srcKeys = srcAttributes.keySet();
    for (String srcKey : srcKeys) {
      if (!attributes.containsKey(srcKey)) {
        attributes.put(srcKey, srcAttributes.get(srcKey));

        for (Map.Entry < String, Set < String >> tag : src.taggedAttributes.entrySet()) {
          if (tag.getValue().contains(srcKey)) {
            Set < String > attribForTag = taggedAttributes.get(tag.getKey());
            if (attribForTag == null) {
              attribForTag = new HashSet < String >();
              taggedAttributes.put(tag.getKey(), attribForTag);
            }
            attribForTag.add(srcKey);
          }
        }
      }
    }
  }

  public void mergeAndTag(final Attributes src, final String tag) {
    Map < String, String > srcAttributes = src.attributes;

    Set < String > srcKeys = srcAttributes.keySet();
    for (String srcKey : srcKeys) {
      String value = srcAttributes.get(srcKey);

      // you can only overwrite keys when they don't exist yet
      if (!attributes.containsKey(srcKey)) {
        setAttribute(srcKey, value);
        tagAttribute(srcKey, tag);
      }
    }
  }

  private void tagAttribute(final String srcKey, final String tag) {
    Set < String > attribForTag = taggedAttributes.get(tag);
    if (attribForTag == null) {
      attribForTag = new HashSet < String >();
      taggedAttributes.put(tag, attribForTag);
    }
    attribForTag.add(srcKey);
  }

  public void refreshFromAttributes(final Attributes src) {
    Map < String, String > srcAttributes = src.attributes;

    Set < String > srcKeys = srcAttributes.keySet();
    for (String srcKey : srcKeys) {
      String srcValue = srcAttributes.get(srcKey);
      if (srcValue.equals("")) {
        // this key should be replaced
        attributes.remove(srcKey);
      } else {
        attributes.put(srcKey, srcAttributes.get(srcKey));
      }
    }
  }

  private Map < String, String > xppToMap(final XmlPullParser xpp) {
    Map < String, String > result = new HashMap < String, String >();
    for (int i = 0; i < xpp.getAttributeCount(); i++) {
      String key = xpp.getAttributeName(i);
      String value = xpp.getAttributeValue(i);
      result.put(key, value);
    }
    return result;
  }

  private void initAttributes(final Map < String, String > source) {
    attributes = new HashMap < String, String >();

    Set < Map.Entry < String, String >> entries = source.entrySet();
    for (Map.Entry < String, String > entry : entries) {
      String key = entry.getKey();
      String value = entry.getValue();
      setAttribute(key, value);
    }
  }

  private void setAttribute(final String key, final String value) {
    attributes.put(key, value);
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

      String tag = resolveTag(key);
      if (tag != null) {
        result += " {" + tag + "}";
      }
    }
    return result;
  }

  private String resolveTag(final String key) {
    for (Map.Entry < String, Set < String >> tag : taggedAttributes.entrySet()) {
      if (tag.getValue().contains(key)) {
        return tag.getKey();
      }
    }
    return null;
  }

  public class Entry {
    private String key;
    private String value;

    private Entry(final String key, final String value) {
      this.key = key;
      this.value = value;
    }

    public String getKey() {
      return key;
    }

    public String getValue() {
      return value;
    }
  }

  public List<Entry> extractParameters() {
    List<Entry> parameters = new ArrayList<Entry>();

    for (Map.Entry < String, String > entry : attributes.entrySet()) {
      String key = entry.getKey(); // like key="$value"
      String value = entry.getValue();
      if (isParameterDefinition(value)) {
        parameters.add(new Entry(value.replaceFirst("\\$", ""), key));
      }
    }

    return parameters;
  }

  private boolean isParameterDefinition(final String value) {
    return !value.startsWith("${") && (value.startsWith("$"));
  }

  public Map < String, String > getAttributes() {
    return attributes;
  }

  public void remove(final String key) {
    attributes.remove(key);
  }

  public String getWithTag(final String name, final String tag) {
    Set < String > attributesWithTag = taggedAttributes.get(tag);
    if (attributesWithTag == null) {
      return null;
    }
    if (!attributesWithTag.contains(name)) {
      return null;
    }
    return attributes.get(name);
  }

  public void resolveParameters(final Attributes attributes) {
    List<Entry> entrySet = getParameterSet();
    for (Entry entry : entrySet) {
      String key = entry.getKey();
      String value = entry.getValue();

      // first check the given attributes and then check our own
      if (attributes.isSet(key)) {
        set(value, attributes.get(key));
      } else if (isSet(key) && !isParameterDefinition(get(key))) {
        set(value, get(key));
      } else {
        remove(value);
      }
    }
  }

  List<Entry> getParameterSet() {
    return extractParameters();
  }

  public void removeWithTag(final String tag) {
    Set < String > tagged = taggedAttributes.get(tag);
    if (tagged != null) {
      for (String key : tagged) {
        remove(key);
      }
    }
  }
}

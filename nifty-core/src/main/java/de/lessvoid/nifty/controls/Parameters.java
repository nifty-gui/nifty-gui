package de.lessvoid.nifty.controls;

import java.util.Properties;

import de.lessvoid.nifty.tools.Color;

/**
 * All attributes that have been applied to a controlDefinition or the control for that controlDefinition.
 * Please note: some methods in here are duplicates with a different name. This is for easier migration from 1.3.x. I've
 * marked these @Deprecated .
 * @author void
 */
public class Parameters {
  private Properties parameters = new Properties();

  public Parameters(final Properties properties) {
    parameters.putAll(properties);
  }

  /**
   * Is the given attribute available.
   * @param name name
   * @return true if available false otherwise
   */
  public boolean isSet(final String name) {
    return parameters.get(name) != null;
  }

  /**
   * Is the given attribute available.
   * @param name name
   * @return true if available false otherwise
   * @Deprecated use {@link isSet(String)}
   */
  @Deprecated
  public boolean containsKey(final String name) {
    return isSet(name);
  }

  /**
   * Return the attribute with the given name.
   * @param name name of attibute
   * @return value
   */
  public String get(final String name) {
    return parameters.getProperty(name);
  }

  /**
   * Return the attribute with the given name but use defaultValue when it is not available.
   * @param name the name of the attribute
   * @param defaultValue the defaultValue to apply when the attribute is not set
   * @return the value for the attribute or defaultValue if name is not set
   */
  public String getWithDefault(final String name, final String defaultValue) {
    String value = parameters.getProperty(name);
    if (value == null) {
      value = defaultValue;
    }
    return value;
  }

  /**
   * Return the attribute with the given name.
   * @param name name of attibute
   * @return value
   * @Deprecated use {@link #get(String)}
   */
  @Deprecated
  public String getProperty(final String name) {
    return get(name);
  }

  /**
   * Return the attribute with the given name but use defaultValue when it is not available.
   * @param name the name of the attribute
   * @param defaultValue the defaultValue to apply when the attribute is not set
   * @return the value for the attribute or defaultValue if name is not set
   * @Deprecated use {@link #getWithDefault(String, String)}
   */
  @Deprecated
  public String getProperty(final String name, final String defaultValue) {
    return getWithDefault(name, defaultValue);
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
   * Get parameter as Integer.
   * @param name the name of the attribute
   * @return the Integer value of the attributes value
   */
  public Integer getAsInteger(final String name) {
    String value = get(name);
    if (value == null) {
      return null;
    }
    return Integer.valueOf(value);
  }

  /**
   * Get parameter as Integer, using defaultValue when the value does not exist.
   * @param name the name of the attribute
   * @defaultValue the defaultValue to use when the attribute does not exist
   * @return the Integer value of the attributes value or the defaultValue when it not exists
   */
  public Integer getAsInteger(final String name, final int defaultValue) {
    String value = get(name);
    if (value == null) {
      return defaultValue;
    }
    return Integer.valueOf(value);
  }

  /**
   * Get parameter as Float.
   * @param name the name of the attribute
   * @return the Float value of the attributes value
   */
  public Float getAsFloat(final String name) {
    String value = get(name);
    if (value == null) {
      return null;
    }
    return Float.valueOf(value);
  }

  /**
   * Get parameter as Color instance.
   * @param name the name of the attribute
   * @return the Color value of the attributes value
   */
  public Color getAsColor(final String name) {
    String value = get(name);
    if (value == null) {
      return null;
    }
    return new Color(value);
  }
}

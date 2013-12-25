package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Properties;

/**
 * All attributes that have been applied to a controlDefinition or the control for that controlDefinition.
 * Please note: some methods in here are duplicates with a different name. This is for easier migration from 1.3.x. I've
 * marked these @Deprecated .
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class Parameters {
  @Nonnull
  private final Properties parameters = new Properties();

  public Parameters(@Nonnull final Properties properties) {
    parameters.putAll(properties);
  }

  /**
   * Is the given attribute available.
   *
   * @param name name
   * @return true if available false otherwise
   */
  public boolean isSet(@Nonnull final String name) {
    return parameters.get(name) != null;
  }

  /**
   * Is the given attribute available.
   *
   * @param name name
   * @return true if available false otherwise
   * @deprecated use {@link #isSet(String)}
   */
  @Deprecated
  public boolean containsKey(@Nonnull final String name) {
    return isSet(name);
  }

  /**
   * Return the attribute with the given name.
   *
   * @param name name of attribute
   * @return value
   */
  @Nullable
  public String get(@Nonnull final String name) {
    return parameters.getProperty(name);
  }

  /**
   * Return the attribute with the given name but use defaultValue when it is not available.
   *
   * @param name         the name of the attribute
   * @param defaultValue the defaultValue to apply when the attribute is not set
   * @return the value for the attribute or defaultValue if name is not set
   */
  @Nonnull
  public String getWithDefault(@Nonnull final String name, @Nonnull final String defaultValue) {
    String value = get(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  /**
   * Return the attribute with the given name.
   *
   * @param name name of attribute
   * @return value
   * @deprecated use {@link #get(String)}
   */
  @Nullable
  @Deprecated
  public String getProperty(@Nonnull final String name) {
    return get(name);
  }

  /**
   * Return the attribute with the given name but use defaultValue when it is not available.
   *
   * @param name         the name of the attribute
   * @param defaultValue the defaultValue to apply when the attribute is not set
   * @return the value for the attribute or defaultValue if name is not set
   * @deprecated use {@link #getWithDefault(String, String)}
   */
  @Nonnull
  @Deprecated
  public String getProperty(@Nonnull final String name, @Nonnull final String defaultValue) {
    return getWithDefault(name, defaultValue);
  }

  /**
   * Get as Boolean helper.
   *
   * @param name         name of attribute
   * @param defaultValue default value
   * @return boolean value
   */
  public boolean getAsBoolean(@Nonnull final String name, final boolean defaultValue) {
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
   *
   * @param name the name of the attribute
   * @return the Integer value of the attributes value
   */
  @Nullable
  public Integer getAsInteger(@Nonnull final String name) {
    String value = get(name);
    if (value == null) {
      return null;
    }
    try {
      return Integer.valueOf(value);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  /**
   * Get parameter as Integer, using defaultValue when the value does not exist.
   *
   * @param name         the name of the attribute
   * @param defaultValue the defaultValue to use when the attribute does not exist
   * @return the Integer value of the attributes value or the defaultValue when it not exists
   */
  public int getAsInteger(@Nonnull final String name, final int defaultValue) {
    Integer result = getAsInteger(name);
    if (result == null) {
      return defaultValue;
    }
    return result;
  }

  /**
   * Get parameter as Float.
   *
   * @param name the name of the attribute
   * @return the Float value of the attributes value
   */
  @Nullable
  public Float getAsFloat(@Nonnull final String name) {
    String value = get(name);
    if (value == null) {
      return null;
    }
    try {
      return Float.valueOf(value);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  /**
   * Get parameter as Float.
   *
   * @param name         the name of the attribute
   * @param defaultValue the defaultValue to use when the attribute does not exist
   * @return the Float value of the attributes value
   */
  public float getAsFloat(@Nonnull final String name, final float defaultValue) {
    Float result = getAsFloat(name);
    if (result == null) {
      return defaultValue;
    }
    return result;
  }

  /**
   * Get parameter as Color instance.
   *
   * @param name the name of the attribute
   * @return the Color value of the attributes value
   */
  @Nullable
  public Color getAsColor(@Nonnull final String name) {
    String value = get(name);
    if (value == null) {
      return null;
    }
    if (Color.check(name)) {
      return new Color(value);
    }
    return null;
  }

  /**
   * Get parameter as Color instance.
   *
   * @param name         the name of the attribute
   * @param defaultValue the defaultValue to use when the attribute does not exist
   * @return the Color value of the attributes value
   */
  @Nonnull
  public Color getAsColor(@Nonnull final String name, @Nonnull final Color defaultValue) {
    Color result = getAsColor(name);
    if (result == null) {
      return defaultValue;
    }
    return result;
  }

  /**
   * Get the parameter as a instance of a enumeration member.
   *
   * @param name      the name of the attribute
   * @param enumClass the enumeration class
   * @param <T>       the enumeration type
   * @return the enumeration instance the parameter was mapped to or {@code null} if the mapping failed
   */
  @Nullable
  public <T extends Enum<T>> T getAsEnum(@Nonnull final String name, @Nonnull final Class<T> enumClass) {
    String value = get(name);
    if (value == null) {
      return null;
    }
    try {
      return Enum.valueOf(enumClass, value);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  /**
   * Get the parameter as a instance of a enumeration member.
   *
   * @param name         the name of the attribute
   * @param enumClass    the enumeration class
   * @param defaultValue the value returned in case the mapping failed
   * @param <T>          the enumeration type
   * @return the enumeration instance the parameter was mapped to or the default value in case the mapping failed
   */
  @Nonnull
  public <T extends Enum<T>> T getAsEnum(
      @Nonnull final String name,
      @Nonnull final Class<T> enumClass,
      @Nonnull T defaultValue) {
    T result = getAsEnum(name, enumClass);
    if (result == null) {
      return defaultValue;
    }
    return result;
  }
}

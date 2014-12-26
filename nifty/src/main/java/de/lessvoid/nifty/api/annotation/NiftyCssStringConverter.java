package de.lessvoid.nifty.api.annotation;

/**
 * You'll need to provide an implementation of this interface when you annotate methods with @NiftyCssProperty
 * that are not String properties. This interface will convert the actual values to Strings and back.
 *
 * This is necessary since CSS files only contain String properties.
 *
 * @author void
 */
public interface NiftyCssStringConverter<E> {
  /**
   * Take the given value and convert it to whatever type you need.
   * @param value the String value
   * @return the actual converted value
   */
  E fromString(String value);

  /**
   * Take the given value and return a String representation.
   * @param value the value
   * @return the String value
   */
  String toString(E value);
}

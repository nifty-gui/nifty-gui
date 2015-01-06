package de.lessvoid.xml.tools;

import java.util.Locale;

/**
 * A Helper to load ResourceBundles from baseNames AND from already loaded ResourceBundles.
 * @author void
 */
public interface BundleInfo {

  /**
   * Get a String from this BundleInfo.
   *
   * @param resourceKey the resource key
   * @param locale the Locale
   * @return the String from the resourceBundle
   */
  String getString(final String resourceKey, final Locale locale);
}

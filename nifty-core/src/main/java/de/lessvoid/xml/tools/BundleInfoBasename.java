package de.lessvoid.xml.tools;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A BundleInfo that will use a basename to resolve a ResourceBundle. 
 * @author void
 */
public class BundleInfoBasename implements BundleInfo {
  private String baseName;

  public BundleInfoBasename(final String baseName) {
    this.baseName = baseName;
  }

  @Override
  public String getString(final String resourceKey, final Locale locale) {
    ResourceBundle res;
    if (locale == null) {
      res = ResourceBundle.getBundle(baseName);
    } else {
      res = ResourceBundle.getBundle(baseName, locale);
    }
    return res.getString(resourceKey);
  }
}

package de.lessvoid.xml.tools;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * A BundleInfo that will use an already existing ResourceBundle. 
 * @author void
 */
public class BundleInfoResourceBundle implements BundleInfo {
  private final ResourceBundle defaultResourceBundle;
  private Map<Locale, ResourceBundle> resourceBundles = new HashMap<Locale, ResourceBundle>();

  public BundleInfoResourceBundle(final ResourceBundle resourceBundle) {
    this.defaultResourceBundle = resourceBundle;
    resourceBundles.put(resourceBundle.getLocale(), resourceBundle);
  }

  public void add(final ResourceBundle resourceBundle) {
    resourceBundles.put(resourceBundle.getLocale(), resourceBundle);
  }

  @Override
  public String getString(final String resourceKey, final Locale locale) {
    ResourceBundle res;
    if (locale == null) {
      res = defaultResourceBundle;
    } else {
      res = resourceBundles.get(locale);
      if (res == null) {
        res = defaultResourceBundle;
      }
    }
    return res.getString(resourceKey);
  }
}

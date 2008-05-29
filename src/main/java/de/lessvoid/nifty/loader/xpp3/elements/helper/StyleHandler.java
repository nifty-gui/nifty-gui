package de.lessvoid.nifty.loader.xpp3.elements.helper;

import java.util.HashMap;
import java.util.Map;

import de.lessvoid.nifty.loader.xpp3.elements.StyleType;

/**
 * StyleHandler.
 * @author void
 */
public class StyleHandler {

  /**
   * style hash map.
   */
  private Map < String, StyleType > styles = new HashMap < String, StyleType >();

  /**
   * register style.
   * @param styleType styleType to register
   */
  public void register(final StyleType styleType) {
    styles.put(styleType.getId(), styleType);
  }

  /**
   * get style with the given id.
   * @param styleId styleId to get
   * @return style or null
   */
  public StyleType getStyle(final String styleId) {
    return styles.get(styleId);
  }
}

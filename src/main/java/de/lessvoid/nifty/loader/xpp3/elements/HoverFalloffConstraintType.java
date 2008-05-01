package de.lessvoid.nifty.loader.xpp3.elements;

/**
 * HoverFalloffConstraintType.
 * @author void
 */
public enum HoverFalloffConstraintType {
  /**
   * none.
   */
  none("none"),

  /**
   * vertical.
   */
  vertical("vertical"),

  /**
   * horizontal.
   */
  horizontal("horizontal");

  /**
   * actual value.
   */
  private String value;

  /**
   * Create HoverFalloffConstraintType.
   * @param valueParam param
   */
  private HoverFalloffConstraintType(final String valueParam) {
    this.value = valueParam;
  }

}

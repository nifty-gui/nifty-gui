package de.lessvoid.nifty.loader.xpp3.elements;

/**
 * HoverFalloffType.
 * @author void
 */
public enum HoverFalloffType {
  /**
   * none.
   */
  none("none"),

  /**
   * linear.
   */
  linear("linear");

  /**
   * actual value.
   */
  private String value;

  /**
   * Create HoverFalloffType.
   * @param valueParam param
   */
  private HoverFalloffType(final String valueParam) {
    this.value = valueParam;
  }

}

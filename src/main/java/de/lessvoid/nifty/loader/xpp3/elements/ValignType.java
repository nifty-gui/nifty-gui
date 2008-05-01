package de.lessvoid.nifty.loader.xpp3.elements;

/**
 * ValignType.
 * @author void
 */
public enum ValignType {
  /**
   * top.
   */
  top("top"),

  /**
   * center.
   */
  center("center"),

  /**
   * bottom.
   */
  bottom("bottom");

  /**
   * actual value.
   */
  private String value;

  /**
   * Create AlignType.
   * @param valueParam param
   */
  private ValignType(final String valueParam) {
    this.value = valueParam;
  }

  /**
   * get value.
   * @return value
   */
  public String getValue() {
    return this.value;
  }
}

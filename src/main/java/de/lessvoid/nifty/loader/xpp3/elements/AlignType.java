package de.lessvoid.nifty.loader.xpp3.elements;

/**
 * AlignType enum.
 * @author void
 */
public enum AlignType {
  /**
   * left.
   */
  left("left"),

  /**
   * center.
   */
  center("center"),

  /**
   * right.
   */
  right("right");

  /**
   * actual value.
   */
  private String value;

  /**
   * Create AlignType.
   * @param valueParam param
   */
  private AlignType(final String valueParam) {
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

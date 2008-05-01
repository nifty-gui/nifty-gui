package de.lessvoid.nifty.loader.xpp3.elements;

/**
 * SubImageSizeModeType.
 * @author void
 */
public enum SubImageSizeModeType {
  /**
   * scale.
   */
  scale("scale"),

  /**
   * resizeHint.
   */
  resizeHint("resizeHint");

  /**
   * actual value.
   */
  private String value;

  /**
   * SubImageSizeModeType.
   * @param valueParam value
   */
  private SubImageSizeModeType(final String valueParam) {
    this.value = valueParam;
  }
}

package de.lessvoid.nifty.loader.xpp3.elements;

/**
 * LayerGroupType.
 * @author void
 */
public class ScreenTypeLayerGroupType {

  /**
   * id.
   * @required
   */
  private String id;

  /**
   * alternate id.
   * @optional
   */
  private String alternate;

  /**
   * @param idParam id
   */
  public ScreenTypeLayerGroupType(final String idParam) {
    this.id = idParam;
  }

  /**
   * set alternate.
   * @param alternateParam alternate
   */
  public void setAlternate(final String alternateParam) {
    this.alternate = alternateParam;
  }

}

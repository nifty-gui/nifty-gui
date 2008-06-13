package de.lessvoid.nifty.layout;

import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * The BoxConstraints class represent a rectangular area on the screen.
 * It has a position (x,y) as well as height and weight attributes.
 *
 * @author void
 */
public class BoxConstraints {

  /**
   * Horizontal Position Constraint of the box.
   */
  private SizeValue x;

  /**
   * Vertical Position Constraint of the box.
   */
  private SizeValue y;

  /**
   * Width Constraint of the box.
   */
  private SizeValue width;

  /**
   * Height Constraint of the box.
   */
  private SizeValue height;

  /**
   * Horizontal Alignment Constrain.
   */
  private HorizontalAlign horizontalAlign;

  /**
   * Vertical Alignment Constrain.
   */
  private VerticalAlign verticalAlign;

  /**
   * default constructor.
   */
  public BoxConstraints() {
    this.x = null;
    this.y = null;
    this.width = null;
    this.height = null;
    this.horizontalAlign = HorizontalAlign.left;
    this.verticalAlign = VerticalAlign.top;
  }

  /**
   * create new BoxConstraints.
   * @param newX x
   * @param newY y
   * @param newWidth width
   * @param newHeight height
   * @param newHorizontalAlign horizontal align
   * @param newVerticalAlign vertical align
   */
  public BoxConstraints(
      final SizeValue newX,
      final SizeValue newY,
      final SizeValue newWidth,
      final SizeValue newHeight,
      final HorizontalAlign newHorizontalAlign,
      final VerticalAlign newVerticalAlign) {
    this.x = newX;
    this.y = newY;
    this.width = newWidth;
    this.height = newHeight;
    this.horizontalAlign = newHorizontalAlign;
    this.verticalAlign = newVerticalAlign;
  }

  /**
   * copy constructor.
   * @param src source instance to copy from
   */
  public BoxConstraints(final BoxConstraints src) {
    this.x = src.x;
    this.y = src.y;
    this.width = src.width;
    this.height = src.height;
    this.horizontalAlign = src.horizontalAlign;
    this.verticalAlign = src.verticalAlign;
  }

  /**
   * Get the horizontal position constraint of the box.
   * @return the horizontal position of the box
   */
  public final SizeValue getX() {
    return x;
  }

  /**
   * Get the horizontal position constraint of the box.
   * @param newX the vertical position of the box
   */
  public final void setX(final SizeValue newX) {
    this.x = newX;
  }

  /**
   * Get the vertical position constraint of the box.
   * @return the vertical position of the box
   */
  public final SizeValue getY() {
    return y;
  }

  /**
   * Set the vertical position constraint of the box.
   * @param newY the vertical position of the box
   */
  public final void setY(final SizeValue newY) {
    this.y = newY;
  }

  /**
   * Get the current height constraint for the box.
   * @return the current height of the box
   */
  public final SizeValue getHeight() {
    return height;
  }

  /**
   * Set a new height constraint for the box.
   * @param newHeight the new height for the box.
   */
  public final void setHeight(final SizeValue newHeight) {
    this.height = newHeight;
  }

  /**
   * Get the current width constraint of the box.
   * @return the current width of the box
   */
  public final SizeValue getWidth() {
    return width;
  }

  /**
   * Set a new width constraint for the box.
   * @param newWidth the new width
   */
  public final void setWidth(final SizeValue newWidth) {
    this.width = newWidth;
  }

  /**
   * Get the current horizontal align.
   * @return the current horizontal align.
   */
  public final HorizontalAlign getHorizontalAlign() {
    return horizontalAlign;
  }

  /**
   * Set a new horizontal align.
   * @param newHorizontalAlign the new horizontal align
   */
  public final void setHorizontalAlign(
      final HorizontalAlign newHorizontalAlign
      ) {
    this.horizontalAlign = newHorizontalAlign;
  }

  /**
   * Get the current VerticalAlign.
   * @return the current VerticalAlign
   */
  public final VerticalAlign getVerticalAlign() {
    return verticalAlign;
  }

  /**
   * Set a new VerticalAlign.
   * @param newVerticalAlign the new vertical align
   */
  public final void setVerticalAlign(final VerticalAlign newVerticalAlign) {
    this.verticalAlign = newVerticalAlign;
  }

}

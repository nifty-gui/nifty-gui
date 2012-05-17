package de.lessvoid.nifty.layout;

import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * The BoxConstraints class represent a rectangular area on the screen.
 * It has a position (x,y) as well as height and width attributes.
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
   * Horizontal Alignment Constraint.
   */
  private HorizontalAlign horizontalAlign;

  /**
   * Vertical Alignment Constraint.
   */
  private VerticalAlign verticalAlign;

  private SizeValue paddingLeft;
  private SizeValue paddingRight;
  private SizeValue paddingTop;
  private SizeValue paddingBottom;

  private SizeValue marginLeft;
  private SizeValue marginRight;
  private SizeValue marginTop;
  private SizeValue marginBottom;

  /**
   * default constructor.
   */
  public BoxConstraints() {
    this.x = null;
    this.y = null;
    this.width = null;
    this.height = null;
    this.horizontalAlign = HorizontalAlign.horizontalDefault;
    this.verticalAlign = VerticalAlign.verticalDefault;
    paddingLeft = new SizeValue("0px");
    paddingRight = new SizeValue("0px");
    paddingTop = new SizeValue("0px");
    paddingBottom = new SizeValue("0px");
    marginLeft = new SizeValue("0px");
    marginRight = new SizeValue("0px");
    marginTop = new SizeValue("0px");
    marginBottom = new SizeValue("0px");
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
    this();
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
  public SizeValue getX() {
    return x;
  }

  /**
   * Get the horizontal position constraint of the box.
   * @param newX the horizontal position of the box
   */
  public void setX(final SizeValue newX) {
    this.x = newX;
  }

  /**
   * Get the vertical position constraint of the box.
   * @return the vertical position of the box
   */
  public SizeValue getY() {
    return y;
  }

  /**
   * Set the vertical position constraint of the box.
   * @param newY the vertical position of the box
   */
  public void setY(final SizeValue newY) {
    this.y = newY;
  }

  /**
   * Get the current height constraint for the box.
   * @return the current height of the box
   */
  public SizeValue getHeight() {
    return height;
  }

  /**
   * Set a new height constraint for the box.
   * @param newHeight the new height for the box.
   */
  public void setHeight(final SizeValue newHeight) {
    this.height = newHeight;
  }

  /**
   * Get the current width constraint of the box.
   * @return the current width of the box
   */
  public SizeValue getWidth() {
    return width;
  }

  /**
   * Set a new width constraint for the box.
   * @param newWidth the new width
   */
  public void setWidth(final SizeValue newWidth) {
    this.width = newWidth;
  }

  /**
   * Get the current horizontal align.
   * @return the current horizontal align.
   */
  public HorizontalAlign getHorizontalAlign() {
    return horizontalAlign;
  }

  /**
   * Set a new horizontal align.
   * @param newHorizontalAlign the new horizontal align
   */
  public void setHorizontalAlign(final HorizontalAlign newHorizontalAlign) {
    this.horizontalAlign = newHorizontalAlign;
  }

  /**
   * Get the current VerticalAlign.
   * @return the current VerticalAlign
   */
  public VerticalAlign getVerticalAlign() {
    return verticalAlign;
  }

  /**
   * Set a new VerticalAlign.
   * @param newVerticalAlign the new vertical align
   */
  public void setVerticalAlign(final VerticalAlign newVerticalAlign) {
    this.verticalAlign = newVerticalAlign;
  }

  public SizeValue getPaddingLeft() {
    return paddingLeft;
  }

  public SizeValue getPaddingRight() {
    return paddingRight;
  }

  public SizeValue getPaddingTop() {
    return paddingTop;
  }

  public SizeValue getPaddingBottom() {
    return paddingBottom;
  }

  public void setPaddingLeft(final SizeValue paddingLeftParam) {
    paddingLeft = paddingLeftParam;
  }

  public void setPaddingRight(final SizeValue paddingRightParam) {
    paddingRight = paddingRightParam;
  }

  public void setPaddingTop(final SizeValue paddingTopParam) {
    paddingTop = paddingTopParam;
  }

  public void setPaddingBottom(final SizeValue paddingBottomParam) {
    paddingBottom = paddingBottomParam;
  }

  public void setPadding(final SizeValue topBottomParam, final SizeValue leftRightParam) {
    paddingLeft = leftRightParam;
    paddingRight = leftRightParam;
    paddingTop = topBottomParam;
    paddingBottom = topBottomParam;
  }

  public void setPadding(final SizeValue topParam, final SizeValue leftRightParam, final SizeValue bottomParam) {
    paddingLeft = leftRightParam;
    paddingRight = leftRightParam;
    paddingTop = topParam;
    paddingBottom = bottomParam;
  }

  public void setPadding(
      final SizeValue topParam,
      final SizeValue rightParam,
      final SizeValue bottomParam,
      final SizeValue leftParam) {
    paddingLeft = leftParam;
    paddingRight = rightParam;
    paddingTop = topParam;
    paddingBottom = bottomParam;
  }

  public void setPadding(final SizeValue padding) {
    paddingLeft = padding;
    paddingRight = padding;
    paddingTop = padding;
    paddingBottom = padding;
  }

  public SizeValue getMarginLeft() {
    return marginLeft;
  }

  public SizeValue getMarginRight() {
    return marginRight;
  }

  public SizeValue getMarginTop() {
    return marginTop;
  }

  public SizeValue getMarginBottom() {
    return marginBottom;
  }

  public void setMarginLeft(final SizeValue marginLeftParam) {
    marginLeft = marginLeftParam;
  }

  public void setMarginRight(final SizeValue marginRightParam) {
    marginRight = marginRightParam;
  }

  public void setMarginTop(final SizeValue marginTopParam) {
    marginTop = marginTopParam;
  }

  public void setMarginBottom(final SizeValue marginBottomParam) {
    marginBottom = marginBottomParam;
  }

  public void setMargin(final SizeValue topBottomParam, final SizeValue leftRightParam) {
    marginLeft = leftRightParam;
    marginRight = leftRightParam;
    marginTop = topBottomParam;
    marginBottom = topBottomParam;
  }

  public void setMargin(final SizeValue topParam, final SizeValue leftRightParam, final SizeValue bottomParam) {
    marginLeft = leftRightParam;
    marginRight = leftRightParam;
    marginTop = topParam;
    marginBottom = bottomParam;
  }

  public void setMargin(
      final SizeValue topParam,
      final SizeValue rightParam,
      final SizeValue bottomParam,
      final SizeValue leftParam) {
    marginLeft = leftParam;
    marginRight = rightParam;
    marginTop = topParam;
    marginBottom = bottomParam;
  }

  public void setMargin(final SizeValue margin) {
    marginLeft = margin;
    marginRight = margin;
    marginTop = margin;
    marginBottom = margin;
  }
}

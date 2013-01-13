package de.lessvoid.niftyimpl.layout;

import de.lessvoid.nifty.layout.HorizontalAlign;
import de.lessvoid.nifty.layout.SizeValue;
import de.lessvoid.nifty.layout.VerticalAlign;

/**
 * The BoxConstraints class represent constraints that will be used when a Layoutable is being layout. 
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

  /**
   * Left padding.
   */
  private SizeValue paddingLeft;

  /**
   * Right padding.
   */
  private SizeValue paddingRight;

  /**
   * Top padding.
   */
  private SizeValue paddingTop;

  /**
   * Bottom padding.
   */
  private SizeValue paddingBottom;

  /**
   * Left margin.
   */
  private SizeValue marginLeft;

  /**
   * Right margin.
   */
  private SizeValue marginRight;

  /**
   * Top margin.
   */
  private SizeValue marginTop;

  /**
   * Bottom margin.
   */
  private SizeValue marginBottom;

  /**
   * default constructor.
   */
  public BoxConstraints() {
    x = null;
    y = null;
    width = null;
    height = null;
    horizontalAlign = HorizontalAlign.horizontalDefault;
    verticalAlign = VerticalAlign.verticalDefault;
    paddingLeft = SizeValue.px(0);
    paddingRight = SizeValue.px(0);
    paddingTop = SizeValue.px(0);
    paddingBottom = SizeValue.px(0);
    marginLeft = SizeValue.px(0);
    marginRight = SizeValue.px(0);
    marginTop = SizeValue.px(0);
    marginBottom = SizeValue.px(0);
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
    this.paddingLeft = src.paddingLeft;
    this.paddingRight = src.paddingRight;
    this.paddingTop = src.paddingTop;
    this.paddingBottom = src.paddingBottom;
    this.marginLeft = src.marginLeft;
    this.marginRight = src.marginRight;
    this.marginTop = src.marginTop;
    this.marginBottom = src.marginBottom;
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

  /**
   * Get Left padding.
   * @return left padding
   */
  public SizeValue getPaddingLeft() {
    return paddingLeft;
  }

  /**
   * Set Left padding.
   * @param paddingLeftParam left padding
   */
  public void setPaddingLeft(final SizeValue paddingLeftParam) {
    paddingLeft = paddingLeftParam;
  }

  /**
   * Get Right padding.
   * @return right padding
   */
  public SizeValue getPaddingRight() {
    return paddingRight;
  }

  /**
   * Set right padding.
   * @param right padding
   */
  public void setPaddingRight(final SizeValue paddingRightParam) {
    paddingRight = paddingRightParam;
  }

  /**
   * Get Top padding.
   * @return top padding
   */
  public SizeValue getPaddingTop() {
    return paddingTop;
  }

  /**
   * Set Top padding.
   * @param top padding
   */
  public void setPaddingTop(final SizeValue paddingTopParam) {
    paddingTop = paddingTopParam;
  }

  /**
   * Get Bottom padding.
   * @return bottom padding
   */
  public SizeValue getPaddingBottom() {
    return paddingBottom;
  }

  /**
   * Set Bottom padding.
   * @param bottom padding.
   */
  public void setPaddingBottom(final SizeValue paddingBottomParam) {
    paddingBottom = paddingBottomParam;
  }

  /**
   * Set all padding values from two parameters.
   *
   * @param topBottomParam top and bottom padding
   * @param leftRightParam left and right padding
   */
  public void setPadding(final SizeValue topBottomParam, final SizeValue leftRightParam) {
    paddingLeft = leftRightParam;
    paddingRight = leftRightParam;
    paddingTop = topBottomParam;
    paddingBottom = topBottomParam;
  }

  /**
   * Set all padding values from three parameters.
   *
   * @param topParam top padding
   * @param leftRightParam left and right padding
   * @param bottomParam bottom padding
   */
  public void setPadding(final SizeValue topParam, final SizeValue leftRightParam, final SizeValue bottomParam) {
    paddingLeft = leftRightParam;
    paddingRight = leftRightParam;
    paddingTop = topParam;
    paddingBottom = bottomParam;
  }

  /**
   * Set all padding values from four individual parameters.
   *
   * @param topParam top padding
   * @param rightParam right padding
   * @param bottomParam bottom padding
   * @param leftParam left padding
   */
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

  /**
   * Set all padding values to the same value.
   * @param padding padding value that will be applied to all four paddings (top, right, bottom, left)
   */
  public void setPadding(final SizeValue padding) {
    paddingLeft = padding;
    paddingRight = padding;
    paddingTop = padding;
    paddingBottom = padding;
  }

  /**
   * Get left margin.
   * @return left margin value
   */
  public SizeValue getMarginLeft() {
    return marginLeft;
  }

  /**
   * Set left margin.
   * @param marginLeftParam left margin value
   */
  public void setMarginLeft(final SizeValue marginLeftParam) {
    marginLeft = marginLeftParam;
  }

  /**
   * Get right margin.
   * @return right margin value
   */
  public SizeValue getMarginRight() {
    return marginRight;
  }

  /**
   * Set right margin.
   * @param marginRightParam right margin value
   */
  public void setMarginRight(final SizeValue marginRightParam) {
    marginRight = marginRightParam;
  }

  /**
   * Get top margin.
   * @return top margin value
   */
  public SizeValue getMarginTop() {
    return marginTop;
  }

  /**
   * Set top margin value.
   * @param marginTopParam top margin value
   */
  public void setMarginTop(final SizeValue marginTopParam) {
    marginTop = marginTopParam;
  }

  /**
   * Get bottom margin.
   * @return bottom margin value
   */
  public SizeValue getMarginBottom() {
    return marginBottom;
  }

  /**
   * Set bottom margin.
   * @param marginBottomParam bottom margin value
   */
  public void setMarginBottom(final SizeValue marginBottomParam) {
    marginBottom = marginBottomParam;
  }

  /**
   * Set all margin values from two values.
   *
   * @param topBottomParam top and bottom margin value
   * @param leftRightParam left and right margin value
   */
  public void setMargin(final SizeValue topBottomParam, final SizeValue leftRightParam) {
    marginLeft = leftRightParam;
    marginRight = leftRightParam;
    marginTop = topBottomParam;
    marginBottom = topBottomParam;
  }

  /**
   * Set all margin values from three values.
   *
   * @param topParam top margin
   * @param leftRightParam left and right margin
   * @param bottomParam bottom margin
   */
  public void setMargin(final SizeValue topParam, final SizeValue leftRightParam, final SizeValue bottomParam) {
    marginLeft = leftRightParam;
    marginRight = leftRightParam;
    marginTop = topParam;
    marginBottom = bottomParam;
  }

  /**
   * Set margin values from four individual values.
   * @param topParam top margin value
   * @param rightParam right margin value
   * @param bottomParam bottom margin value
   * @param leftParam left margin value
   */
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

  /**
   * Set all margin values to the same value.
   *
   * @param margin the margin value to set all margin values to.
   */
  public void setMargin(final SizeValue margin) {
    marginLeft = margin;
    marginRight = margin;
    marginTop = margin;
    marginBottom = margin;
  }
}

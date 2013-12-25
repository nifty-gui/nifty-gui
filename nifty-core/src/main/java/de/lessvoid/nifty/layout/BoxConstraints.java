package de.lessvoid.nifty.layout;

import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;

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
  @Nonnull
  private SizeValue x;

  /**
   * Vertical Position Constraint of the box.
   */
  @Nonnull
  private SizeValue y;

  /**
   * Width Constraint of the box.
   */
  @Nonnull
  private SizeValue width;

  /**
   * Height Constraint of the box.
   */
  @Nonnull
  private SizeValue height;

  /**
   * Horizontal Alignment Constraint.
   */
  @Nonnull
  private HorizontalAlign horizontalAlign;

  /**
   * Vertical Alignment Constraint.
   */
  @Nonnull
  private VerticalAlign verticalAlign;

  @Nonnull
  private SizeValue paddingLeft;
  @Nonnull
  private SizeValue paddingRight;
  @Nonnull
  private SizeValue paddingTop;
  @Nonnull
  private SizeValue paddingBottom;

  @Nonnull
  private SizeValue marginLeft;
  @Nonnull
  private SizeValue marginRight;
  @Nonnull
  private SizeValue marginTop;
  @Nonnull
  private SizeValue marginBottom;

  /**
   * default constructor.
   */
  public BoxConstraints() {
    this.x = SizeValue.def();
    this.y = SizeValue.def();
    this.width = SizeValue.def();
    this.height = SizeValue.def();
    this.horizontalAlign = HorizontalAlign.horizontalDefault;
    this.verticalAlign = VerticalAlign.verticalDefault;
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
   *
   * @param newX               x
   * @param newY               y
   * @param newWidth           width
   * @param newHeight          height
   * @param newHorizontalAlign horizontal align
   * @param newVerticalAlign   vertical align
   */
  public BoxConstraints(
      @Nonnull final SizeValue newX,
      @Nonnull final SizeValue newY,
      @Nonnull final SizeValue newWidth,
      @Nonnull final SizeValue newHeight,
      @Nonnull final HorizontalAlign newHorizontalAlign,
      @Nonnull final VerticalAlign newVerticalAlign) {
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
   *
   * @param src source instance to copy from
   */
  public BoxConstraints(@Nonnull final BoxConstraints src) {
    x = src.x;
    y = src.y;
    width = src.width;
    height = src.height;
    horizontalAlign = src.horizontalAlign;
    verticalAlign = src.verticalAlign;
    paddingLeft = src.paddingLeft;
    paddingRight = src.paddingRight;
    paddingTop = src.paddingTop;
    paddingBottom = src.paddingBottom;
    marginLeft = src.marginLeft;
    marginRight = src.marginRight;
    marginTop = src.marginTop;
    marginBottom = src.marginBottom;
  }

  /**
   * Get the horizontal position constraint of the box.
   *
   * @return the horizontal position of the box
   */
  @Nonnull
  public SizeValue getX() {
    return x;
  }

  /**
   * Get the horizontal position constraint of the box.
   *
   * @param newX the horizontal position of the box
   */
  public void setX(@Nonnull final SizeValue newX) {
    this.x = newX;
  }

  /**
   * Get the vertical position constraint of the box.
   *
   * @return the vertical position of the box
   */
  @Nonnull
  public SizeValue getY() {
    return y;
  }

  /**
   * Set the vertical position constraint of the box.
   *
   * @param newY the vertical position of the box
   */
  public void setY(@Nonnull final SizeValue newY) {
    this.y = newY;
  }

  /**
   * Get the current height constraint for the box.
   *
   * @return the current height of the box
   */
  @Nonnull
  public SizeValue getHeight() {
    return height;
  }

  /**
   * Set a new height constraint for the box.
   *
   * @param newHeight the new height for the box.
   */
  public void setHeight(@Nonnull final SizeValue newHeight) {
    this.height = newHeight;
  }

  /**
   * Get the current width constraint of the box.
   *
   * @return the current width of the box
   */
  @Nonnull
  public SizeValue getWidth() {
    return width;
  }

  /**
   * Set a new width constraint for the box.
   *
   * @param newWidth the new width
   */
  public void setWidth(@Nonnull final SizeValue newWidth) {
    this.width = newWidth;
  }

  /**
   * Get the current horizontal align.
   *
   * @return the current horizontal align.
   */
  @Nonnull
  public HorizontalAlign getHorizontalAlign() {
    return horizontalAlign;
  }

  /**
   * Set a new horizontal align.
   *
   * @param newHorizontalAlign the new horizontal align
   */
  public void setHorizontalAlign(@Nonnull final HorizontalAlign newHorizontalAlign) {
    this.horizontalAlign = newHorizontalAlign;
  }

  /**
   * Get the current VerticalAlign.
   *
   * @return the current VerticalAlign
   */
  @Nonnull
  public VerticalAlign getVerticalAlign() {
    return verticalAlign;
  }

  /**
   * Set a new VerticalAlign.
   *
   * @param newVerticalAlign the new vertical align
   */
  public void setVerticalAlign(@Nonnull final VerticalAlign newVerticalAlign) {
    this.verticalAlign = newVerticalAlign;
  }

  @Nonnull
  public SizeValue getPaddingLeft() {
    return paddingLeft;
  }

  @Nonnull
  public SizeValue getPaddingRight() {
    return paddingRight;
  }

  @Nonnull
  public SizeValue getPaddingTop() {
    return paddingTop;
  }

  @Nonnull
  public SizeValue getPaddingBottom() {
    return paddingBottom;
  }

  public void setPaddingLeft(@Nonnull final SizeValue paddingLeftParam) {
    paddingLeft = paddingLeftParam;
  }

  public void setPaddingRight(@Nonnull final SizeValue paddingRightParam) {
    paddingRight = paddingRightParam;
  }

  public void setPaddingTop(@Nonnull final SizeValue paddingTopParam) {
    paddingTop = paddingTopParam;
  }

  public void setPaddingBottom(@Nonnull final SizeValue paddingBottomParam) {
    paddingBottom = paddingBottomParam;
  }

  public void setPadding(@Nonnull final SizeValue topBottomParam, @Nonnull final SizeValue leftRightParam) {
    paddingLeft = leftRightParam;
    paddingRight = leftRightParam;
    paddingTop = topBottomParam;
    paddingBottom = topBottomParam;
  }

  public void setPadding(
      @Nonnull final SizeValue topParam,
      @Nonnull final SizeValue leftRightParam,
      @Nonnull final SizeValue bottomParam) {
    paddingLeft = leftRightParam;
    paddingRight = leftRightParam;
    paddingTop = topParam;
    paddingBottom = bottomParam;
  }

  public void setPadding(
      @Nonnull final SizeValue topParam,
      @Nonnull final SizeValue rightParam,
      @Nonnull final SizeValue bottomParam,
      @Nonnull final SizeValue leftParam) {
    paddingLeft = leftParam;
    paddingRight = rightParam;
    paddingTop = topParam;
    paddingBottom = bottomParam;
  }

  public void setPadding(@Nonnull final SizeValue padding) {
    paddingLeft = padding;
    paddingRight = padding;
    paddingTop = padding;
    paddingBottom = padding;
  }

  @Nonnull
  public SizeValue getMarginLeft() {
    return marginLeft;
  }

  @Nonnull
  public SizeValue getMarginRight() {
    return marginRight;
  }

  @Nonnull
  public SizeValue getMarginTop() {
    return marginTop;
  }

  @Nonnull
  public SizeValue getMarginBottom() {
    return marginBottom;
  }

  public void setMarginLeft(@Nonnull final SizeValue marginLeftParam) {
    marginLeft = marginLeftParam;
  }

  public void setMarginRight(@Nonnull final SizeValue marginRightParam) {
    marginRight = marginRightParam;
  }

  public void setMarginTop(@Nonnull final SizeValue marginTopParam) {
    marginTop = marginTopParam;
  }

  public void setMarginBottom(@Nonnull final SizeValue marginBottomParam) {
    marginBottom = marginBottomParam;
  }

  public void setMargin(@Nonnull final SizeValue topBottomParam, @Nonnull final SizeValue leftRightParam) {
    marginLeft = leftRightParam;
    marginRight = leftRightParam;
    marginTop = topBottomParam;
    marginBottom = topBottomParam;
  }

  public void setMargin(
      @Nonnull final SizeValue topParam,
      @Nonnull final SizeValue leftRightParam,
      @Nonnull final SizeValue bottomParam) {
    marginLeft = leftRightParam;
    marginRight = leftRightParam;
    marginTop = topParam;
    marginBottom = bottomParam;
  }

  public void setMargin(
      @Nonnull final SizeValue topParam,
      @Nonnull final SizeValue rightParam,
      @Nonnull final SizeValue bottomParam,
      @Nonnull final SizeValue leftParam) {
    marginLeft = leftParam;
    marginRight = rightParam;
    marginTop = topParam;
    marginBottom = bottomParam;
  }

  public void setMargin(@Nonnull final SizeValue margin) {
    marginLeft = margin;
    marginRight = margin;
    marginTop = margin;
    marginBottom = margin;
  }
}

/*
 * Copyright (c) 2014, Jens Hohmuth 
 * All rights reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are 
 * met: 
 * 
 *  * Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer. 
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.lessvoid.nifty.internal.layout;

import de.lessvoid.nifty.api.HAlign;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.VAlign;

/**
 * The BoxConstraints class represent constraints that will be used when a Layoutable is being layout. 
 *
 * @author void
 */
public class InternalBoxConstraints {

  // Horizontal Position Constraint of the box.
  private UnitValue x;

  // Vertical Position Constraint of the box.
  private UnitValue y;

  // Width Constraint of the box.
  private UnitValue width;

  // Height Constraint of the box.
  private UnitValue height;

  // Horizontal Alignment Constraint.
  private HAlign horizontalAlign;

  // Vertical Alignment Constraint.
  private VAlign verticalAlign;

  // Left padding.
  private UnitValue paddingLeft;

  // Right padding.
  private UnitValue paddingRight;

  // Top padding.
  private UnitValue paddingTop;

  // Bottom padding.
  private UnitValue paddingBottom;

  // Left margin.
  private UnitValue marginLeft;

  // Right margin.
  private UnitValue marginRight;

  // Top margin.
  private UnitValue marginTop;

  // Bottom margin.
  private UnitValue marginBottom;

  /**
   * default constructor.
   */
  public InternalBoxConstraints() {
    x = null;
    y = null;
    width = null;
    height = null;
    horizontalAlign = HAlign.horizontalDefault;
    verticalAlign = VAlign.verticalDefault;
    paddingLeft = UnitValue.px(0);
    paddingRight = UnitValue.px(0);
    paddingTop = UnitValue.px(0);
    paddingBottom = UnitValue.px(0);
    marginLeft = UnitValue.px(0);
    marginRight = UnitValue.px(0);
    marginTop = UnitValue.px(0);
    marginBottom = UnitValue.px(0);
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
  public InternalBoxConstraints(
      final UnitValue newX,
      final UnitValue newY,
      final UnitValue newWidth,
      final UnitValue newHeight,
      final HAlign newHorizontalAlign,
      final VAlign newVerticalAlign) {
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
  public InternalBoxConstraints(final InternalBoxConstraints src) {
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
  public UnitValue getX() {
    return x;
  }

  /**
   * Get the horizontal position constraint of the box.
   * @param newX the horizontal position of the box
   */
  public void setX(final UnitValue newX) {
    this.x = newX;
  }

  /**
   * Get the vertical position constraint of the box.
   * @return the vertical position of the box
   */
  public UnitValue getY() {
    return y;
  }

  /**
   * Set the vertical position constraint of the box.
   * @param newY the vertical position of the box
   */
  public void setY(final UnitValue newY) {
    this.y = newY;
  }

  /**
   * Get the current height constraint for the box.
   * @return the current height of the box
   */
  public UnitValue getHeight() {
    return height;
  }

  /**
   * Set a new height constraint for the box.
   * @param newHeight the new height for the box.
   */
  public void setHeight(final UnitValue newHeight) {
    this.height = newHeight;
  }

  /**
   * Get the current width constraint of the box.
   * @return the current width of the box
   */
  public UnitValue getWidth() {
    return width;
  }

  /**
   * Set a new width constraint for the box.
   * @param newWidth the new width
   */
  public void setWidth(final UnitValue newWidth) {
    this.width = newWidth;
  }

  /**
   * Get the current horizontal align.
   * @return the current horizontal align.
   */
  public HAlign getHorizontalAlign() {
    return horizontalAlign;
  }

  /**
   * Set a new horizontal align.
   * @param newHorizontalAlign the new horizontal align
   */
  public void setHorizontalAlign(final HAlign newHorizontalAlign) {
    this.horizontalAlign = newHorizontalAlign;
  }

  /**
   * Get the current VerticalAlign.
   * @return the current VerticalAlign
   */
  public VAlign getVerticalAlign() {
    return verticalAlign;
  }

  /**
   * Set a new VerticalAlign.
   * @param newVerticalAlign the new vertical align
   */
  public void setVerticalAlign(final VAlign newVerticalAlign) {
    this.verticalAlign = newVerticalAlign;
  }

  /**
   * Get Left padding.
   * @return left padding
   */
  public UnitValue getPaddingLeft() {
    return paddingLeft;
  }

  /**
   * Set Left padding.
   * @param paddingLeftParam left padding
   */
  public void setPaddingLeft(final UnitValue paddingLeftParam) {
    paddingLeft = paddingLeftParam;
  }

  /**
   * Get Right padding.
   * @return right padding
   */
  public UnitValue getPaddingRight() {
    return paddingRight;
  }

  /**
   * Set right padding.
   * @param right padding
   */
  public void setPaddingRight(final UnitValue paddingRightParam) {
    paddingRight = paddingRightParam;
  }

  /**
   * Get Top padding.
   * @return top padding
   */
  public UnitValue getPaddingTop() {
    return paddingTop;
  }

  /**
   * Set Top padding.
   * @param top padding
   */
  public void setPaddingTop(final UnitValue paddingTopParam) {
    paddingTop = paddingTopParam;
  }

  /**
   * Get Bottom padding.
   * @return bottom padding
   */
  public UnitValue getPaddingBottom() {
    return paddingBottom;
  }

  /**
   * Set Bottom padding.
   * @param bottom padding.
   */
  public void setPaddingBottom(final UnitValue paddingBottomParam) {
    paddingBottom = paddingBottomParam;
  }

  /**
   * Set all padding values from two parameters.
   *
   * @param topBottomParam top and bottom padding
   * @param leftRightParam left and right padding
   */
  public void setPadding(final UnitValue topBottomParam, final UnitValue leftRightParam) {
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
  public void setPadding(final UnitValue topParam, final UnitValue leftRightParam, final UnitValue bottomParam) {
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
      final UnitValue topParam,
      final UnitValue rightParam,
      final UnitValue bottomParam,
      final UnitValue leftParam) {
    paddingLeft = leftParam;
    paddingRight = rightParam;
    paddingTop = topParam;
    paddingBottom = bottomParam;
  }

  /**
   * Set all padding values to the same value.
   * @param padding padding value that will be applied to all four paddings (top, right, bottom, left)
   */
  public void setPadding(final UnitValue padding) {
    paddingLeft = padding;
    paddingRight = padding;
    paddingTop = padding;
    paddingBottom = padding;
  }

  /**
   * Get left margin.
   * @return left margin value
   */
  public UnitValue getMarginLeft() {
    return marginLeft;
  }

  /**
   * Set left margin.
   * @param marginLeftParam left margin value
   */
  public void setMarginLeft(final UnitValue marginLeftParam) {
    marginLeft = marginLeftParam;
  }

  /**
   * Get right margin.
   * @return right margin value
   */
  public UnitValue getMarginRight() {
    return marginRight;
  }

  /**
   * Set right margin.
   * @param marginRightParam right margin value
   */
  public void setMarginRight(final UnitValue marginRightParam) {
    marginRight = marginRightParam;
  }

  /**
   * Get top margin.
   * @return top margin value
   */
  public UnitValue getMarginTop() {
    return marginTop;
  }

  /**
   * Set top margin value.
   * @param marginTopParam top margin value
   */
  public void setMarginTop(final UnitValue marginTopParam) {
    marginTop = marginTopParam;
  }

  /**
   * Get bottom margin.
   * @return bottom margin value
   */
  public UnitValue getMarginBottom() {
    return marginBottom;
  }

  /**
   * Set bottom margin.
   * @param marginBottomParam bottom margin value
   */
  public void setMarginBottom(final UnitValue marginBottomParam) {
    marginBottom = marginBottomParam;
  }

  /**
   * Set all margin values from two values.
   *
   * @param topBottomParam top and bottom margin value
   * @param leftRightParam left and right margin value
   */
  public void setMargin(final UnitValue topBottomParam, final UnitValue leftRightParam) {
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
  public void setMargin(final UnitValue topParam, final UnitValue leftRightParam, final UnitValue bottomParam) {
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
      final UnitValue topParam,
      final UnitValue rightParam,
      final UnitValue bottomParam,
      final UnitValue leftParam) {
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
  public void setMargin(final UnitValue margin) {
    marginLeft = margin;
    marginRight = margin;
    marginTop = margin;
    marginBottom = margin;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append("InternalBoxConstraints [x=");
    result.append(x);
    result.append(", y=");
    result.append(y);
    result.append(", width=");
    result.append(width);
    result.append(", height=");
    result.append(height);
    result.append(", horizontalAlign=");
    result.append(horizontalAlign);
    result.append(", verticalAlign=");
    result.append(verticalAlign);
    result.append(", paddingLeft=");
    result.append(paddingLeft);
    result.append(", paddingRight=");
    result.append(paddingRight);
    result.append(", paddingTop=");
    result.append(paddingTop);
    result.append(", paddingBottom=");
    result.append(paddingBottom);
    result.append(", marginLeft=");
    result.append(marginLeft);
    result.append(", marginRight=");
    result.append(marginRight);
    result.append(", marginTop=");
    result.append(marginTop);
    result.append(", marginBottom=");
    result.append(marginBottom);
    result.append("] ");
    result.append(super.toString());
    return result.toString();
  }
}

/*
 * Copyright (c) 2015, Nifty GUI Community 
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
package de.lessvoid.nifty.api;

import de.lessvoid.nifty.api.annotation.NiftyStyleProperty;
import de.lessvoid.nifty.api.controls.NiftyControl;
import de.lessvoid.nifty.api.converter.NiftyStyleStringConverterHAlign;
import de.lessvoid.nifty.api.converter.NiftyStyleStringConverterNiftyColor;
import de.lessvoid.nifty.api.converter.NiftyStyleStringConverterNiftyLinearGradient;
import de.lessvoid.nifty.api.converter.NiftyStyleStringConverterUnitValue;
import de.lessvoid.nifty.api.converter.NiftyStyleStringConverterUnitValueArray;
import de.lessvoid.nifty.api.converter.NiftyStyleStringConverterVAlign;
import de.lessvoid.nifty.api.types.Point;
import de.lessvoid.nifty.internal.InternalNiftyNode;
import de.lessvoid.nifty.internal.accessor.NiftyNodeAccessor;
import de.lessvoid.nifty.internal.math.Vec4;

/**
 * The core element of the Nifty scene graph is a NiftyNode. It is created by the main Nifty instance and represents the
 * hierarchical structure of a Nifty GUI.
 * 
 * @author void
 */
public class NiftyNode {
  private final InternalNiftyNode impl;

  /**
   * Please use one of the {@link Nifty#createRootNode()} methods to create a new NiftyNode. You're not supposed to
   * create an instance of this class directly and you're not supposed to extend from this class.
   */
  private NiftyNode(final InternalNiftyNode impl) {
    this.impl = impl;
    this.impl.setNiftyNode(this);
    this.impl.initDefaultProperties(this);
  }

  /**
   * Get the id of this NiftyNode.
   * @return the id
   */
  @NiftyStyleProperty(name = "id")
  public String getId() {
    return impl.getId();
  }

  /**
   * Get the x position of this node.
   * 
   * @return x position of this node.
   */
  public int getX() {
    return impl.getX();
  }

  /**
   * Get the y position of this node.
   * 
   * @return y y position of this node.
   */
  public int getY() {
    return impl.getY();
  }

  /**
   * Get the width of this node.
   * 
   * @return width width of this element.
   */
  public int getWidth() {
    return impl.getWidth();
  }

  /**
   * Get the height of this node.
   * 
   * @return height height of this node.
   */
  public int getHeight() {
    return impl.getHeight();
  }

  /**
   * Set the horizontal alignment.
   * 
   * @param alignment
   *          the new horizontal alignment
   */
  @NiftyStyleProperty(name = "halign", converter = NiftyStyleStringConverterHAlign.class)
  public void setHAlign(final HAlign alignment) {
    setHAlign(alignment, NiftyNodeState.Regular);
  }

  /**
   * Set the horizontal alignment.
   * 
   * @param alignment
   *          the new horizontal alignment
   * @param states the NiftyNode.State that this should be applied to.
   */
  public void setHAlign(final HAlign alignment, final NiftyNodeState ... states) {
    impl.setHAlign(alignment, states);
  }

  /**
   * Set the vertical alignment.
   * 
   * @param alignment
   *          the new vertical alignment
   */
  @NiftyStyleProperty(name = "valign", converter = NiftyStyleStringConverterVAlign.class)
  public void setVAlign(final VAlign alignment) {
    setVAlign(alignment, NiftyNodeState.Regular);
  }

  /**
   * Set the vertical alignment.
   * 
   * @param alignment
   *          the new vertical alignment
   * @param states the NiftyNode.State that this should be applied to.
   */
  public void setVAlign(final VAlign alignment, final NiftyNodeState ... states) {
    impl.setVAlign(alignment, states);
  }

  /**
   * Set the x constraint of this NiftyNode. Please remember that not all layouts will respect that value.
   * @param value the UnitValue representing the new x position
   */
  public void setXConstraint(final UnitValue value) {
    setXConstraint(value, NiftyNodeState.Regular);
  }

  /**
   * Set the x constraint of this NiftyNode. Please remember that not all layouts will respect that value.
   * @param value the UnitValue representing the new x position
   * @param states the NiftyNode.State that this should be applied to.
   */
  public void setXConstraint(final UnitValue value, final NiftyNodeState ... states) {
    impl.setXConstraint(value, states);
  }

  /**
   * Set the y constraint of this NiftyNode. Please remember that not all layouts will respect that value.
   * @param value the UnitValue representing the new y position
   */
  public void setYConstraint(final UnitValue value) {
    setYConstraint(value, NiftyNodeState.Regular);
  }

  /**
   * Set the y constraint of this NiftyNode. Please remember that not all layouts will respect that value.
   * @param value the UnitValue representing the new y position
   * @param states the NiftyNode.State that this should be applied to.
   */
  public void setYConstraint(final UnitValue value, final NiftyNodeState ... states) {
    impl.setYConstraint(value, states);
  }

  /**
   * Change the width constraint of this NiftyNode forcing it to a certain
   * UnitValue.
   * 
   * @param value
   *          the UnitValue representing the new width
   */
  @NiftyStyleProperty(name = "width", converter = NiftyStyleStringConverterUnitValue.class)
  public void setWidthConstraint(final UnitValue value) {
    setWidthConstraint(value, NiftyNodeState.Regular);
  }

  /**
   * Change the width constraint of this NiftyNode forcing it to a certain
   * UnitValue.
   * 
   * @param value
   *          the UnitValue representing the new width
   * @param states the NiftyNode.State that this should be applied to.
   */
  public void setWidthConstraint(final UnitValue value, final NiftyNodeState ... states) {
    impl.setWidthConstraint(value, states);
  }

  /**
   * Change the height constraint of this NiftyNode forcing it to a certain
   * UnitValue.
   * 
   * @param value
   *          the UnitValue representing the new height
   */
  @NiftyStyleProperty(name = "height", converter = NiftyStyleStringConverterUnitValue.class)
  public void setHeightConstraint(final UnitValue value) {
    setHeightConstraint(value, NiftyNodeState.Regular);
  }

  /**
   * Change the height constraint of this NiftyNode forcing it to a certain
   * UnitValue.
   * 
   * @param value
   *          the UnitValue representing the new height
   * @param states the NiftyNode.State that this should be applied to.
   */
  public void setHeightConstraint(final UnitValue value, final NiftyNodeState ... states) {
    impl.setHeightConstraint(value, states);
  }

  /**
   * Set the ChildLayout for this NiftyNode. The ChildLayout defines the way
   * child element of this node will be layed out.
   * 
   * @param childLayout
   */
  public void setChildLayout(final ChildLayout childLayout) {
    setChildLayout(childLayout, NiftyNodeState.Regular);
  }

  /**
   * Set the ChildLayout for this NiftyNode. The ChildLayout defines the way
   * child element of this node will be layed out.
   * 
   * @param childLayout
   * @param states the NiftyNode.State that this should be applied to.
   */
  public void setChildLayout(final ChildLayout childLayout, final NiftyNodeState ... states) {
    impl.setChildLayout(childLayout, states);
  }

  /**
   * Change the blend mode when rendering this NiftyNode.
   * @param compositeOperation the new BlendMode
   */
  public void setCompositeOperation(final NiftyCompositeOperation compositeOperation) {
    setCompositeOperation(compositeOperation, NiftyNodeState.Regular);
  }

  /**
   * Change the blend mode when rendering this NiftyNode.
   * @param compositeOperation the new BlendMode
   * @param states the NiftyNode.State that this should be applied to.
   */
  public void setCompositeOperation(final NiftyCompositeOperation compositeOperation, final NiftyNodeState ... states) {
    impl.setCompositeOperation(compositeOperation, states);
  }

  /**
   * Get the current active composite operation for this node.
   * @return the current composite operation
   */
  public NiftyCompositeOperation getCompositeOperation() {
    return impl.getCompositeOperation();
  }

  /**
   * Change the background color of this node to a new color. The default value is a fully transparent color.
   *
   * @param color the new background color
   */
  @NiftyStyleProperty(name = "background-color", converter = NiftyStyleStringConverterNiftyColor.class)
  public void setBackgroundColor(final NiftyColor color) {
    setBackgroundColor(color, NiftyNodeState.Regular);
  }

  /**
   * Change the background color of this node to a new color. The default value is a fully transparent color.
   *
   * @param color the new background color
   * @param states the NiftyNode.State that this background color should be applied to. This is optional. If you omit it
   * the standard default state is being modified.
   */
  public void setBackgroundColor(final NiftyColor color, final NiftyNodeState ... states) {
    impl.setBackgroundColor(color, states);
  }

  /**
   * Change the background gradient of this node. The default value is null.
   *
   * @param gradient the new background gradient
   */
  @NiftyStyleProperty(name = "background-gradient", converter = NiftyStyleStringConverterNiftyLinearGradient.class)
  public void setBackgroundGradient(final NiftyLinearGradient gradient) {
    setBackgroundGradient(gradient, NiftyNodeState.Regular);
  }

  /**
   * Change the background gradient of this node. The default value is null.
   *
   * @param gradient the new background gradient
   * @param states the NiftyNode.State that this background gradient should be applied to. This is optional. If you omit
   * it the standard default state is being modified.
   */
  public void setBackgroundGradient(final NiftyLinearGradient gradient, final NiftyNodeState ... states) {
    impl.setBackgroundGradient(gradient, states);
  }

  /**
   * Get the padding string.
   *
   * @return the padding values
   */
  @NiftyStyleProperty(name = "padding", converter = NiftyStyleStringConverterUnitValueArray.class)
  public UnitValue[] getPadding() {
    return impl.getPadding();
  }

  /**
   * Set the padding string to set all padding values in a single call. The format of the padding string is the same
   * as in CSS.
   *
   * one value:    [applied to all]
   * two values:   [top and bottom], [left and right]
   * three values: [top], [left and right], [bottom]
   * four values:  [top], [right], [bottom], [left]
   *
   * @param padding the new top padding
   */
  public void setPadding(final UnitValue ... values) {
    setPadding(values, NiftyNodeState.Regular);
  }

  /**
   * Set the padding string to set all padding values in a single call. The format of the padding string is the same
   * as in CSS.
   *
   * @param padding the new top padding
   * @param states the NiftyNodeStates to apply the top padding to.
   */
  public void setPadding(final UnitValue[] padding, final NiftyNodeState ... states) {
    impl.setPadding(padding, states);
  }

  /**
   * Get the top padding.
   *
   * @return the top padding
   */
  @NiftyStyleProperty(name = "padding-top", converter = NiftyStyleStringConverterUnitValue.class)
  public UnitValue getPaddingTop() {
    return impl.getPaddingTop();
  }

  /**
   * Set the top padding.
   *
   * @param paddingTop the new top padding
   */
  public void setPaddingTop(final UnitValue paddingTop) {
    setPaddingTop(paddingTop, NiftyNodeState.Regular);
  }

  /**
   * Set the top padding.
   *
   * @param paddingTop the new top padding
   * @param states the NiftyNodeStates to apply the top padding to.
   */
  public void setPaddingTop(final UnitValue paddingTop, final NiftyNodeState ... states) {
    impl.setPaddingTop(paddingTop, states);
  }

  /**
   * Get the right padding.
   *
   * @return the right padding
   */
  @NiftyStyleProperty(name = "padding-right", converter = NiftyStyleStringConverterUnitValue.class)
  public UnitValue getPaddingRight() {
    return impl.getPaddingRight();
  }

  /**
   * Set the right padding.
   *
   * @param paddingRight the new right padding
   */
  public void setPaddingRight(final UnitValue paddingRight) {
    setPaddingRight(paddingRight, NiftyNodeState.Regular);
  }

  /**
   * Set the right padding.
   *
   * @param paddingRight the new right padding
   * @param states the NiftyNodeStates to apply the right padding to
   */
  public void setPaddingRight(final UnitValue paddingRight, final NiftyNodeState ... states) {
    impl.setPaddingRight(paddingRight, states);
  }

  /**
   * Get the bottom padding.
   *
   * @return the bottom padding
   */
  @NiftyStyleProperty(name = "padding-bottom", converter = NiftyStyleStringConverterUnitValue.class)
  public UnitValue getPaddingBottom() {
    return impl.getPaddingBottom();
  }

  /**
   * Set the bottom padding.
   *
   * @param paddingBottom the new bottom padding
   */
  public void setPaddingBottom(final UnitValue paddingBottom) {
    setPaddingBottom(paddingBottom, NiftyNodeState.Regular);
  }

  /**
   * Set the bottom padding.
   *
   * @param paddingBottom the new bottom padding
   * @param states the NiftyNodeStates to apply the bottom padding to
   */
  public void setPaddingBottom(final UnitValue paddingBottom, final NiftyNodeState ... states) {
    impl.setPaddingBottom(paddingBottom, states);
  }

  /**
   * Get the left padding.
   *
   * @return the left padding
   */
  @NiftyStyleProperty(name = "padding-left", converter = NiftyStyleStringConverterUnitValue.class)
  public UnitValue getPaddingLeft() {
    return impl.getPaddingLeft();
  }

  /**
   * Set the left padding.
   *
   * @param paddingLeft the new left padding
   */
  public void setPaddingLeft(final UnitValue paddingLeft) {
    setPaddingLeft(paddingLeft, NiftyNodeState.Regular);
  }

  /**
   * Set the left padding.
   *
   * @param paddingLeft the new left padding
   * @param states the NiftyNodeStates to apply the left padding to
   */
  public void setPaddingLeft(final UnitValue paddingLeft, final NiftyNodeState ... states) {
    impl.setPaddingLeft(paddingLeft, states);
  }

  /**
   * Get the margin string.
   *
   * @return the margin values
   */
  @NiftyStyleProperty(name = "margin", converter = NiftyStyleStringConverterUnitValueArray.class)
  public UnitValue[] getMargin() {
    return impl.getMargin();
  }

  /**
   * Set the margin string to set all margin values in a single call. The format of the margin string is the same
   * as in CSS.
   *
   * @param margin the new margin
   */
  public void setMargin(final UnitValue ... margin) {
    setMargin(margin, NiftyNodeState.Regular);
  }

  /**
   * Set the margin string to set all margin values in a single call. The format of the margin string is the same
   * as in CSS.
   *
   * @param margin the new margin
   * @param states the NiftyNodeStates to apply the margin to.
   */
  public void setMargin(final UnitValue[] margin, final NiftyNodeState ... states) {
    impl.setMargin(margin, states);
  }

  /**
   * Get the top margin.
   *
   * @return the top margin
   */
  @NiftyStyleProperty(name = "margin-top", converter = NiftyStyleStringConverterUnitValue.class)
  public UnitValue getMarginTop() {
    return impl.getMarginTop();
  }

  /**
   * Set the top margin.
   *
   * @param marginTop the new top margin
   */
  public void setMarginTop(final UnitValue marginTop) {
    setMarginTop(marginTop, NiftyNodeState.Regular);
  }

  /**
   * Set the top margin.
   *
   * @param marginTop the new top margin
   * @param states the NiftyNodeStates to apply the top margin to
   */
  public void setMarginTop(final UnitValue marginTop, final NiftyNodeState ... states) {
    impl.setMarginTop(marginTop, NiftyNodeState.Regular);
  }

  /**
   * Get the right margin.
   *
   * @return the right margin
   */
  @NiftyStyleProperty(name = "margin-right", converter = NiftyStyleStringConverterUnitValue.class)
  public UnitValue getMarginRight() {
    return impl.getMarginRight();
  }

  /**
   * Set the right margin.
   *
   * @param marginRight the new right margin
   */
  public void setMarginRight(final UnitValue marginRight) {
    setMarginRight(marginRight, NiftyNodeState.Regular);
  }

  /**
   * Set the right margin.
   *
   * @param marginRight the new right margin
   * @param states the NiftyNodeStates to apply the right margin to
   */
  public void setMarginRight(final UnitValue marginRight, final NiftyNodeState ... states) {
    impl.setMarginRight(marginRight, states);
  }

  /**
   * Get the bottom margin.
   *
   * @return the bottom margin
   */
  @NiftyStyleProperty(name = "margin-bottom", converter = NiftyStyleStringConverterUnitValue.class)
  public UnitValue getMarginBottom() {
    return impl.getMarginBottom();
  }

  /**
   * Set the bottom margin.
   *
   * @param marginBottom the new bottom margin
   */
  public void setMarginBottom(final UnitValue marginBottom) {
    setMarginBottom(marginBottom, NiftyNodeState.Regular);
  }

  /**
   * Set the bottom margin.
   *
   * @param marginBottom the new bottom margin
   * @param states the NiftyNodeStates to apply the bottom margin to
   */
  public void setMarginBottom(final UnitValue marginBottom, final NiftyNodeState ... states) {
    impl.setMarginBottom(marginBottom, states);
  }

  /**
   * Get the left margin.
   *
   * @return the left margin
   */
  @NiftyStyleProperty(name = "margin-left", converter = NiftyStyleStringConverterUnitValue.class)
  public UnitValue getMarginLeft() {
    return impl.getMarginLeft();
  }

  /**
   * Set the left margin.
   *
   * @param marginLeft the new left margin
   */
  public void setMarginLeft(final UnitValue marginLeft) {
    setMarginLeft(marginLeft, NiftyNodeState.Regular);
  }

  /**
   * Set the left margin.
   *
   * @param marginLeft the new left margin
   * @param states the NiftyNodeStates to apply the left margin to
   */
  public void setMarginLeft(final UnitValue marginLeft, final NiftyNodeState ... states) {
    impl.setMarginLeft(marginLeft, states);
  }

  /**
   * Get the current background color of this node.
   *
   * @return the currently set background color
   */
  public NiftyColor getBackgroundColor() {
    return impl.getBackgroundColor();
  }

  /**
   * Get the current background gradient of this node.
   *
   * @return the currently set background gradient
   */
  public NiftyLinearGradient getBackgroundGradient() {
    return impl.getBackgroundGradient();
  }

  /**
   * Set the pivot point that will form the base of all calculation. You can think of this as the center. This is in
   * the interval [0.0, 1.0].
   *
   * @param x x
   * @param y y
   */
  public void setPivot(final double x, final double y) {
    impl.setPivot(x, y);
  }

  /**
   * Set rotation around the x-axis in degrees.
   * @param angle rotation angle
   */
  public void setRotationX(final double angle) {
    setRotationX(angle, NiftyNodeState.Regular);
  }

  /**
   * Set rotation around the x-axis in degrees.
   * @param angle rotation angle
   * @param states the NiftyNode.State that this should be applied to. This is optional.
   */
  public void setRotationX(final double angle, final NiftyNodeState ... states) {
    impl.setRotationX(angle, states);
  }

  /**
   * Set rotation around the y-axis in degrees.
   * @param angle rotation angle
   */
  public void setRotationY(final double angle) {
    setRotationY(angle, NiftyNodeState.Regular);
  }

  /**
   * Set rotation around the y-axis in degrees.
   * @param angle rotation angle
   * @param states the NiftyNode.State that this should be applied to. This is optional.
   */
  public void setRotationY(final double angle, final NiftyNodeState ... states) {
    impl.setRotationY(angle, states);
  }

  /**
   * Set rotation around the z-axis in degrees.
   * @param angle rotation angle
   */
  public void setRotationZ(final double angle) {
    setRotationZ(angle, NiftyNodeState.Regular);
  }

  /**
   * Set rotation around the z-axis in degrees.
   * @param angle rotation angle
   * @param states the NiftyNode.State that this should be applied to. This is optional.
   */
  public void setRotationZ(final double angle, final NiftyNodeState ... states) {
    impl.setRotationZ(angle, states);
  }

  /**
   * Set scale for x-axis.
   * @param factor factor
   */
  public void setScaleX(final double factor) {
    setScaleX(factor, NiftyNodeState.Regular);
  }

  /**
   * Set scale for x-axis.
   * @param factor factor
   * @param states the NiftyNode.State that this should be applied to. This is optional.
   */
  public void setScaleX(final double factor, final NiftyNodeState ... states) {
    impl.setScaleX(factor, states);
  }

  /**
   * Set scale for y-axis.
   * @param factor factor
   */
  public void setScaleY(final double factor) {
    setScaleY(factor, NiftyNodeState.Regular);
  }

  /**
   * Set scale for y-axis.
   * @param factor factor
   * @param states the NiftyNode.State that this should be applied to. This is optional.
   */
  public void setScaleY(final double factor, final NiftyNodeState ... states) {
    impl.setScaleY(factor, states);
  }

  /**
   * Set scale for z-axis.
   * @param factor factor
   */
  public void setScaleZ(final double factor) {
    setScaleZ(factor, NiftyNodeState.Regular);
  }

  /**
   * Set scale for z-axis.
   * @param factor factor
   * @param states the NiftyNode.State that this should be applied to. This is optional.
   */
  public void setScaleZ(final double factor, final NiftyNodeState ... states) {
    impl.setScaleZ(factor, states);
  }

  /**
   * Get rotation around the x-axis in degrees.
   * @return angle rotation angle
   */
  public double getRotationX() {
    return impl.getRotationX();
  }

  /**
   * Set rotation around the y-axis in degrees.
   * @return angle rotation angle
   */
  public double getRotationY() {
    return impl.getRotationY();
  }

  /**
   * Get rotation around the z-axis in degrees.
   * @return angle rotation angle
   */
  public double getRotationZ() {
    return impl.getRotationZ();
  }

  /**
   * Get scale for x-axis.
   * @return x scale
   */
  public double getScaleX() {
    return impl.getScaleX();
  }

  /**
   * Get scale for y-axis.
   * @return y scale
   */
  public double getScaleY() {
    return impl.getScaleY();
  }

  /**
   * Get scale for z-axis.
   * @return z scale
   */
  public double getScaleZ() {
    return impl.getScaleZ();
  }

  /**
   * Set a NiftyCanvasPainter for the NiftyNode. This means you'd like to provide the content on your own. The
   * NiftyCanvasPainter is an interface you're supposed to implement. Nifty will call you back when it is time to
   * provide the content of this Node.
   *
   * @param painter the NiftyCanvasPainter instance to use for this Node
   */
  public void setCanvasPainter(final NiftyCanvasPainter painter) {
    setCanvasPainter(painter, NiftyNodeState.Regular);
  }

  /**
   * Set a NiftyCanvasPainter for the NiftyNode. This means you'd like to provide the content on your own. The
   * NiftyCanvasPainter is an interface you're supposed to implement. Nifty will call you back when it is time to
   * provide the content of this Node.
   *
   * @param painter the NiftyCanvasPainter instance to use for this Node
   * @param states the NiftyNode.State that this should be applied to. This is optional.
   */
  public void setCanvasPainter(final NiftyCanvasPainter painter, final NiftyNodeState ... states) {
    impl.setCanvasPainter(painter, states);
  }

  /**
   * Add a NiftyCanvasPainter for the NiftyNode. This means you'd like to keep the canvas painters already available for
   * this node and add your own one. This method is helpful if you want to keep the standard canvas painter and just
   * add additional rendering on top of it.
   *
   * @param painter the NiftyCanvasPainter instance to add for this Node
   */
  public void addCanvasPainter(final NiftyCanvasPainter painter) {
    impl.addCanvasPainter(painter);
  }

  /**
   * Forces this Node to be redrawn the next time nodes are refreshed.
   */
  public void requestRedraw() {
    impl.requestRedraw();
  }

  /**
   * Forces this Node to be relayed out the next time nodes are refreshed.
   */
  public void requestLayout() {
    impl.requestLayout();
  }

  /**
   * Call the given callback every interval ms after the delay in ms is over.
   *
   * @param delay the delay before the start in ms
   * @param interval the interval after the callback is called
   * @param callback the callback
   */
  public void startAnimated(final long delay, final long interval, final NiftyCallback<Float> callback) {
    impl.startAnimated(delay, interval, callback);
  }

  /**
   * This is a special version of startAnimated that will automatically redraws the content of this node after the
   * given interval.
   *
   * @param delay the delay before the start in ms
   * @param interval the interval after the callback is called
   */
  public void startAnimatedRedraw(final long delay, final long interval) {
    impl.startAnimated(delay, interval, new NiftyCallback<Float>() {
      @Override
      public void execute(final Float t) {
        impl.requestRedraw();
      }
    });
  }

  /**
   * Stop redrawing this node constantly.
   */
  public void stopAnimatedRedraw(final long delay) {
    impl.stopAnimatedRedraw();
  }

  /**
   * Get render order value for this NiftyNode.
   * @return renderOrder
   */
  public int getRenderOrder() {
    return impl.getRenderOrder();
  }

  /**
   * Set render order for this NiftyNode.
   * @param renderOrder the new render order value
   */
  public void setRenderOrder(final int renderOrder) {
    setRenderOrder(renderOrder, NiftyNodeState.Regular);
  }

  /**
   * Set render order for this NiftyNode.
   * @param renderOrder the new render order value
   * @param states the NiftyNode.State that this should be applied to. This is optional.
   */
  public void setRenderOrder(final int renderOrder, final NiftyNodeState ... states) {
    impl.setRenderOrder(renderOrder, states);
  }

  /**
   * Convert a position in screen coordinates to the corresponding coordinates local to this node.
   *
   * @param x the x coordinate to convert
   * @param y the y coordinate to convert
   * @return a Vec4 containing the coordinates in local space
   */
  public Point screenToLocal(final int x, final int y) {
    Vec4 v = impl.screenToLocal(x, y);
    return new Point(v.getX(), v.getY());
  }

  /**
   * Convert a position in the local coordinate system for this node into screen coordinates.
   *
   * @param x the x coordinate in local node coordinates to convert to screen coordinates
   * @param y the y coordinate in local node coordinates to convert to screen coordinates
   * @return the screen coordinates
   */
  public Point localToScreen(final int x, final int y) {
    Vec4 v = impl.localToScreen(x, y);
    return new Point(v.getX(), v.getY());
  }

  /**
   * Apply the given class (or classes of the values are separated by white spaces) to that node. Calling this method
   * will replace all other classes that might have been added before.
   *
   * @param classes the style classes
   */
  public void setStyleClass(final String classes) {
    impl.setStyleClass(classes);
  }

  @NiftyStyleProperty(name="class")
  public String getStyleClass() {
    return impl.getStyleClass();
  }

  /**
   * Create a new NiftyNode and make this node it's parent.
   * 
   * @return a new NiftyNode
   */
  public NiftyNode newChildNode() {
    return new NiftyNode(impl.newChildNode());
  }

  /**
   * Create a new NiftyNode and make this node it's parent. Use the given
   * childLayout for the new child node.
   * 
   * @param childLayout
   *          the new ChildLayout for the new node
   * @return a new NiftyNode
   */
  public NiftyNode newChildNode(final ChildLayout childLayout) {
    return new NiftyNode(impl.newChildNode(childLayout));
  }

  /**
   * Create a new NiftyNode and make this node it's parent. The new ChildNode
   * will be given the given width and height. This is a convienience method
   * only since you can always change the height of the node after you've
   * created a new node.
   * 
   * @param width
   *          width of the new NiftyNode
   * @param height
   *          height of the new NiftyNode
   * @return a new NiftyNode
   */
  public NiftyNode newChildNode(final UnitValue width, final UnitValue height) {
    return new NiftyNode(impl.newChildNode(width, height));
  }

  /**
   * Create a new NiftyNode and make this node it's parent. The new ChildNode
   * will be given the given width and height. This is a convienience method
   * only since you can always change the height of the node after you've
   * created a new node.
   * 
   * @param width
   *          width of the new NiftyNode
   * @param height
   *          height of the new NiftyNode
   * @param childLayout
   *          the new ChildLayout for the new node
   * @return a new NiftyNode
   */
  public NiftyNode newChildNode(final UnitValue width, final UnitValue height, final ChildLayout childLayout) {
    return new NiftyNode(impl.newChildNode(width, height, childLayout));
  }

  /**
   * Create a new NiftyNode and make this node it's parent.
   *
   * @param id the id of the new child node
   * @return a new NiftyNode
   */
  public NiftyNode newChildNode(final String id) {
    return new NiftyNode(impl.newChildNode(id));
  }

  /**
   * Create a new NiftyNode and make this node it's parent. Use the given
   * childLayout for the new child node.
   *
   * @param id the id of the new child node
   * @param childLayout
   *          the new ChildLayout for the new node
   * @return a new NiftyNode
   */
  public NiftyNode newChildNode(final String id, final ChildLayout childLayout) {
    return new NiftyNode(impl.newChildNode(id, childLayout));
  }

  /**
   * Create a new NiftyNode and make this node it's parent. The new ChildNode
   * will be given the given width and height. This is a convienience method
   * only since you can always change the height of the node after you've
   * created a new node.
   *
   * @param id the id of the new child node
   * @param width
   *          width of the new NiftyNode
   * @param height
   *          height of the new NiftyNode
   * @return a new NiftyNode
   */
  public NiftyNode newChildNode(final String id, final UnitValue width, final UnitValue height) {
    return new NiftyNode(impl.newChildNode(id, width, height));
  }

  /**
   * Create a new NiftyNode and make this node it's parent. The new ChildNode
   * will be given the given width and height. This is a convienience method
   * only since you can always change the height of the node after you've
   * created a new node.
   *
   * @param id the id of the new child node
   * @param width
   *          width of the new NiftyNode
   * @param height
   *          height of the new NiftyNode
   * @param childLayout
   *          the new ChildLayout for the new node
   * @return a new NiftyNode
   */
  public NiftyNode newChildNode(final String id, final UnitValue width, final UnitValue height, final ChildLayout childLayout) {
    return new NiftyNode(impl.newChildNode(id, width, height, childLayout));
  }

  /**
   * Create a new NiftyControl and make this node it's parent. Internally this method will create a regular NiftyNode
   * that will be attached to the NiftyControl.
   * 
   * @return a new NiftyControl
   */
  public <T extends NiftyControl> T newControl(final Class<T> controlClass) {
    return impl.newControl(controlClass, new NiftyNode(impl.newChildNode()));
  }

  /**
   * Collect state information for this node (and all of it's children) into the
   * given StringBuilder.
   * 
   * Please note:
   * 
   * The data written is highly fragile and can be changed between releases
   * without any warning. Don't rely on this information other than for
   * debugging the state of the nodes.
   * 
   * @param result
   *          the StringBuilder to add the state info to
   */
  public void getStateInfo(final StringBuilder result) {
    impl.getStateInfo(result);
  }

  /**
   * @see #getStateInfo(StringBuilder)
   * 
   *      Additionally this methods allow you to filter the result with a
   *      regular expression. You'll always get the id of the elements back and
   *      the rows that match your regular expression.
   * 
   *      Example:
   * 
   *      <pre>
   * niftyNode.getStateInfo(result, &quot;.*position.*&quot;); // will only give you position
   * // information
   * </pre>
   * 
   * @param result
   *          the StringBuilder to add the state info to
   * @param pattern
   *          the pattern to match the output
   */
  public void getStateInfo(final StringBuilder result, final String pattern) {
    impl.getStateInfo(result, pattern);
  }

  /**
   * When this NiftyNode doesn't have any size constraints set but a NiftyMinSizeCallback is given then it is used to
   * calculate the minimum size of the NiftyNode. The NiftyMinSizeCallback should return the minimum width and height
   * of the content of the NiftyNode and Nifty will automatically set width and height constraints for the NiftyNode.
   *
   * @param minSizeCallback the NiftyMinSizeCallback.
   */
  public void enableMinSize(final NiftyMinSizeCallback minSizeCallback) {
    impl.enableMinSize(minSizeCallback);
  }

  /**
   * Subscribe the given Object to receive events published by this NiftyNode.
   * @param listener the Listener to subscribe events for
   */
  public void subscribe(final Object listener) {
    impl.subscribe(listener);
  }

  /**
   * Force this Node to the NiftyNodeStates given. Please note that usually it's NOT necessary to call this method at
   * all. It's only ever interesting to be called for special purposes like you want't to test your new cool hover
   * state and you don't want to manually hover your node with your pointer each time you made a change.
   *
   * Also note that when you call this then that's it! Nifty will not update the states by itself anymore! It will
   * just render the states you give it here! So make sure you know what you're doing when you call this method :)
   *
   * (And as I said, actually it's not necessary to call it at all ...)
   *
   * @param states the NiftyNodeStates you want this Node to have
   */
  public void forceStates(final NiftyNodeState ... states) {
    impl.forceStates(states);
  }

  @Override
  public String toString() {
    return "[" + impl.toString() + "]";
  }

  // package private accessor stuff

  InternalNiftyNode getImpl() {
    return impl;
  }

  static {
    NiftyNodeAccessor.DEFAULT = new InternalNiftyNodeAccessorImpl();
  }

  static NiftyNode newInstance(final InternalNiftyNode impl) {
    return new NiftyNode(impl);
  }
}

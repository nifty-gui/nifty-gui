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
package de.lessvoid.nifty.node;

import de.lessvoid.nifty.NiftyState;
import de.lessvoid.nifty.spi.node.NiftyLayoutReceiver;
import de.lessvoid.nifty.spi.node.NiftyNodeStateImpl;
import de.lessvoid.nifty.types.NiftyRect;
import de.lessvoid.niftyinternal.math.Mat4;

import javax.annotation.Nonnull;

import static de.lessvoid.nifty.NiftyState.NiftyStandardState.NiftyStateTransformation;
import static de.lessvoid.nifty.NiftyState.NiftyStandardState.NiftyStateTransformationLayoutRect;
import static de.lessvoid.nifty.NiftyState.NiftyStandardState.NiftyStateTransformationChanged;

/**
 * Created by void on 09.08.15.
 */
class NiftyTransformationNodeImpl
    implements
      NiftyNodeStateImpl<NiftyTransformationNode>,
      NiftyLayoutReceiver<NiftyTransformationNode> {
  private final TransformationParameters transformation = new TransformationParameters();
  private final Mat4 identity = Mat4.createIdentity();
  private final Mat4 local = Mat4.createIdentity();
  private final Mat4 temp = Mat4.createIdentity();
  private NiftyRect layoutRect;

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // NiftyNodeImpl
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public NiftyTransformationNode getNiftyNode() {
    return new NiftyTransformationNode(this);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // NiftyNodeStateImpl
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public void update(final NiftyState niftyState) {
    NiftyRect parentLayoutRect = niftyState.getState(NiftyStateTransformationLayoutRect, null);
    niftyState.setState(NiftyStateTransformationLayoutRect, layoutRect);

    float parentRelativeLayoutPosX = layoutRect.getOrigin().getX();
    float parentRelativeLayoutPosY = layoutRect.getOrigin().getY();
    if (parentLayoutRect != null) {
      parentRelativeLayoutPosX -= parentLayoutRect.getOrigin().getX();
      parentRelativeLayoutPosY -= parentLayoutRect.getOrigin().getY();
    }

    Mat4 parentLocalToScreen = niftyState.getState(NiftyStateTransformation, identity);
    boolean parentLocalToScreenChanged = niftyState.getState(NiftyStateTransformationChanged, false);
    boolean thisStateChanged = transformation.isTransformationChanged() || parentLocalToScreenChanged;
    niftyState.setState(NiftyStateTransformation, updateTransformation(parentLocalToScreen, thisStateChanged, parentRelativeLayoutPosX, parentRelativeLayoutPosY));
    niftyState.setState(NiftyStateTransformationChanged, thisStateChanged);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // NiftyLayoutReceiver
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public void setLayoutResult(@Nonnull NiftyRect rect) {
    layoutRect = rect;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Public Methods
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public double getPivotX() {
    return transformation.getPivotX();
  }

  public void setPivotX(final double pivotX) {
    transformation.setPivotX(pivotX);
  }

  public double getPivotY() {
    return transformation.getPivotY();
  }

  public void setPivotY(final double pivotY) {
    transformation.setPivotY(pivotY);
  }

  public double getAngleX() {
    return transformation.getAngleX();
  }

  public void setAngleX(final double angleX) {
    transformation.setAngleX(angleX);
  }

  public double getAngleY() {
    return transformation.getAngleY();
  }

  public void setAngleY(final double angleY) {
    transformation.setAngleY(angleY);
  }

  public double getAngleZ() {
    return transformation.getAngleZ();
  }

  public void setAngleZ(final double angleZ) {
    transformation.setAngleZ(angleZ);
  }

  public double getScaleX() {
    return transformation.getScaleX();
  }

  public void setScaleX(final double scaleX) {
    transformation.setScaleX(scaleX);
  }

  public double getScaleY() {
    return transformation.getScaleY();
  }

  public void setScaleY(final double scaleY) {
    transformation.setScaleY(scaleY);
  }

  public double getScaleZ() {
    return transformation.getScaleZ();
  }

  public void setScaleZ(final double scaleZ) {
    transformation.setScaleZ(scaleZ);
  }

  public double getPosX() {
    return transformation.getPosX();
  }

  public void setPosX(final double posX) {
    transformation.setPosX(posX);
  }

  public double getPosY() {
    return transformation.getPosY();
  }

  public void setPosY(final double posY) {
    transformation.setPosY(posY);
  }

  public double getPosZ() {
    return transformation.getPosZ();
  }

  public void setPosZ(final double posZ) {
    transformation.setPosZ(posZ);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Private Methods
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private Mat4 updateTransformation(
      final Mat4 parentLocalToScreen,
      final boolean transformationChanged,
      final float parentRelativeLayoutPosX,
      final float parentRelativeLayoutPosY) {
    if (!transformationChanged) {
      return local;
    }
    transformation.buildTransformationMatrix(
        temp,
        parentRelativeLayoutPosX,
        parentRelativeLayoutPosY,
        layoutRect.getSize().getWidth(),
        layoutRect.getSize().getHeight());
    Mat4.mul(parentLocalToScreen, temp, local);
    return local;
  }

}

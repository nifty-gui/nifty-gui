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
package de.lessvoid.niftyinternal.node;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyState;
import static de.lessvoid.nifty.NiftyState.NiftyStandardState.*;
import de.lessvoid.nifty.node.NiftyTransformationNode;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;
import de.lessvoid.nifty.spi.node.NiftyNodeStateImpl;
import de.lessvoid.niftyinternal.accessor.NiftyAccessor;
import de.lessvoid.niftyinternal.math.Mat4;

/**
 * Created by void on 09.08.15.
 */
public class NiftyTransformationNodeImpl implements NiftyNodeStateImpl, NiftyNodeImpl<NiftyTransformationNode> {
  private Nifty nifty;
  private NiftyTransformationNode niftyNode;

  private boolean transformationChanged = true;
  private double pivotX = 0.5;
  private double pivotY = 0.5;
  private double angleX = 0.0;
  private double angleY = 0.0;
  private double angleZ = 0.0;
  private double scaleX = 1.0;
  private double scaleY = 1.0;
  private double scaleZ = 1.0;

  private Mat4 localTransformation;
  private Mat4 localTransformationInverse;
  private Mat4 screenToLocalTransformation;
  private Mat4 screenToLocalTransformationInverse;

  @Override
  public void initialize(final Nifty nifty, final NiftyTransformationNode niftyNode) {
    this.nifty = nifty;
    this.niftyNode = niftyNode;
  }

  @Override
  public NiftyTransformationNode getNiftyNode() {
    return niftyNode;
  }

  @Override
  public void update(final NiftyState niftyState) {
    if (updateTransformationChanged(pivotX, niftyNode.getPivotX())) pivotX = niftyNode.getPivotX();
    if (updateTransformationChanged(pivotY, niftyNode.getPivotY())) pivotY = niftyNode.getPivotY();
    if (updateTransformationChanged(angleX, niftyNode.getAngleX())) angleX = niftyNode.getAngleX();
    if (updateTransformationChanged(angleY, niftyNode.getAngleY())) angleY = niftyNode.getAngleY();
    if (updateTransformationChanged(angleZ, niftyNode.getAngleZ())) angleZ = niftyNode.getAngleZ();
    if (updateTransformationChanged(scaleX, niftyNode.getScaleX())) scaleX = niftyNode.getScaleX();
    if (updateTransformationChanged(scaleY, niftyNode.getScaleY())) scaleY = niftyNode.getScaleY();
    if (updateTransformationChanged(scaleZ, niftyNode.getScaleZ())) scaleZ = niftyNode.getScaleZ();

    niftyState.setState(NiftyStateTransformation, updateTransformation());
  }

  public String toString() {
    return niftyNode.toString();
  }

  private Mat4 updateTransformation() {
    if (!isTransformationChanged()) {
      return screenToLocalTransformation;
    }
    localTransformation = new Mat4(buildLocalTransformation());
    localTransformationInverse = Mat4.invert(localTransformation, new Mat4());

    Mat4 parentTransformation = getParentScreenToLocalTransformation();
    screenToLocalTransformation = Mat4.mul(parentTransformation, localTransformation);
    screenToLocalTransformationInverse = Mat4.invert(screenToLocalTransformation, new Mat4());

    transformationChanged = false;
    return screenToLocalTransformation;
  }

  private boolean updateTransformationChanged(final double oldValue, final double newValue) {
    if (newValue != oldValue) {
      transformationChanged = true;
      return true;
    }
    return false;
  }

  private Mat4 buildLocalTransformation() {
    float pivotX = (float) this.pivotX * 1.f; // TODO figure out width
    float pivotY = (float) this.pivotY * 1.f; // TODO figure out height
    return Mat4.mul(Mat4.mul(Mat4.mul(Mat4.mul(Mat4.mul(Mat4.mul(
                            Mat4.createTranslate(0.f, 0.f, 0.f), // TODO this is actually x and y pos
                            Mat4.createTranslate(pivotX, pivotY, 0.0f)),
                        Mat4.createRotate((float) this.angleX, 1.f, 0.f, 0.f)),
                    Mat4.createRotate((float) this.angleY, 0.f, 1.f, 0.f)),
                Mat4.createRotate((float) this.angleZ, 0.f, 0.f, 1.f)),
            Mat4.createScale((float) this.scaleX, (float) this.scaleY, (float) this.scaleZ)),
        Mat4.createTranslate(-pivotX, -pivotY, 0.0f));
  }

  private boolean isTransformationChanged() {
    return transformationChanged;
  }

  private Mat4 getParentScreenToLocalTransformation() {
    NiftyTransformationNodeImpl parent = NiftyAccessor.getDefault().getInternalNiftyTree(nifty).getParent(NiftyTransformationNodeImpl.class, this);
    if (parent == null) {
      return Mat4.createIdentity();
    }
    return parent.screenToLocalTransformation;
  }
}

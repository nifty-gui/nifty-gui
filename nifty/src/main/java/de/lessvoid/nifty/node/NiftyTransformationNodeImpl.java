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
import de.lessvoid.nifty.spi.node.NiftyNodeStateImpl;

import java.util.ArrayList;
import java.util.List;

import static de.lessvoid.nifty.NiftyState.NiftyStandardState.NiftyStateTransformation;

/**
 * Created by void on 09.08.15.
 */
class NiftyTransformationNodeImpl
    implements
      NiftyNodeStateImpl<NiftyTransformationNode> {
  private TransformationParameters transformationParameters = new TransformationParameters();

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
    List<TransformationParameters> transformations = niftyState.getState(NiftyStateTransformation);
    if (transformations == null) {
      transformations = new ArrayList<>();
    }
    transformations.add(transformationParameters);
    niftyState.setState(NiftyStateTransformation, transformations);
  }

  public double getPivotX() {
    return transformationParameters.getPivotX();
  }

  public void setPivotX(final double pivotX) {
    transformationParameters.setPivotX(pivotX);
  }

  public double getPivotY() {
    return transformationParameters.getPivotY();
  }

  public void setPivotY(final double pivotY) {
    transformationParameters.setPivotY(pivotY);
  }

  public double getAngleX() {
    return transformationParameters.getAngleX();
  }

  public void setAngleX(final double angleX) {
    transformationParameters.setAngleX(angleX);
  }

  public double getAngleY() {
    return transformationParameters.getAngleY();
  }

  public void setAngleY(final double angleY) {
    transformationParameters.setAngleY(angleY);
  }

  public double getAngleZ() {
    return transformationParameters.getAngleZ();
  }

  public void setAngleZ(final double angleZ) {
    transformationParameters.setAngleZ(angleZ);
  }

  public double getScaleX() {
    return transformationParameters.getScaleX();
  }

  public void setScaleX(final double scaleX) {
    transformationParameters.setScaleX(scaleX);
  }

  public double getScaleY() {
    return transformationParameters.getScaleY();
  }

  public void setScaleY(final double scaleY) {
    transformationParameters.setScaleY(scaleY);
  }

  public double getScaleZ() {
    return transformationParameters.getScaleZ();
  }

  public void setScaleZ(final double scaleZ) {
    transformationParameters.setScaleZ(scaleZ);
  }

  public double getPosX() {
    return transformationParameters.getPosX();
  }

  public void setPosX(final double posX) {
    transformationParameters.setPosX(posX);
  }

  public double getPosY() {
    return transformationParameters.getPosY();
  }

  public void setPosY(final double posY) {
    transformationParameters.setPosY(posY);
  }

  public double getPosZ() {
    return transformationParameters.getPosZ();
  }

  public void setPosZ(final double posZ) {
    transformationParameters.setPosZ(posZ);
  }
}

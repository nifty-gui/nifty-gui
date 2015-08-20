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

import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.types.NiftyColor;

/**
 * The API for the NiftyTransformationNode node.
 * Created by void on 09.08.15.
 */
public class NiftyTransformationNode implements NiftyNode {
  private double pivotX = 0.5;
  private double pivotY = 0.5;
  private double angleX = 0.0;
  private double angleY = 0.0;
  private double angleZ = 0.0;
  private double scaleX = 1.0;
  private double scaleY = 1.0;
  private double scaleZ = 1.0;

  public static NiftyTransformationNode transformationNode() {
    return new NiftyTransformationNode();
  }

  public double getPivotX() {
    return pivotX;
  }

  public NiftyTransformationNode setPivotX(final double pivotX) {
    this.pivotX = pivotX;
    return this;
  }

  public double getPivotY() {
    return pivotY;
  }

  public NiftyTransformationNode setPivotY(final double pivotY) {
    this.pivotY = pivotY;
    return this;
  }

  public double getAngleX() {
    return angleX;
  }

  public NiftyTransformationNode setAngleX(final double angleX) {
    this.angleX = angleX;
    return this;
  }

  public double getAngleY() {
    return angleY;
  }

  public NiftyTransformationNode setAngleY(final double angleY) {
    this.angleY = angleY;
    return this;
  }

  public double getAngleZ() {
    return angleZ;
  }

  public NiftyTransformationNode setAngleZ(final double angleZ) {
    this.angleZ = angleZ;
    return this;
  }

  public double getScaleX() {
    return scaleX;
  }

  public NiftyTransformationNode setScaleX(final double scaleX) {
    this.scaleX = scaleX;
    return this;
  }

  public double getScaleY() {
    return scaleY;
  }

  public NiftyTransformationNode setScaleY(final double scaleY) {
    this.scaleY = scaleY;
    return this;
  }

  public double getScaleZ() {
    return scaleZ;
  }

  public NiftyTransformationNode setScaleZ(final double scaleZ) {
    this.scaleZ = scaleZ;
    return this;
  }

  public String toString() {
    return "(" + this.getClass().getSimpleName() + ") FIXME";
  }
}

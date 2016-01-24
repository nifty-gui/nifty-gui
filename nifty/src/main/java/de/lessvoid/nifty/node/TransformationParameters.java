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

import de.lessvoid.niftyinternal.math.Mat4;

/**
 * Created by void on 24.01.16.
 */
public class TransformationParameters {
  private final Mat4 a = Mat4.createIdentity();
  private final Mat4 b = Mat4.createIdentity();
  private final Mat4 c = Mat4.createIdentity();
  private final StringBuilder builder = new StringBuilder();

  private boolean transformationChanged = true;

  private double pivotX = 0.5;
  private double pivotY = 0.5;
  private double angleX = 0.0;
  private double angleY = 0.0;
  private double angleZ = 0.0;
  private double scaleX = 1.0;
  private double scaleY = 1.0;
  private double scaleZ = 1.0;
  private double posX = 0.0;
  private double posY = 0.0;
  private double posZ = 0.0;

  public void buildTransformationMatrix(final Mat4 target, float layoutPosX, float layoutPosY, int layoutWidth, int layoutHeight) {
    a.setTranslate(layoutPosX, layoutPosY, 0.f);
    b.setTranslate((float) posX, (float) posY, 0.f);
    Mat4.mul(a, b, c);
    a.setTranslate((float) pivotX * layoutWidth, (float) pivotY * layoutHeight, 0.f);
    Mat4.mul(c, a, b);
    a.setRotate((float) angleX, 1.f, 0.f, 0.f);
    Mat4.mul(b, a, c);
    a.setRotate((float) angleY, 0.f, 1.f, 0.f);
    Mat4.mul(c, a, b);
    a.setRotate((float) angleZ, 0.f, 0.f, 1.f);
    Mat4.mul(b, a, c);
    a.setScale((float) scaleX, (float) scaleY, (float) scaleZ);
    Mat4.mul(c, a, b);
    a.setTranslate((float) -pivotX * layoutWidth, (float) -pivotY * layoutHeight, 0.f);
    Mat4.mul(b, a, target);
  }

  public String toString() {
    builder.setLength(0);
    builder.append("(");
    builder.append("pivotX [").append(pivotX).append("] ");
    builder.append("pivotY [").append(pivotY).append("] ");
    builder.append("angleX [").append(angleX).append("] ");
    builder.append("angleY [").append(angleY).append("] ");
    builder.append("angleZ [").append(angleZ).append("] ");
    builder.append("scaleX [").append(scaleX).append("] ");
    builder.append("scaleY [").append(scaleY).append("] ");
    builder.append("scaleZ [").append(scaleZ).append("] ");
    builder.append("posX [").append(posX).append("] ");
    builder.append("posY [").append(posY).append("] ");
    builder.append("posZ [").append(posZ).append("]");
    builder.append(")");
    return builder.toString();
  }

  public double getPivotX() {
    return pivotX;
  }

  public double getPivotY() {
    return pivotY;
  }

  public double getAngleX() {
    return angleX;
  }

  public double getAngleY() {
    return angleY;
  }

  public double getAngleZ() {
    return angleZ;
  }

  public double getScaleX() {
    return scaleX;
  }

  public double getScaleY() {
    return scaleY;
  }

  public double getScaleZ() {
    return scaleZ;
  }

  public double getPosX() {
    return posX;
  }

  public double getPosY() {
    return posY;
  }

  public double getPosZ() {
    return posZ;
  }

  public void setPivotX(final double pivotX) {
    updateTransformationChanged(this.pivotX, pivotX);
    this.pivotX = pivotX;
  }

  public void setPivotY(final double pivotY) {
    updateTransformationChanged(this.pivotY, pivotY);
    this.pivotY = pivotY;
  }

  public void setAngleX(final double angleX) {
    updateTransformationChanged(this.angleX, angleX);
    this.angleX = angleX;
  }

  public void setAngleY(final double angleY) {
    updateTransformationChanged(this.angleY, angleY);
    this.angleY = angleY;
  }

  public void setAngleZ(final double angleZ) {
    updateTransformationChanged(this.angleZ, angleZ);
    this.angleZ = angleZ;
  }
  public void setScaleX(final double scaleX) {
    updateTransformationChanged(this.scaleX, scaleX);
    this.scaleX = scaleX;
  }

  public void setScaleY(final double scaleY) {
    updateTransformationChanged(this.scaleY, scaleY);
    this.scaleY = scaleY;
  }

  public void setScaleZ(final double scaleZ) {
    updateTransformationChanged(this.scaleZ, scaleZ);
    this.scaleZ = scaleZ;
  }

  public void setPosX(final double posX) {
    updateTransformationChanged(this.posX, posX);
    this.posX = posX;
  }

  public void setPosY(final double posY) {
    updateTransformationChanged(this.posY, posY);
    this.posY = posY;
  }

  public void setPosZ(final double posZ) {
    updateTransformationChanged(this.posZ, posZ);
    this.posZ = posZ;
  }

  private void updateTransformationChanged(final double oldValue, final double newValue) {
    if (newValue != oldValue) {
      transformationChanged = true;
    }
  }
}

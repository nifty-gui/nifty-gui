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

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.spi.node.NiftyNode;

/**
 * The API for the NiftyTransformationNode node.
 * Created by void on 09.08.15.
 */
public class NiftyTransformationNode implements NiftyNode {
  private final NiftyTransformationNodeImpl impl;

  public static NiftyTransformationNode transformationNode(final Nifty nifty) {
    return new NiftyTransformationNode(nifty);
  }

  private NiftyTransformationNode(final Nifty nifty) {
    impl = new NiftyTransformationNodeImpl(nifty);
  }

  NiftyTransformationNode(final NiftyTransformationNodeImpl impl) {
    this.impl = impl;
  }

  public double getPivotX() {
    return impl.getPivotX();
  }

  public NiftyTransformationNode setPivotX(final double pivotX) {
    impl.setPivotX(pivotX);
    return this;
  }

  public double getPivotY() {
    return impl.getPivotY();
  }

  public NiftyTransformationNode setPivotY(final double pivotY) {
    impl.setPivotY(pivotY);
    return this;
  }

  public double getAngleX() {
    return impl.getAngleX();
  }

  public NiftyTransformationNode setAngleX(final double angleX) {
    impl.setAngleX(angleX);
    return this;
  }

  public double getAngleY() {
    return impl.getAngleY();
  }

  public NiftyTransformationNode setAngleY(final double angleY) {
    impl.setAngleY(angleY);
    return this;
  }

  public double getAngleZ() {
    return impl.getAngleZ();
  }

  public NiftyTransformationNode setAngleZ(final double angleZ) {
    impl.setAngleZ(angleZ);
    return this;
  }

  public double getScaleX() {
    return impl.getScaleX();
  }

  public NiftyTransformationNode setScaleX(final double scaleX) {
    impl.setScaleX(scaleX);
    return this;
  }

  public double getScaleY() {
    return impl.getScaleY();
  }

  public NiftyTransformationNode setScaleY(final double scaleY) {
    impl.setScaleY(scaleY);
    return this;
  }

  public double getScaleZ() {
    return impl.getScaleZ();
  }

  public NiftyTransformationNode setScaleZ(final double scaleZ) {
    impl.setScaleZ(scaleZ);
    return this;
  }

  public String toString() {
    return "(" + this.getClass().getSimpleName() + ") FIXME";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NiftyTransformationNode that = (NiftyTransformationNode) o;
    return !(impl != null ? !impl.equals(that.impl) : that.impl != null);
  }

  @Override
  public int hashCode() {
    return impl != null ? impl.hashCode() : 0;
  }

  // friend access

  NiftyTransformationNodeImpl getImpl() {
    return impl;
  }
}

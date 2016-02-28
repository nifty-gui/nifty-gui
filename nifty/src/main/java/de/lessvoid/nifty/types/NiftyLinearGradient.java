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
package de.lessvoid.nifty.types;

import de.lessvoid.niftyinternal.accessor.NiftyLinearGradientAccessor;
import de.lessvoid.niftyinternal.common.InternalNiftyColorStop;
import de.lessvoid.niftyinternal.render.batch.LinearGradient;

import java.util.Set;
import java.util.TreeSet;

/**
 * A linear gradient between two points. The two points are either given directly or are implied when only an angle
 * is given. In the latter case the actual points are calculated later with respect to the coordinates of the node
 * the gradient will be applied to.
 * <br>
 * Color stops can be added along the line given as a stop value in the interval [0.0, 1.0] and a NiftyColor.
 * <br>
 * Currently once these color stops are added they are treated as read-only, which means that their position and color
 * can't be changed anymore.
 * <br>
 * @author void
 */
public final class NiftyLinearGradient {
  private final double startX;
  private final double startY;
  private final double endX;
  private final double endY;
  private final double angleInRadiants;
  private final boolean angleMode;
  private final Set<InternalNiftyColorStop> colorStops = new TreeSet<>();

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Public API
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Create the gradient from two points.
   *
   * @param x0 start point x
   * @param y0 start point y
   * @param x1 end point x
   * @param y1 end point y
   * @return the NiftyLinearGradient
   */
  public static NiftyLinearGradient create(final double x0, final double y0, final double x1, final double y1) {
    return new NiftyLinearGradient(x0, y0, x1, y1);
  }

  /**
   * Create a linear gradient from an angle in degrees.
   *
   * @param angleInDegree The angle of the gradient line in degrees
   * @return the NiftyLinearGradient
   */
  public static NiftyLinearGradient createFromAngleInDeg(final double angleInDegree) {
    return new NiftyLinearGradient(toRad(angleInDegree));
  }

  /**
   * Create a linear gradient from an angle in radian.
   *
   * @param angleInRadians The angle of the gradient line in radian
   * @return the NiftyLinearGradient
   */
  public static NiftyLinearGradient createFromAngleInRad(final double angleInRadians) {
    return new NiftyLinearGradient(angleInRadians);
  }

  /**
   * Add a color stop to the gradient at position stop (value between 0.0 and 1.0) with the given color.
   *
   * @param stop a value between 0.0 and 1.0 that represents the position between start and end in a
   * gradient.
   * @param color a NiftyColor to display at stop position.
   */
  public final NiftyLinearGradient addColorStop(final double stop, final NiftyColor color) {
    InternalNiftyColorStop newColorStop = new InternalNiftyColorStop(stop, color);
    colorStops.remove(newColorStop);
    colorStops.add(newColorStop);
    return this;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NiftyLinearGradient that = (NiftyLinearGradient) o;

    if (Double.compare(that.startX, startX) != 0) return false;
    if (Double.compare(that.startY, startY) != 0) return false;
    if (Double.compare(that.endX, endX) != 0) return false;
    if (Double.compare(that.endY, endY) != 0) return false;
    if (Double.compare(that.angleInRadiants, angleInRadiants) != 0) return false;
    if (angleMode != that.angleMode) return false;
    return colorStops.equals(that.colorStops);
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(startX);
    result = (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(startY);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(endX);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(endY);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(angleInRadiants);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + (angleMode ? 1 : 0);
    result = 31 * result + colorStops.hashCode();
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Friend API
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Copy constructor.
   * @param src the source
   */
  NiftyLinearGradient(final NiftyLinearGradient src) {
    this.angleMode = src.angleMode;
    this.angleInRadiants = src.angleInRadiants;
    this.colorStops.addAll(src.colorStops);
    this.startX = src.startX;
    this.startY = src.startY;
    this.endX = src.endX;
    this.endY = src.endY;
  }

  /**
   * Return the internal Nifty LinearGradient instance taking the given node coordinates into account when this
   * NiftyLinearGradient instance is using angleMode. If it is not using angleMode then the coordinates given in the
   * constructor are used as-is.
   *
   * @param x0 top left x coordinate of the node/rectangle to apply the gradient to
   * @param y0 top left y coordinate of the node/rectangle to apply the gradient to
   * @param x1 bottom right x coordinate of the node/rectangle to apply the gradient to
   * @param y1 bottom right x coordinate of the node/rectangle to apply the gradient to
   * @return the LinearGradient
   */
  final LinearGradient asLinearGradient(final double x0, final double y0, final double x1, final double y1) {
    if (angleMode) {
      double w = x1 - x0;
      double h = y1 - y0;
      double mx = x0 + w / 2;
      double my = y0 + h / 2;
      double sinAngle = Math.sin(angleInRadiants);
      double cosAngle = Math.cos(angleInRadiants);
      double length = Math.abs(w * sinAngle) + Math.abs(h * cosAngle);
      double halfLength = length / 2;

      // we flip start end end coordinates here since in Nifty the upper left hand corner is (0, 0)
      return new LinearGradient(
        mx - halfLength * sinAngle,
        my + halfLength * cosAngle,
        mx + halfLength * sinAngle,
        my - halfLength * cosAngle,
        colorStops);
    } else {
      return new LinearGradient(
        startX,
        startY,
        endX,
        endY,
        colorStops
      );
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Private
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private NiftyLinearGradient(final double x0, final double y0, final double x1, final double y1) {
    this.angleMode = false;
    this.angleInRadiants = 0.0;
    this.startX = x0;
    this.startY = y0;
    this.endX = x1;
    this.endY = y1;
  }

  private NiftyLinearGradient(final double angleInRadiants) {
    this.angleMode = true;
    this.angleInRadiants = angleInRadiants;
    this.startX = 0.0;
    this.startY = 0.0;
    this.endX = 0.0;
    this.endY = 0.0;
  }

  private static double toRad(final double grad) {
    return Math.PI * 2 * grad / 360.;
  }

  static {
    NiftyLinearGradientAccessor.DEFAULT = new NiftyLinearGradientAccessorImpl();
  }
}

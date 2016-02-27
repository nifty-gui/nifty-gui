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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A linear gradient between two points. The two points are either given directly or are implied when only an angle
 * is given. In the latter case points are calculated internally using normalised coordinates.
 *
 * Color stops can be added along the line given as a stop value in the interval [0.0, 1.0] and a NiftyColor.
 *
 * Currently once these color stops are added they are treated as read-only, which means that their position and color
 * can't be changed anymore.
 *
 * @author void
 */
public class NiftyLinearGradient {
  private final double angleInRadiants;
  private final Set<InternalNiftyColorStop> colorStops = new TreeSet<>();

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Public API
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Create a linear gradient from an angle in degrees.
   *
   * @param angleInDegree The angle of the gradient line in degrees
   */
  public static NiftyLinearGradient createFromAngleInDeg(final double angleInDegree) {
    return new NiftyLinearGradient(toRad(angleInDegree));
  }

  /**
   * Create a linear gradient from an angle in radian.
   *
   * @param angleInRadians The angle of the gradient line in radian
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
  public NiftyLinearGradient addColorStop(final double stop, final NiftyColor color) {
    InternalNiftyColorStop newColorStop = new InternalNiftyColorStop(stop, color);
    colorStops.remove(newColorStop);
    colorStops.add(newColorStop);
    return this;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(angleInRadiants);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((colorStops == null) ? 0 : colorStops.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    NiftyLinearGradient other = (NiftyLinearGradient) obj;
    if (Double.doubleToLongBits(angleInRadiants) != Double.doubleToLongBits(other.angleInRadiants))
      return false;
    if (colorStops == null) {
      if (other.colorStops != null)
        return false;
    } else if (!colorStops.equals(other.colorStops))
      return false;
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Friend API
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Copy constructor.
   * @param src the source
   */
  NiftyLinearGradient(final NiftyLinearGradient src) {
    this.angleInRadiants = src.angleInRadiants;
    this.colorStops.addAll(src.colorStops);
  }

  /**
   * Get the angle of the gradient line.
   *
   * @return the angle of the gradient line
   */
  double getAngleInRadiants() {
    return angleInRadiants;
  }

  /**
   * Returns a List of all existing color stops in this gradient. You'll get a new list so you can't modify the list.
   * @return the existing list of NiftyColorStops
   */
  List<InternalNiftyColorStop> getColorStops() {
    return Collections.unmodifiableList(new ArrayList<>(colorStops));
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Private
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private NiftyLinearGradient(final double angleInRadiants) {
    this.angleInRadiants = angleInRadiants;
  }

  private static double toRad(final double grad) {
    return Math.PI * 2 * grad / 360.;
  }

  static {
    NiftyLinearGradientAccessor.DEFAULT = new NiftyLinearGradientAccessorImpl();
  }
}

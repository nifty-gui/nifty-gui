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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A linear gradient between with a given angle that contains a number of color stops.
 *
 * @author void
 */
public class NiftyLinearGradient {
  private final double angleInRadiants;
  private final Set<NiftyColorStop> colorStops = new TreeSet<NiftyColorStop>();
  private double scale = 1.0;
  private boolean flip = false;

  private NiftyLinearGradient(final double angleInRadiants) {
    this.angleInRadiants = angleInRadiants;
  }

  /**
   * Create a linear gradient from an angle in degrees.
   *
   * @param angleInRadiants The angle of the gradient line in degrees
   */
  public static NiftyLinearGradient createFromAngleInDeg(final double angleInDegree) {
    return new NiftyLinearGradient(toRad(angleInDegree));
  }

  /**
   * Create a linear gradient from an angle in radian.
   *
   * @param angleInRadiants The angle of the gradient line in radian
   */
  public static NiftyLinearGradient createFromAngleInRad(final double angleInRadians) {
    return new NiftyLinearGradient(angleInRadians);
  }

  /**
   * Copy constructor.
   * @param src the source
   */
  public NiftyLinearGradient(final NiftyLinearGradient src) {
    this.angleInRadiants = src.angleInRadiants;
    this.colorStops.addAll(src.colorStops);
    this.scale = src.scale;
    this.flip = src.flip;
  }

  /**
   * Add a list of NiftyColorStops to this gradient.
   * @param newColorSteps the list of NiftyColorStops to add
   */
  public NiftyLinearGradient addColorSteps(final List<NiftyColorStop> newColorSteps) {
    this.colorStops.addAll(newColorSteps);
    return this;
  }

  /**
   * Add a color stop to the gradient at position stop (value between 0.0 and 1.0) with the given color.
   *
   * @param stop a value between 0.0 and 1.0 that represents the position between start and end in a
   * gradient.
   * @param color a NiftyColor to display at stop position.
   */
  public NiftyLinearGradient addColorStop(final double stop, final NiftyColor color) {
    NiftyColorStop newColorStop = new NiftyColorStop(stop, color);
    colorStops.remove(newColorStop);
    colorStops.add(newColorStop);
    return this;
  }

  /**
   * Get the angle of the gradient line.
   *
   * @return the angle of the gradient line
   */
  public double getAngleInRadiants() {
    return angleInRadiants;
  }

  /**
   * Returns a List of all existing color stops in this gradient. You'll get a new list so you can't modify the list.
   * @return the existing list of NiftyColorStops
   */
  public List<NiftyColorStop> getColorStops() {
    return Collections.unmodifiableList(new ArrayList<NiftyColorStop>(colorStops));
  }

  /**
   * Apply an optional scale factor to all stops. The default value is 1.0.
   * @param scale the new scale factor for the gradient
   * @return this
   */
  public NiftyLinearGradient setScale(final double scale) {
    assertPositiveScale(scale);
    this.scale = scale;
    return this;
  }

  /**
   * Get the current scale factor.
   * @return the current scale factor or 1.0 if none have been set.
   */
  public double getScale() {
    return scale ;
  }

  /**
   * Flip the color stops in this gradient around.
   * @return this
   */
  public NiftyLinearGradient setFlip() {
    this.flip = true;
    return this;
  }

  /**
   * Return true if this gradient should be flipped. Default is false.
   * @return true when this gradient is supposed to be flipped and false if not
   */
  public boolean isFlip() {
    return flip;
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

  private void assertPositiveScale(final double scale) {
    if (Math.signum(scale) < 0) {
      throw new IllegalArgumentException("scale must be positive but was (" + scale + ")");
    }
  }

  private static double toRad(final double grad) {
    return Math.PI * 2 * grad / 360.;
  }
}

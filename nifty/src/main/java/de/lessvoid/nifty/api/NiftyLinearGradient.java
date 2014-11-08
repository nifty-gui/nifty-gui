/*
 * Copyright (c) 2014, Jens Hohmuth 
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
 * A linear gradient between two points that contains a number of color stops.
 *
 * @author void
 */
public class NiftyLinearGradient {
  private final double x0;
  private final double y0;
  private final double x1;
  private final double y1;
  private final Set<NiftyColorStop> colorStops = new TreeSet<NiftyColorStop>();

  /**
   * Create a linear gradient from (x0, y0) to (x1, y1).
   *
   * @param x0 The x-coordinate of the start point of the gradient
   * @param y0 The y-coordinate of the start point of the gradient
   * @param x1 The x-coordinate of the end point of the gradient
   * @param y1 The y-coordinate of the end point of the gradient
   */
  public NiftyLinearGradient(final double x0, final double y0, final double x1, final double y1) {
    this.x0 = x0;
    this.y0 = y0;
    this.x1 = x1;
    this.y1 = y1;
  }

  /**
   * Copy constructor.
   * @param src the source
   */
  public NiftyLinearGradient(final NiftyLinearGradient src) {
    this.x0 = src.x0;
    this.y0 = src.y0;
    this.x1 = src.x1;
    this.y1 = src.y1;
    this.colorStops.addAll(src.colorStops);
  }

  public void addColorSteps(final List<NiftyColorStop> newColorSteps) {
    this.colorStops.addAll(newColorSteps);
  }

  /**
   * Add a color stop to the gradient at position stop (value between 0.0 and 1.0) with the given color.
   *
   * @param stop a value between 0.0 and 1.0 that represents the position between start and end in a
   * gradient.
   * @param color a NiftyColor to display at stop position.
   */
  public void addColorStop(final double stop, final NiftyColor color) {
    assertValidStop(stop);

    NiftyColorStop newColorStop = new NiftyColorStop(stop, color);
    colorStops.remove(newColorStop);
    colorStops.add(newColorStop);
  }

  /**
   * Get the x-coordinate of the start point of the gradient.
   *
   * @return x-coordinate of the start point of the gradient.
   */
  public double getX0() {
    return x0;
  }

  /**
   * Get the y-coordinate of the start point of the gradient.
   *
   * @return y-coordinate of the start point of the gradient.
   */
  public double getY0() {
    return y0;
  }

  /**
   * Get the x-coordinate of the end point of the gradient.
   *
   * @return x-coordinate of the end point of the gradient.
   */
  public double getX1() {
    return x1;
  }

  /**
   * Get the y-coordinate of the end point of the gradient.
   *
   * @return y-coordinate of the end point of the gradient.
   */
  public double getY1() {
    return y1;
  }

  /**
   * Returns a List of all existing color stops in this gradient. You'll get a new list so you can't modify the list.
   * @return the existing list of NiftyColorStops
   */
  public List<NiftyColorStop> getColorStops() {
    return Collections.unmodifiableList(new ArrayList<NiftyColorStop>(colorStops));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((colorStops == null) ? 0 : colorStops.hashCode());
    long temp;
    temp = Double.doubleToLongBits(x0);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(x1);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(y0);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(y1);
    result = prime * result + (int) (temp ^ (temp >>> 32));
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
    if (colorStops == null) {
      if (other.colorStops != null)
        return false;
    } else if (!colorStops.equals(other.colorStops))
      return false;
    if (Double.doubleToLongBits(x0) != Double.doubleToLongBits(other.x0))
      return false;
    if (Double.doubleToLongBits(x1) != Double.doubleToLongBits(other.x1))
      return false;
    if (Double.doubleToLongBits(y0) != Double.doubleToLongBits(other.y0))
      return false;
    if (Double.doubleToLongBits(y1) != Double.doubleToLongBits(other.y1))
      return false;
    return true;
  }

  private void assertValidStop(final double stop) {
    if (stop < 0.0 || stop > 1.0) {
      throw new IllegalArgumentException("color stop value [" + stop + "] not between 0.0 and 1.0");
    }
  }
}

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
package de.lessvoid.niftyinternal.render.batch;

import de.lessvoid.nifty.spi.NiftyRenderDevice.ColorStop;
import de.lessvoid.niftyinternal.common.InternalNiftyColorStop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A linear gradient between two points that contains a number of color stops. This is a linear gradient as the
 * renderer sees it.
 *
 * @author void
 */
public final class LinearGradient {
  private final double startX;
  private final double startY;
  private final double endX;
  private final double endY;
  private final Set<ColorStop> colorStops = new TreeSet<>();

  /**
   * Create a new LinearGradient.
   *
   * @param startX start x
   * @param startY start y
   * @param endX end x
   * @param endY end y
   * @param colorStops color stops
   */
  public LinearGradient(final double startX, final double startY, final double endX, final double endY, final Set<InternalNiftyColorStop> colorStops) {
    this.startX = startX;
    this.startY = startY;
    this.endX = endX;
    this.endY = endY;
    this.colorStops.addAll(colorStops);
  }

  /**
   * Get the x-coordinate of the start point of the gradient.
   *
   * @return x-coordinate of the start point of the gradient.
   */
  public final double getStartX() {
    return startX;
  }

  /**
   * Get the y-coordinate of the start point of the gradient.
   *
   * @return y-coordinate of the start point of the gradient.
   */
  public final double getStartY() {
    return startY;
  }

  /**
   * Get the x-coordinate of the end point of the gradient.
   *
   * @return x-coordinate of the end point of the gradient.
   */
  public final double getEndX() {
    return endX;
  }

  /**
   * Get the y-coordinate of the end point of the gradient.
   *
   * @return y-coordinate of the end point of the gradient.
   */
  public final double getEndY() {
    return endY;
  }

  /**
   * Returns a List of all existing color stops in this gradient. You'll get a new list so you can't modify the list.
   * @return the existing list of NiftyColorStops
   */
  public final List<ColorStop> getColorStops() {
    return Collections.unmodifiableList(new ArrayList<>(colorStops));
  }

  @Override
  public final int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((colorStops == null) ? 0 : colorStops.hashCode());
    long temp;
    temp = Double.doubleToLongBits(startX);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(endX);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(startY);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(endY);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public final boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    LinearGradient other = (LinearGradient) obj;
    if (colorStops == null) {
      if (other.colorStops != null)
        return false;
    } else if (!colorStops.equals(other.colorStops))
      return false;
    if (Double.doubleToLongBits(startX) != Double.doubleToLongBits(other.startX))
      return false;
    if (Double.doubleToLongBits(endX) != Double.doubleToLongBits(other.endX))
      return false;
    if (Double.doubleToLongBits(startY) != Double.doubleToLongBits(other.startY))
      return false;
    if (Double.doubleToLongBits(endY) != Double.doubleToLongBits(other.endY))
      return false;
    return true;
  }
}

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
package de.lessvoid.nifty.internal.render.batch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import de.lessvoid.nifty.api.NiftyColorStop;
import de.lessvoid.nifty.api.NiftyLinearGradient;

/**
 * A linear gradient between two points that contains a number of color stops. This is a linear gradient as the
 * renderer sees it. The points it carries are the resolved, e.g. NiftyNode coordinate system specific points.
 *
 * @author void
 */
public class LinearGradient {
  private final double startX;
  private final double startY;
  private final double endX;
  private final double endY;
  private final Set<NiftyColorStop> colorStops = new TreeSet<NiftyColorStop>();

  /**
   * Create a linear gradient considering the given rectangle (x0, y0) - (x1, y1) and the angle and colorstop from the
   * NiftyLinearGradient.
   *
   * @param x0 The x-coordinate of the rectangle
   * @param y0 The y-coordinate of the rectangle
   * @param x1 The x-coordinate of the rectangle
   * @param y1 The y-coordinate of the rectangle
   * @param gradient The NiftyLinearGradient parameter
   */
  public LinearGradient(final double x0, final double y0, final double x1, final double y1, final NiftyLinearGradient gradient) {
    double w = x1 - x0;
    double h = y1 - y0;
    double mx = x0 + w / 2;
    double my = y0 + h / 2;
    double angle = gradient.getAngleInRadiants();
    double sinAngle = Math.sin(angle);
    double cosAngle = Math.cos(angle);
    double length = Math.abs(w * sinAngle) +
                    Math.abs(h * cosAngle);
    double halfLength = length / 2;
    this.startX = mx - halfLength * sinAngle;
    this.startY = my - halfLength * cosAngle;
    this.endX = mx + halfLength * sinAngle;
    this.endY = my + halfLength * cosAngle;
    this.colorStops.addAll(flip(applyScale(gradient.getColorStops(), gradient.getScale()), gradient.isFlip()));
  }

  /**
   * Copy constructor.
   * @param src the source
   */
  public LinearGradient(final LinearGradient src) {
    this.startX = src.startX;
    this.startY = src.startY;
    this.endX = src.endX;
    this.endY = src.endY;
    this.colorStops.addAll(src.colorStops);
  }

  /**
   * Get the x-coordinate of the start point of the gradient.
   *
   * @return x-coordinate of the start point of the gradient.
   */
  public double getStartX() {
    return startX;
  }

  /**
   * Get the y-coordinate of the start point of the gradient.
   *
   * @return y-coordinate of the start point of the gradient.
   */
  public double getStartY() {
    return startY;
  }

  /**
   * Get the x-coordinate of the end point of the gradient.
   *
   * @return x-coordinate of the end point of the gradient.
   */
  public double getEndX() {
    return endX;
  }

  /**
   * Get the y-coordinate of the end point of the gradient.
   *
   * @return y-coordinate of the end point of the gradient.
   */
  public double getEndY() {
    return endY;
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
  public boolean equals(Object obj) {
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

  private List<NiftyColorStop> flip(final List<NiftyColorStop> colorStops, final boolean flip) {
    if (!flip) {
      return colorStops;
    }

    // make a copy of the stops
    ArrayList<NiftyColorStop> reversedList = new ArrayList<NiftyColorStop>(colorStops);

    // reverse the order (so that the colors are in the flipped order)
    Collections.reverse(reversedList);

    // since NiftyColorStop is not mutable we need to return a new list
    ArrayList<NiftyColorStop> result = new ArrayList<NiftyColorStop>();

    // now flip the color stops as well 
    for (int i=0; i<reversedList.size(); i++) {
      NiftyColorStop stop = reversedList.get(i);
      result.add(new NiftyColorStop((stop.getStop() - 1.0) * -1.0, stop.getColor()));
    }
    return result;
  }

  private List<NiftyColorStop> applyScale(final List<NiftyColorStop> colorStops, final double scale) {
    // just shortcut the regular case
    if (scale == 1.0) {
      return colorStops;
    }

    List<NiftyColorStop> result = new ArrayList<NiftyColorStop>();
    for (int i=0; i<colorStops.size(); i++) {
      NiftyColorStop colorStop = colorStops.get(i);
      double newStop = colorStop.getStop() * scale;
      result.add(new NiftyColorStop(newStop, colorStop.getColor()));

      // having colorStops > 1.0 doesn't really make sense since we'll never see them
      if (newStop >= 1.0) {
        return result;
      }
    }
    return result;
  }
}

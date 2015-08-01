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
package de.lessvoid.nifty.api.types;

/**
 * A NiftyColorStop contains a stop value between 0.0 and 1.0 and a NiftyColor and is used by the
 * NiftyGradients.
 *
 * A NiftyColorStop is considered equal to another NiftyColorStop if the stop value is equals. Please note that this
 * means you can't have two NiftyColorStops with the same stop value but different colors.
 *
 * @author void
 */
public class NiftyColorStop implements Comparable<NiftyColorStop> {
  private final double stop;
  private final NiftyColor color;

  public NiftyColorStop(final double stop, final NiftyColor color) {
    this.stop = stop;
    this.color = color;
  }

  public double getStop() {
    return stop;
  }

  public NiftyColor getColor() {
    return color;
  }

  @Override
  public String toString() {
    return stop + ": " + color;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(stop);
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
    NiftyColorStop other = (NiftyColorStop) obj;
    if (Double.doubleToLongBits(stop) != Double.doubleToLongBits(other.stop))
      return false;
    return true;
  }

  @Override
  public int compareTo(final NiftyColorStop colorStop) {
    return Double.valueOf(stop).compareTo(colorStop.stop);
  }
}

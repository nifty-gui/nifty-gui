package de.lessvoid.nifty.api;

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

  NiftyColorStop(final double stop, final NiftyColor color) {
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

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

  private void assertValidStop(final double stop) {
    if (stop < 0.0 || stop > 1.0) {
      throw new IllegalArgumentException("color stop value [" + stop + "] not between 0.0 and 1.0");
    }
  }
}

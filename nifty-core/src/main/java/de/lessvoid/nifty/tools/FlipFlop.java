package de.lessvoid.nifty.tools;

import javax.annotation.Nonnull;

/**
 * This objects contains two equal objects that can be switched upon request.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class FlipFlop<T> {
  /**
   * The object one that is returned as first object as long as {@link #flip} is {@code false}.
   */
  @Nonnull
  private final T obj1;

  /**
   * The object two that is returned as second object as long as {@link #flip} is {@code false}.
   */
  @Nonnull
  private final T obj2;

  /**
   * The flip.
   */
  private boolean flip;

  /**
   * Create a new flip flop and set the two instances required.
   *
   * @param obj1 the instance of the first object
   * @param obj2 the instance of the second object
   */
  public FlipFlop(@Nonnull final T obj1, @Nonnull final T obj2) {
    this.obj1 = obj1;
    this.obj2 = obj2;
  }

  /**
   * Flip the switch and turn around the references of the object. The object that was the first object before,
   * now becomes the second.
   */
  public void flip() {
    flip = !flip;
  }

  /**
   * Get the first object.
   *
   * @return the first object
   */
  @Nonnull
  public T getFirst() {
    if (flip) {
      return obj2;
    }
    return obj1;
  }

  /**
   * Get the second object.
   *
   * @return the second object
   */
  @Nonnull
  public T getSecond() {
    if (flip) {
      return obj1;
    }
    return obj2;
  }
}

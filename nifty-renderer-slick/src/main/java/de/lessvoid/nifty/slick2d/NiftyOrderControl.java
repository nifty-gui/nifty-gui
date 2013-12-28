package de.lessvoid.nifty.slick2d;

import javax.annotation.Nonnull;

/**
 * This interface defined the functions to control the order of rendering and updating the Nifty-GUI and the game.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface NiftyOrderControl {
  /**
   * Get the current rendering order.
   *
   * @return the current render order
   */
  @Nonnull
  NiftyRenderOrder getRenderOrder();

  /**
   * Get the current updating order.
   *
   * @return the current update order
   */
  @Nonnull
  NiftyUpdateOrder getUpdateOrder();

  /**
   * Set the rendering order that is supposed to be used.
   *
   * @param order the render order
   */
  void setRenderOrder(@Nonnull NiftyRenderOrder order);

  /**
   * Set the updating order that is supposed to be used.
   *
   * @param order the update order
   */
  void setUpdateOrder(@Nonnull NiftyUpdateOrder order);
}

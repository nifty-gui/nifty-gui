package de.lessvoid.nifty.slick2d;

import javax.annotation.Nonnull;

/**
 * This enumerator contains the possible settings for the render order of the Nifty-GUI and the game in the overlay
 * game
 * and game state implementations.
 *
 * @author Marting Karing &lt;nitram@illarion.org&gt;
 */
public enum NiftyRenderOrder {
  /**
   * Using this enumerator value the game will be rendered first, followed by the Nifty-GUI.
   */
  @Nonnull
  NiftyOverlay,

  /**
   * Using this enumerator value the Nifty-GUI will be rendered first, followed by the game.
   */
  @Nonnull
  NiftyBackground
}

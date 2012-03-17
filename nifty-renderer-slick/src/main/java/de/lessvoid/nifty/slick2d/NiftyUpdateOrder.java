package de.lessvoid.nifty.slick2d;

/**
 * This enumerator contains the possible settings for the update order of the Nifty-GUI and the game in the overlay
 * game
 * and game state implementations.
 *
 * @author Marting Karing &lt;nitram@illarion.org&gt;
 */
public enum NiftyUpdateOrder {
  /**
   * Using this enumerator value the game will be updated first, followed by the Nifty-GUI.
   */
  NiftyLast,

  /**
   * Using this enumerator value the Nifty-GUI will be updated first, followed by the game.
   */
  NiftyFirst
}

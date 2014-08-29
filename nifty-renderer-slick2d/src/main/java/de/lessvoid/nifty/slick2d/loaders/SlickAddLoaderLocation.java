package de.lessvoid.nifty.slick2d.loaders;

/**
 * This enumerator is used to control the order of the loaders that is applied when adding a new loader to the loaders
 * list.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public enum SlickAddLoaderLocation {
  /**
   * Add the loader where ever you like.
   */
  doNotCare,

  /**
   * Add the loader to the top of the list, so its queried first.
   */
  first,

  /**
   * Add the loader to the bottom of the list, so its queried last.
   */
  last
}

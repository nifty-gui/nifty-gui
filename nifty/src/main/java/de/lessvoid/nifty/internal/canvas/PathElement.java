package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.render.batch.BatchManager;

/**
 * A PathElement.
 * @author void
 */
public interface PathElement {

  /**
   * Render this path element to the batchManager using data from the Context and the parameters given.
   *
   * @param context the Context
   * @param batchManager the BatchManager
   * @param first this is the first path element
   * @param last this is the last path element
   */
  void render(
      Context context,
      BatchManager batchManager,
      boolean first,
      boolean last);
}
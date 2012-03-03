package de.lessvoid.nifty.slick2d.render.cursor;

import org.newdawn.slick.Graphics;

/**
 * This class is created to implement native mouse cursors with slick. Those cursor are not in need of the render
 * function and so this function is implemented empty.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public abstract class AbstractNativeSlickMouseCursor implements SlickMouseCursor {
  /**
   * Render the cursor. For the native implementations this function does nothing at all.
   */
  @Override
  public final void render(final Graphics g, final int x, final int y) {
    // nothing
  }
}

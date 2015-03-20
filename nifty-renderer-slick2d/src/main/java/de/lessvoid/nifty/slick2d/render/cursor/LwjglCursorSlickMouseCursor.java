package de.lessvoid.nifty.slick2d.render.cursor;

import org.lwjgl.input.Cursor;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import javax.annotation.Nonnull;

/**
 * This implementation of the slick mouse cursor uses the LWJGL cursor implementation to display the mouse cursor.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class LwjglCursorSlickMouseCursor extends AbstractNativeSlickMouseCursor {
  /**
   * The cursor that is displayed.
   */
  private final Cursor cursor;

  /**
   * The game container that is supposed to show this cursor.
   */
  private final GameContainer container;

  /**
   * Create a new slick mouse cursor that wraps a LWJGL cursor.
   *
   * @param lwjglCursor the lwjgl cursor
   * @param container the game container that is now supposed to show this cursor
   */
  public LwjglCursorSlickMouseCursor(final Cursor lwjglCursor, @Nonnull final GameContainer container) {
    cursor = lwjglCursor;
    this.container = container;
  }

  /**
   * Start displaying the cursor.
   */
  @Override
  public void enable() {
    try {
      container.setMouseCursor(cursor, 0, 0);
    } catch (@Nonnull final SlickException ignored) {
      // enabling failed
    }
  }

  /**
   * Switch back to the native default cursor.
   */
  @Override
  public void disable() {
    container.setDefaultMouseCursor();
  }

  /**
   * Dispose the cursor.
   */
  @Override
  public void dispose() {
    cursor.destroy();
  }
}

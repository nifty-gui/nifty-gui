package de.lessvoid.nifty.slick2d.render.cursor;

import org.lwjgl.input.Cursor;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

/**
 * This implementation of the slick mouse cursor uses the LWJGL cursor
 * implementation to display the mouse cursor.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class LwjglCursorSlickMouseCursor extends AbstractNativeSlickMouseCursor {
  /**
   * The cursor that is displayed.
   */
  private final Cursor cursor;

  /**
   * Create a new slick mouse cursor that wraps a LWJGL cursor.
   * 
   * @param lwjglCursor
   *          the lwjgl cursor
   */
  public LwjglCursorSlickMouseCursor(final Cursor lwjglCursor) {
    super();
    cursor = lwjglCursor;
  }

  /**
   * Switch back to the native default cursor.
   */
  @Override
  public void disableCursor(final GameContainer container) {
    container.setDefaultMouseCursor();
  }

  /**
   * Dispose the cursor.
   */
  @Override
  public void dispose() {
    cursor.destroy();
  }

  /**
   * Start displaying the cursor.
   */
  @Override
  public void enableCursor(final GameContainer container) {
    try {
      container.setMouseCursor(cursor, 0, 0);
    } catch (final SlickException e) {
      // enabling failed
    }
  }
}

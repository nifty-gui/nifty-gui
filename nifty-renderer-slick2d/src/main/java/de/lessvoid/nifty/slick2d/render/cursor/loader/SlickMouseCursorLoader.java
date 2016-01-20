package de.lessvoid.nifty.slick2d.render.cursor.loader;

import de.lessvoid.nifty.slick2d.loaders.SlickLoader;
import de.lessvoid.nifty.slick2d.render.cursor.SlickLoadCursorException;
import de.lessvoid.nifty.slick2d.render.cursor.SlickMouseCursor;
import org.newdawn.slick.GameContainer;

import javax.annotation.Nonnull;

/**
 * This interface defines the loader of a mouse cursor.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface SlickMouseCursorLoader extends SlickLoader {
  /**
   * Load a mouse cursor.
   *
   * @param filename the name of the file that stores the cursor image
   * @param hotspotX the x coordinate of the hotspot (left->right)
   * @param hotspotY the y coordinate of the hotspot (bottom->top)
   * @param container the game container that is now supposed to show this cursor
   * @return the loaded mouse cursor
   * @throws SlickLoadCursorException in case loading the cursor fails
   */
  @Nonnull
  SlickMouseCursor loadCursor(String filename, int hotspotX, int hotspotY, @Nonnull GameContainer container) throws SlickLoadCursorException;
}

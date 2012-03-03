package de.lessvoid.nifty.slick2d.loaders;

import java.util.Iterator;

import de.lessvoid.nifty.slick2d.render.cursor.SlickLoadCursorException;
import de.lessvoid.nifty.slick2d.render.cursor.SlickMouseCursor;
import de.lessvoid.nifty.slick2d.render.cursor.loader.LwjglCursorSlickMouseCursorLoader;
import de.lessvoid.nifty.slick2d.render.cursor.loader.SlickMouseCursorLoader;

/**
 * This maintains the list of known cursor loaders and queries them one by one in order to load a cursor.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class SlickMouseCursorLoaders extends AbstractSlickLoaders<SlickMouseCursorLoader> {
  /**
   * The singleton instance of this class.
   */
  private static final SlickMouseCursorLoaders INSTANCE = new SlickMouseCursorLoaders();

  /**
   * Get the singleton instance of this class.
   *
   * @return the singleton instance
   */
  public static SlickMouseCursorLoaders getInstance() {
    return INSTANCE;
  }

  /**
   * Private constructor so no instances but the singleton instance are created.
   */
  private SlickMouseCursorLoaders() {
  }

  /**
   * Load a mouse cursor.
   *
   * @param filename the name of the file that stores the image of this cursor
   * @param hotspotX the x coordinate of the cursor hotspot
   * @param hotspotY the y coordinate of the cursor hotspot
   * @return the loaded mouse cursor
   * @throws IllegalArgumentException in case all loaders fail to load this cursor
   */
  @SuppressWarnings("TypeMayBeWeakened")
  public SlickMouseCursor loadCursor(final String filename, final int hotspotX, final int hotspotY) {
    final Iterator<SlickMouseCursorLoader> itr = getLoaderIterator();

    while (itr.hasNext()) {
      try {
        return itr.next().loadCursor(filename, hotspotX, hotspotY);
      } catch (final SlickLoadCursorException ignored) {
        // this loader failed... does not matter
      }
    }

    throw new IllegalArgumentException("Failed to load cursor \"" + filename + "\".");
  }

  /**
   * Add the default loaders.
   */
  @Override
  public void loadDefaultLoaders(final SlickAddLoaderLocation order) {
    addLoader(new LwjglCursorSlickMouseCursorLoader(), order);
  }
}

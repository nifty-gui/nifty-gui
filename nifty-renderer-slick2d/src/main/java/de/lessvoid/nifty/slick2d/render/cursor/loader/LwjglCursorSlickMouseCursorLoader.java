package de.lessvoid.nifty.slick2d.render.cursor.loader;

import de.lessvoid.nifty.slick2d.render.cursor.LwjglCursorSlickMouseCursor;
import de.lessvoid.nifty.slick2d.render.cursor.SlickLoadCursorException;
import de.lessvoid.nifty.slick2d.render.cursor.SlickMouseCursor;
import org.lwjgl.LWJGLException;
import org.newdawn.slick.opengl.CursorLoader;
import org.newdawn.slick.opengl.ImageDataFactory;
import org.newdawn.slick.opengl.LoadableImageData;
import org.newdawn.slick.util.ResourceLoader;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * This loader is used to load slick mouse cursors that work with LWJGL mouse cursors.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class LwjglCursorSlickMouseCursorLoader implements SlickMouseCursorLoader {

  /**
   * Load a new mouse cursor.
   */
  @Nonnull
  @Override
  public SlickMouseCursor loadCursor(
      final String filename, final int hotspotX, final int hotspotY) throws SlickLoadCursorException {

    final LoadableImageData data = ImageDataFactory.getImageDataFor(filename);
    try {
      data.loadImage(ResourceLoader.getResourceAsStream(filename), true, true, null);
    } catch (@Nonnull final IOException e) {
      throw new SlickLoadCursorException("Failed loading cursor.", e);
    }
    try {
      return new LwjglCursorSlickMouseCursor(CursorLoader.get().getCursor(data, hotspotX, hotspotY));
    } catch (@Nonnull final LWJGLException e) {
      throw new SlickLoadCursorException("Failed loading cursor.", e);
    } catch (@Nonnull final IOException e) {
      throw new SlickLoadCursorException("Failed loading cursor.", e);
    }
  }

}

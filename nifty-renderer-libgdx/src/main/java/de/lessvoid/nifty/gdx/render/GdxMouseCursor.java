package de.lessvoid.nifty.gdx.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.lessvoid.nifty.spi.render.MouseCursor;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public final class GdxMouseCursor implements MouseCursor {
  @Nonnull
  private static final Logger log = Logger.getLogger(GdxMouseCursor.class.getName());
  @Nonnull
  private final GdxImage cursorImage;
  private final int hotspotX;
  private final int hotspotY;

  /**
   * @param cursorImage The pre-loaded cursor image.
   * @param hotspotX The x location of the pixel in the cursor image that actually clicks
   * @param hotspotY The y location of the pixel in the cursor image that actually clicks
   */
  public GdxMouseCursor(@Nonnull GdxImage cursorImage, final int hotspotX, final int hotspotY) {
    this.cursorImage = cursorImage;
    this.hotspotX = hotspotX;
    this.hotspotY = hotspotY;
  }

  /**
   * Enables (shows) the mouse cursor image specified in {@link #GdxMouseCursor(GdxImage, int, int)}. Replaces (hides)
   * the system mouse cursor image.
   */
  @Override
  public void enable() {
    try {
      if (cursorImage.hasPixmap()) {
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursorImage.getPixmap(), hotspotX, hotspotY));
      }
    } catch (GdxRuntimeException e) {
      log.log(Level.SEVERE, "Applying the mouse cursor failed!", e);
    }
  }

  /**
   * Disables (hides) the mouse cursor image specified in {@link #GdxMouseCursor(GdxImage, int, int)}. Restores (shows)
   * the system mouse cursor image.
   */
  @Override
  public void disable() {
    Gdx.graphics.setCursor(Gdx.graphics.newCursor(null, hotspotX, hotspotY));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose() {
  }
}

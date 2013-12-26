package de.lessvoid.nifty.gdx.render;

import com.badlogic.gdx.Gdx;
import de.lessvoid.nifty.gdx.render.batch.GdxBatchRenderImage;
import de.lessvoid.nifty.spi.render.MouseCursor;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public final class GdxMouseCursor implements MouseCursor {
  // location of the pixel in the cursor texture that actually clicks
  private final int hotspotX;
  private final int hotspotY;
  private final GdxBatchRenderImage cursorImage;

  /**
   * @param cursorImage The pre-loaded cursor image.
   * @param hotspotX    The x location of the pixel in the cursor image that actually clicks
   * @param hotspotY    The y location of the pixel in the cursor image that actually clicks
   */
  public GdxMouseCursor(GdxBatchRenderImage cursorImage, final int hotspotX, final int hotspotY) {
    this.cursorImage = cursorImage;
    this.hotspotX = hotspotX;
    this.hotspotY = hotspotY;
  }

  /**
   * Enables (shows) the mouse cursor image specified in {@link #GdxMouseCursor(de.lessvoid.nifty.gdx.render.batch
   * .GdxBatchRenderImage, int, int)}.
   * Replaces (hides) the system mouse cursor image.
   */
  public void enable() {
    Gdx.input.setCursorImage(cursorImage.asPixmap(), hotspotX, hotspotY);
  }

  /**
   * Disables (hides) the mouse cursor image specified in {@link #GdxMouseCursor(de.lessvoid.nifty.gdx.render.batch
   * .GdxBatchRenderImage, int, int)}.
   * Restores (shows) the system mouse cursor image.
   */
  public void disable() {
    Gdx.input.setCursorImage(null, 0, 0);
  }

  @Override
  public void dispose() {
  }
}

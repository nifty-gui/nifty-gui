package de.lessvoid.nifty.renderer.lwjgl.render;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import de.lessvoid.nifty.spi.render.RenderImage;

/**
 * Lwjgl/Slick implementation for the RenderImage interface.
 * @author void
 */
public class RenderImageLwjgl implements RenderImage {
  private org.newdawn.slick.Image image;

  /**
   * Create a new RenderImage.
   * @param renderTools 
   * @param name the name of the resource in the file system
   * @param filterParam use linear filter (true) or nearest filter (false)
   */
  public RenderImageLwjgl(final String name, final boolean filterParam) {
    try {
      int filter = Image.FILTER_NEAREST;
      if (filterParam) {
        filter = Image.FILTER_LINEAR;
      }
      this.image = new org.newdawn.slick.Image(name, false, filter);
    } catch (SlickException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get width of image.
   * @return width
   */
  public int getWidth() {
    return image.getWidth();
  }

  /**
   * Get height of image.
   * @return height
   */
  public int getHeight() {
    return image.getHeight();
  }

  public org.newdawn.slick.Image getImage() {
    return image;
  }
}

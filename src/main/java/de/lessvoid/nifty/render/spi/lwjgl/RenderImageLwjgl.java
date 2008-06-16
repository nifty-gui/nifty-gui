package de.lessvoid.nifty.render.spi.lwjgl;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import de.lessvoid.nifty.render.spi.RenderImage;
import de.lessvoid.nifty.tools.Color;

/**
 * Lwjgl/Slick implementation for the RenderImage interface.
 * @author void
 */
public class RenderImageLwjgl implements RenderImage {

  /**
   * The slick image.
   */
  private org.newdawn.slick.Image image;

  /**
   * Create a new RenderImage.
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

  /**
   * Render the image using the given Box to specify the render attributes.
   * @param x x
   * @param y y
   * @param width width
   * @param height height
   * @param color color
   * @param scale scale
   */
  public void render(
      final int x, final int y, final int width, final int height, final Color color, final float scale) {
    RenderTools.beginRender();
    GL11.glTranslatef(x + width / 2, y + height / 2, 0.0f);
    GL11.glScalef(scale, scale, 1.0f);
    GL11.glTranslatef(-(x + width / 2), -(y + height / 2), 0.0f);
    image.bind();
    image.draw(x, y, width, height, convertToSlickColor(color));
    RenderTools.endRender();
  }

  /**
   * Render sub image.
   * @param x x
   * @param y y
   * @param w w
   * @param h h
   * @param srcX x
   * @param srcY y
   * @param srcW w
   * @param srcH h
   * @param color color
   */
  public void render(
      final int x,
      final int y,
      final int w,
      final int h,
      final int srcX,
      final int srcY,
      final int srcW,
      final int srcH,
      final Color color) {
    RenderTools.beginRender();
    image.bind();
    image.draw(x, y, x + w, y + h, srcX, srcY, srcX + srcW, srcY + srcH, convertToSlickColor(color));
    RenderTools.endRender();
  }

  /**
   * Convert a Nifty color to a Slick color.
   * @param color nifty color
   * @return slick color
   */
  private org.newdawn.slick.Color convertToSlickColor(final Color color) {
    if (color != null) {
      return new org.newdawn.slick.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    } else {
      return new org.newdawn.slick.Color(1.0f, 1.0f, 1.0f, 1.0f);
    }
  }
}

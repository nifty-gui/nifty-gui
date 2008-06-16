package de.lessvoid.nifty.render;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import de.lessvoid.nifty.render.spi.RenderImage;
import de.lessvoid.nifty.render.spi.lwjgl.RenderTools;
import de.lessvoid.nifty.tools.Color;

/**
 * NiftyImage.
 * @author void
 */
public class NiftyImage {

  /**
   * RenderImage.
   */
  private RenderImage image;

  /**
   * sub image type to use.
   */
  private NiftyImageMode subImageMode;

  /**
   * create new NiftyImage.
   * @param createImage RenderImage
   */
  public NiftyImage(final RenderImage createImage) {
    this.image = createImage;
    this.subImageMode = NiftyImageMode.normal();
  }

  /**
   * Get the width of the image.
   * @return width of image in pixel
   */
  public int getWidth() {
    return image.getWidth();
  }

  /**
   * Get the height of the image.
   * @return height of image in pixel
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
    subImageMode.render(image, x, y, width, height, color, scale);
  }

  /**
   * Set a new sub image active state.
   * @param newSubImageMode new type
   */
  public void setImageMode(final NiftyImageMode newSubImageMode) {
    this.subImageMode = newSubImageMode;
  }
}

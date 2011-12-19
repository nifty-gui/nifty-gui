package de.lessvoid.nifty.slick2d.render.image;

import org.newdawn.slick.Graphics;

import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

/**
 * This slick render image extends the normal render image in order to allow the
 * render device to draw this images easily.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface SlickRenderImage extends RenderImage {
  /**
   * Draw the entire image on the screen.
   * 
   * @param g
   *          the graphics instance that is used for rendering
   * @param x
   *          the x coordinate of the image on the screen
   * @param y
   *          the y coordinate of the image on the screen
   * @param width
   *          the width of the image on the screen
   * @param height
   *          the height of the image on the screen
   * @param color
   *          the color that is applied to the image
   * @param scale
   *          scaling factor that is applied to the image, the image is scaled
   *          around the center of the image
   */
  void renderImage(Graphics g, int x, int y, int width, int height, Color color, float scale);

  /**
   * Draw a part of a image on the screen.
   * 
   * @param g
   *          the graphics instance that is used for rendering
   * @param x
   *          the x coordinate on the screen to draw the image to
   * @param y
   *          the y coordinate on the screen to draw the image to
   * @param w
   *          the width of the image on the screen
   * @param h
   *          the height of the image on the screen
   * @param srcX
   *          the x coordinate on the image
   * @param srcY
   *          the y coordinate on the image
   * @param srcW
   *          the width of the sub-image that is supposed to be drawn
   * @param srcH
   *          the height of the sub-image that is supposed to be drawn
   * @param color
   *          the color that is supposed to be applied to the image
   * @param scale
   *          the scaling factor that should be applied to the image
   * @param centerX
   *          the x coordinate on the screen the image is scaled around
   * @param centerY
   *          the y coordinate on the screen the image is scaled around
   */
  void renderImage(
      Graphics g,
      int x,
      int y,
      int w,
      int h,
      int srcX,
      int srcY,
      int srcW,
      int srcH,
      Color color,
      float scale,
      int centerX,
      int centerY);
}

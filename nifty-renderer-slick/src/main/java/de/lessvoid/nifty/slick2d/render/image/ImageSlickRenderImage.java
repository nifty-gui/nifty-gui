package de.lessvoid.nifty.slick2d.render.image;

import de.lessvoid.nifty.slick2d.render.SlickRenderUtils;
import de.lessvoid.nifty.tools.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * This slick render image implementation uses a Slick image to draw on the screen.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class ImageSlickRenderImage implements SlickRenderImage {
  /**
   * The image that is used for the rendering operation.
   */
  private final Image image;

  /**
   * This instance of a slick color is used to avoid the need to create a new color instance every time this image is
   * rendered.
   */
  private final org.newdawn.slick.Color slickColor;

  /**
   * Create this render image that is supposed to render a specified Slick image.
   *
   * @param usedImage the image to draw
   */
  public ImageSlickRenderImage(final Image usedImage) {
    image = usedImage;
    slickColor = new org.newdawn.slick.Color(0.0f, 0.0f, 0.0f, 0.0f);
  }

  /**
   * Dispose this image. After calling this method its not possible anymore to render this image. Also all functions of
   * this image are likely to yield invalid results.
   */
  @Override
  public void dispose() {
    try {
      getImage().destroy();
    } catch (final SlickException ignored) {
      // Destroying failed... does not matter
    }
  }

  /**
   * Get the height of the image.
   */
  @Override
  public int getHeight() {
    return getImage().getHeight();
  }

  /**
   * Get the image that is drawn by this render image. When overwriting this class its possible to alter this function
   * in order to receive the image in different ways. The default implementation uses the image stored in this
   * instance.
   *
   * @return the used image
   */
  protected Image getImage() {
    return image;
  }

  /**
   * Get the width of the image.
   */
  @Override
  public int getWidth() {
    return getImage().getWidth();
  }

  @Override
  public void renderImage(
      final Graphics g,
      final int x,
      final int y,
      final int width,
      final int height,
      final Color color,
      final float scale) {

    final int centerX = x + (width >> 1);
    final int centerY = y + (height >> 1);

    renderImage(g, x, y, width, height, 0, 0, getWidth(), getHeight(), color, scale, centerX, centerY);
  }

  @Override
  public void renderImage(
      final Graphics g,
      final int x,
      final int y,
      final int w,
      final int h,
      final int srcX,
      final int srcY,
      final int srcW,
      final int srcH,
      final Color color,
      final float scale,
      final int centerX,
      final int centerY) {

    g.pushTransform();
    g.translate(centerX, centerY);
    g.scale(scale, scale);
    g.translate(-centerX, -centerY);

    g.drawImage(getImage(), x, y, x + w, y + h, srcX, srcY, srcX + srcW, srcY + srcH,
        SlickRenderUtils.convertColorNiftySlick(color, slickColor));

    g.popTransform();
  }
}

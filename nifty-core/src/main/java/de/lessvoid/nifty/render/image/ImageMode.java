package de.lessvoid.nifty.render.image;

import de.lessvoid.nifty.Parameterizable;
import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

public interface ImageMode extends Parameterizable {

  /**
   * Render image.
   * 
   * @param renderImage RenderImage
   * @param x x
   * @param y y
   * @param width width
   * @param height height
   * @param color color
   * @param scale scale
   */
  void render(
      final RenderDevice renderDevice,
      final RenderImage renderImage,
      final int x,
      final int y,
      final int width,
      final int height,
      final Color color,
      final float scale);

  Size getImageNativeSize(NiftyImage image);
}

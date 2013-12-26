package de.lessvoid.nifty.slick2d.render.image.loader;

import de.lessvoid.nifty.slick2d.render.image.ImageSlickRenderImage;
import de.lessvoid.nifty.slick2d.render.image.SlickLoadImageException;
import de.lessvoid.nifty.slick2d.render.image.SlickRenderImage;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import javax.annotation.Nonnull;

/**
 * This image loader takes care for loading Slick image based RenderImages.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class ImageSlickRenderImageLoader implements SlickRenderImageLoader {
  /**
   * Load the image.
   */
  @Nonnull
  @Override
  public SlickRenderImage loadImage(final String filename, final boolean filterLinear) throws SlickLoadImageException {
    try {
      final int filter = filterLinear ? Image.FILTER_LINEAR : Image.FILTER_NEAREST;
      final Image image = new Image(filename, false, filter);
      return new ImageSlickRenderImage(image);
    } catch (@Nonnull final SlickException e) {
      throw new SlickLoadImageException("Loading the image \"" + filename + "\" failed.", e);
    }
  }
}

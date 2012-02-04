package de.lessvoid.nifty.slick2d.render.image.loader;

import de.lessvoid.nifty.slick2d.loaders.SlickLoader;
import de.lessvoid.nifty.slick2d.render.image.SlickLoadImageException;
import de.lessvoid.nifty.slick2d.render.image.SlickRenderImage;

/**
 * This interface defines a image loader that loads a image to be used by Nifty.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface SlickRenderImageLoader extends SlickLoader {
  /**
   * Load a image that is defined by the filename.
   *
   * @param filename the name of the file to load
   * @param filterLinear {@code true} in case the image is supposed to be filtered linear
   * @return the loaded image
   * @throws SlickLoadImageException in case loading the image fails
   */
  SlickRenderImage loadImage(String filename, boolean filterLinear) throws SlickLoadImageException;
}

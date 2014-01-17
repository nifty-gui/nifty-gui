package de.lessvoid.nifty.render.io;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class ImageLoaderFactory {
  public static ImageLoader createImageLoader(@Nonnull final String imageFilename) {
    return imageFilename.endsWith(".tga") ? new TGAImageLoader() : new DefaultImageLoader();
  }
}

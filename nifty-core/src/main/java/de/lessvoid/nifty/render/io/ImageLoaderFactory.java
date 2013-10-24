package de.lessvoid.nifty.render.io;

import javax.annotation.Nonnull;

public class ImageLoaderFactory {
  public static ImageLoader createImageLoader(@Nonnull final String imageFilename) {
    if (imageFilename.endsWith(".tga")) {
      return new TGAImageLoader();
    }
    return new ImageIOImageLoader();
  }
}

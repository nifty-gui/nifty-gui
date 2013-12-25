package de.lessvoid.nifty.render;

import de.lessvoid.nifty.render.NiftyImageManager.ReferencedCountedImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * The purpose of this interface is to extract everything that is different in the way the NiftyImageManager needs
 * to do when using a BatchRenderDevice implementation. There will be a *ExtStandard implementation that does nothing
 * and an *ExtBatch implementation that will do additional management based on Nifty Screen.
 *
 * @author void
 */
public interface NiftyImageManagerExt<T extends ReferencedCountedImage> {

  void registerImage(@Nonnull Screen screen, @Nonnull T image);

  void unregisterImage(@Nonnull T reference);

  void uploadScreenImages(@Nonnull Screen screen);

  void unloadScreenImages(
      @Nonnull Screen screen,
      @Nonnull RenderDevice renderDevice,
      @Nonnull Collection<T> imageCache);

  void screenAdded(@Nonnull Screen screen);

  void screenRemoved(@Nonnull Screen screen);

  void addScreenInfo(@Nonnull StringBuffer result);

  @Nonnull
  T createReferencedCountedImage(
      @Nonnull RenderDevice renderDevice,
      @Nonnull Screen screen,
      @Nonnull String filename,
      boolean filterLinear,
      @Nonnull RenderImage renderImage,
      @Nonnull String key);

}

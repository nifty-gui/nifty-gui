package de.lessvoid.nifty.render;

import java.util.Collection;

import de.lessvoid.nifty.render.NiftyImageManager.ReferencedCountedImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;

/**
 * The purpose of this interface is to extract everything that is different in the way the NiftyImageManager needs
 * to do when using a BatchRenderDevice implementation. There will be a *ExtStandard implementation that does nothing
 * and an *ExtBatch implementation that will do additional management based on Nifty Screen. 
 * @author void
 */
public interface NiftyImageManagerExt<T extends ReferencedCountedImage> {

  void registerImage(Screen screen, T image);
  void unregisterImage(T reference);
  void uploadScreenImages(Screen screen);
  void unloadScreenImages(Screen screen, RenderDevice renderDevice, Collection<T> imageCache);
  void screenAdded(Screen screen);
  void screenRemoved(Screen screen);
  void addScreenInfo(StringBuffer result);
  
  T createReferencedCountedImage(
      RenderDevice renderDevice,
      Screen screen,
      String filename,
      boolean filterLinear,
      RenderImage renderImage,
      String key);

}

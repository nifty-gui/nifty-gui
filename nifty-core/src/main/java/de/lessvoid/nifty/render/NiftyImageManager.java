package de.lessvoid.nifty.render;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.lessvoid.nifty.NiftyStopwatch;
import de.lessvoid.nifty.batch.BatchRenderDevice;
import de.lessvoid.nifty.render.NiftyImageManagerExtBatch.ReferencedCountedImageBatch;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;

public class NiftyImageManager {
  private static Logger log = Logger.getLogger(NiftyImageManager.class.getName());
  private final RenderDevice renderDevice;
  private Map<String, ReferencedCountedImage> imageCache = new HashMap<String, ReferencedCountedImage>();
  private Map<RenderImage, ReferencedCountedImage> backReference = new HashMap<RenderImage, ReferencedCountedImage>();
  private final NiftyImageManagerExt<ReferencedCountedImage> ext;

  public NiftyImageManager(final RenderDevice renderDevice) {
    this.renderDevice = renderDevice;
    this.ext = getExtImpl(renderDevice);
  }

  public RenderImage registerImage(final String filename, final boolean filterLinear, final Screen screen) {
    ReferencedCountedImage image = addImage(filename, filterLinear, screen);
    ext.registerImage(screen, image);
    return image.getRenderImage();
  }

  public void unregisterImage(final RenderImage image) {
    if (backReference.containsKey(image)) {
      ReferencedCountedImage reference = backReference.get(image);
      if (removeImage(reference)) {
        ext.unregisterImage(reference);
      }
    }
  }

  public void uploadScreenImages(final Screen screen) {
    log.fine(">>> uploadScreenImages [" + screen.getScreenId() + "] start");
    NiftyStopwatch.start();

    ext.uploadScreenImages(screen);

    long time = NiftyStopwatch.stop();
    if (log.isLoggable(Level.FINE)) {
      log.fine("{" + String.format("%d", time) + " ms} <<< uploadScreenImages [" + screen.getScreenId() + "]");
    }
    if (log.isLoggable(Level.FINER)) {
      log.finer("{" + String.format("%d", time) + " ms} <<< uploadScreenImages [" + screen.getScreenId() + "] " + getInfoString());
    }
  }

  public void unloadScreenImages(final Screen screen) {
    log.fine(">>> unloadScreenImages [" + screen.getScreenId() + "] start");
    NiftyStopwatch.start();

    ext.unloadScreenImages(screen, renderDevice, imageCache.values());

    long time = NiftyStopwatch.stop();
    if (log.isLoggable(Level.FINE)) {
      log.fine("{" + String.format("%d", time) + " ms} <<< unloadScreenImages [" + screen.getScreenId() + "]");
    }
    if (log.isLoggable(Level.FINER)) {
      log.finer("{" + String.format("%d", time) + " ms} <<< unloadScreenImages [" + screen.getScreenId() + "] " + getInfoString());
    }
  }

  public void screenAdded(final Screen screen) {
    ext.screenAdded(screen);

    if (log.isLoggable(Level.FINER)) {
      log.finer("screenAdded [" + screen.getScreenId() + "] " + getInfoString());
    }
  }

  public void screenRemoved(final Screen screen) {
    ext.screenRemoved(screen);

    if (log.isLoggable(Level.FINER)) {
      log.finer("screenRemoved [" + screen.getScreenId() + "] " + getInfoString());
    }
  }

  public RenderImage reload(final RenderImage image) {
    if (backReference.containsKey(image)) {
      return backReference.get(image).reload();
    }
    return image;
  }

  public String getInfoString() {
    StringBuffer result = new StringBuffer();
    result.append(imageCache.size() + " entries in cache and " + backReference.size() + " backreference entries.");
    ext.addScreenInfo(result);
    return result.toString();
  }

  private NiftyImageManagerExt<ReferencedCountedImage> getExtImpl(final RenderDevice renderer) {
    if (renderer instanceof BatchRenderDevice) {
      // FIXME a generics expert should fix that...
      NiftyImageManagerExtBatch<?> b = new NiftyImageManagerExtBatch<ReferencedCountedImageBatch>(); 
      return (NiftyImageManagerExt<ReferencedCountedImage>) b;
    }
    return new NiftyImageManagerExtStandard<ReferencedCountedImage>();
  }

  private static String buildName(final String filename, final boolean filterLinear) {
    return filename + "|" + filterLinear;
  }

  private ReferencedCountedImage addImage(final String filename, final boolean filterLinear, final Screen screen) {
    String key = buildName(filename, filterLinear);
    if (imageCache.containsKey(key)) {
      ReferencedCountedImage existingImage = imageCache.get(key);
      existingImage.addReference();
      if (log.isLoggable(Level.FINER)) {
        log.finer("[" + screen.getScreenId() + "][" + key + "] refcount++ [" + existingImage.getReferences() + "]");
      }
      return existingImage;
    }
    NiftyStopwatch.start();

    RenderImage renderImage = renderDevice.createImage(filename, filterLinear);
    ReferencedCountedImage newImage = ext.createReferencedCountedImage(renderDevice, screen, filename, filterLinear, renderImage, key);
    backReference.put(renderImage, newImage);
    imageCache.put(key, newImage);

    NiftyStopwatch.stop("imageManager.getImage(" + filename + ")");
    return newImage;
  }

  private boolean removeImage(final ReferencedCountedImage reference) {
    Screen screen = reference.getScreen();
    if (reference.removeReference()) {
      imageCache.remove(reference.getName());
      backReference.remove(reference.getRenderImage());
      if (log.isLoggable(Level.FINER)) {
        log.finer("[" + (screen == null ? "---" : screen.getScreenId()) + "][" + reference.getName() + "] refcount-- [" + reference.getReferences() + "] => DISPOSED");
      }
      return true;
    }
    if (log.isLoggable(Level.FINER)) {
      log.finer("[" + (screen == null ? "---" : screen.getScreenId()) + "][" + reference.getName() + "] refcount-- [" + reference.getReferences() + "]");
    }
    return false;
  }

  public interface ReferencedCountedImage {
    RenderImage reload();
    RenderImage addReference();
    boolean removeReference();
    int getReferences();
    RenderImage getRenderImage();
    String getName();
    Screen getScreen();
  }
}

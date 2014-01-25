package de.lessvoid.nifty.render;

import de.lessvoid.nifty.NiftyStopwatch;
import de.lessvoid.nifty.render.batch.BatchRenderDevice;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NiftyImageManager {
  @Nonnull
  private static final Logger log = Logger.getLogger(NiftyImageManager.class.getName());
  @Nonnull
  private final RenderDevice renderDevice;
  @Nonnull
  private final Map<String, ReferencedCountedImage> imageCache = new HashMap<String, ReferencedCountedImage>();
  @Nonnull
  private final Map<RenderImage, ReferencedCountedImage> backReference = new HashMap<RenderImage,
      ReferencedCountedImage>();
  @Nonnull
  private final NiftyImageManagerExt<ReferencedCountedImage> ext;

  public NiftyImageManager(@Nonnull final RenderDevice renderDevice) {
    this.renderDevice = renderDevice;
    this.ext = getExtImpl(renderDevice);
  }

  @Nullable
  public RenderImage registerImage(
      @Nonnull final String filename,
      final boolean filterLinear,
      @Nonnull final Screen screen) {
    ReferencedCountedImage image = addImage(filename, filterLinear, screen);
    if (image == null) {
      return null;
    }
    ext.registerImage(screen, image);
    return image.getRenderImage();
  }

  public void unregisterImage(@Nonnull final RenderImage image) {
    if (backReference.containsKey(image)) {
      ReferencedCountedImage reference = backReference.get(image);
      if (removeImage(reference)) {
        ext.unregisterImage(reference);
      }
    }
  }

  public void uploadScreenImages(@Nonnull final Screen screen) {
    log.fine(">>> uploadScreenImages [" + screen.getScreenId() + "] start");
    NiftyStopwatch.start();

    ext.uploadScreenImages(screen);

    long time = NiftyStopwatch.stop();
    if (log.isLoggable(Level.FINE)) {
      log.fine("{" + String.format("%d", time) + " ms} <<< uploadScreenImages [" + screen.getScreenId() + "]");
    }
    if (log.isLoggable(Level.FINER)) {
      log.finer("{" + String.format("%d", time) + " ms} <<< uploadScreenImages [" + screen.getScreenId() + "] " +
          getInfoString());
    }
  }

  public void unloadScreenImages(@Nonnull final Screen screen) {
    log.fine(">>> unloadScreenImages [" + screen.getScreenId() + "] start");
    NiftyStopwatch.start();

    ext.unloadScreenImages(screen, renderDevice, imageCache.values());

    long time = NiftyStopwatch.stop();
    if (log.isLoggable(Level.FINE)) {
      log.fine("{" + String.format("%d", time) + " ms} <<< unloadScreenImages [" + screen.getScreenId() + "]");
    }
    if (log.isLoggable(Level.FINER)) {
      log.finer("{" + String.format("%d", time) + " ms} <<< unloadScreenImages [" + screen.getScreenId() + "] " +
          getInfoString());
    }
  }

  public void screenAdded(@Nonnull final Screen screen) {
    ext.screenAdded(screen);

    if (log.isLoggable(Level.FINER)) {
      log.finer("screenAdded [" + screen.getScreenId() + "] " + getInfoString());
    }
  }

  public void screenRemoved(@Nonnull final Screen screen) {
    ext.screenRemoved(screen);

    if (log.isLoggable(Level.FINER)) {
      log.finer("screenRemoved [" + screen.getScreenId() + "] " + getInfoString());
    }
  }

  @Nonnull
  public RenderImage reload(@Nonnull final RenderImage image) {
    if (backReference.containsKey(image)) {
      return backReference.get(image).reload();
    }
    return image;
  }

  @Nonnull
  public String getInfoString() {
    StringBuffer result = new StringBuffer();
    result.append(imageCache.size()).append(" entries in cache and ").append(backReference.size())
        .append(" backreference entries.");
    ext.addScreenInfo(result);
    return result.toString();
  }

  @Nonnull
  private NiftyImageManagerExt<ReferencedCountedImage> getExtImpl(final RenderDevice renderer) {
    if (renderer instanceof BatchRenderDevice) {
      return new NiftyImageManagerExtBatch();
    }
    return new NiftyImageManagerExtStandard();
  }

  @Nonnull
  private static String buildName(@Nonnull final String filename, final boolean filterLinear) {
    return filename + "|" + Boolean.toString(filterLinear);
  }

  @Nullable
  private ReferencedCountedImage addImage(
      @Nonnull final String filename,
      final boolean filterLinear,
      @Nonnull final Screen screen) {
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
    if (renderImage == null) {
      return null;
    }
    ReferencedCountedImage newImage =
        ext.createReferencedCountedImage(renderDevice, screen, filename, filterLinear, renderImage, key);
    backReference.put(renderImage, newImage);
    imageCache.put(key, newImage);

    NiftyStopwatch.stop("imageManager.getImage(" + filename + ")");
    return newImage;
  }

  private boolean removeImage(@Nonnull final ReferencedCountedImage reference) {
    Screen screen = reference.getScreen();
    if (reference.removeReference()) {
      imageCache.remove(reference.getName());
      backReference.remove(reference.getRenderImage());
      if (log.isLoggable(Level.FINER)) {
        log.finer("[" + (screen.getScreenId()) + "][" + reference.getName() + "] refcount-- [" + reference
            .getReferences() + "] => DISPOSED");
      }
      return true;
    }
    if (log.isLoggable(Level.FINER)) {
      log.finer("[" + (screen.getScreenId()) + "][" + reference.getName() + "] refcount-- [" + reference
          .getReferences() + "]");
    }
    return false;
  }

  public interface ReferencedCountedImage {
    @Nonnull
    RenderImage reload();

    @Nonnull
    RenderImage addReference();

    boolean removeReference();

    int getReferences();

    @Nonnull
    RenderImage getRenderImage();

    @Nonnull
    String getName();

    @Nonnull
    Screen getScreen();
  }
}

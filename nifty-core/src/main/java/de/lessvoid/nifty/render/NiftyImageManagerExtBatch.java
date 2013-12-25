package de.lessvoid.nifty.render;

import de.lessvoid.nifty.batch.BatchRenderDevice;
import de.lessvoid.nifty.batch.BatchRenderImage;
import de.lessvoid.nifty.render.NiftyImageManager.ReferencedCountedImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NiftyImageManagerExtBatch implements NiftyImageManagerExt<ReferencedCountedImage> {
  @Nonnull
  private static final Logger log = Logger.getLogger(NiftyImageManagerExtBatch.class.getName());

  @Nonnull
  private final Map<String, Set<ReferencedCountedImageBatch>> screenRef = new HashMap<String,
      Set<ReferencedCountedImageBatch>>();
  @Nullable
  private Screen currentScreen;

  @Override
  public void registerImage(@Nonnull final Screen screen, @Nonnull final ReferencedCountedImage image) {
    Set<ReferencedCountedImageBatch> screenList = screenRef.get(screen.getScreenId());
    if (screenList == null) {
      screenList = new HashSet<ReferencedCountedImageBatch>();
      screenRef.put(screen.getScreenId(), screenList);
    }
    final ReferencedCountedImageBatch batchImage = cast(image);
    if (screenList.add(batchImage)) {
      if (log.isLoggable(Level.FINER)) {
        log.finer("[" + screen.getScreenId() + "] now with [" + screenList.size() + "] entries (" + image.getName() +
            ")");
      }
    }
    if (currentScreen != null && currentScreen.getScreenId().equals(screen.getScreenId())) {
      if (!batchImage.isUploaded()) {
        batchImage.upload();
      }
    }
  }

  @Override
  public void unregisterImage(@Nonnull final ReferencedCountedImage reference) {
    final ReferencedCountedImageBatch image = cast(reference);
    image.unload();

    Set<ReferencedCountedImageBatch> screenList = screenRef.get(reference.getScreen().getScreenId());
    if (screenList != null) {
      screenList.remove(image);
    }
  }

  @Override
  public void uploadScreenImages(@Nonnull final Screen screen) {
    currentScreen = screen;

    // find all ReferencedCountedImage and upload them into the texture atlas (for this screen).
    Set<ReferencedCountedImageBatch> imageList = screenRef.get(screen.getScreenId());
    if (imageList == null) {
      return;
    }

    for (ReferencedCountedImageBatch image : imageList) {
      image.upload();
    }
  }

  @Override
  public void unloadScreenImages(
      @Nonnull final Screen screen,
      @Nonnull final RenderDevice renderDevice,
      @Nonnull final Collection<ReferencedCountedImage> imageSet) {
    ((BatchRenderDevice) renderDevice).resetTextureAtlas();

    // we need to mark all images as unloaded
    for (ReferencedCountedImage i : imageSet) {
      cast(i).markAsUnloaded();
    }

    currentScreen = null;
  }

  @Nonnull
  private static ReferencedCountedImageBatch cast(@Nonnull final ReferencedCountedImage image) {
    if (image instanceof ReferencedCountedImageBatch) {
      return (ReferencedCountedImageBatch) image;
    }
    throw new IllegalArgumentException("Illegal image type supplied: " + image.getClass().getName() + "expected " +
        ReferencedCountedImageBatch.class.getName());
  }

  @Override
  public void screenAdded(@Nonnull final Screen screen) {
  }

  @Override
  public void screenRemoved(@Nonnull final Screen screen) {
    screenRef.remove(screen.getScreenId());
  }

  @Override
  public void addScreenInfo(@Nonnull final StringBuffer result) {
    if (screenRef.entrySet().isEmpty()) {
      return;
    }
    result.append("\n");
    for (Map.Entry<String, Set<ReferencedCountedImageBatch>> entry : screenRef.entrySet()) {
      result.append("\n[").append(entry.getKey()).append("]\n");
      for (ReferencedCountedImageBatch image : entry.getValue()) {
        result.append(image.toString());
      }
    }
  }

  @Nonnull
  @Override
  public ReferencedCountedImageBatch createReferencedCountedImage(
      @Nonnull final RenderDevice renderDevice,
      @Nonnull final Screen screen,
      @Nonnull final String filename,
      final boolean filterLinear,
      @Nonnull final RenderImage renderImage,
      @Nonnull final String key) {
    return new ReferencedCountedImageBatch(renderDevice, screen, filename, filterLinear, renderImage, key);
  }

  /**
   * A standard implementation of a ReferencedCountedImage without Batch support.
   *
   * @author void
   */
  public static class ReferencedCountedImageBatch implements ReferencedCountedImage {
    @Nonnull
    private final RenderDevice renderDevice;
    @Nonnull
    private final Screen screen;
    @Nonnull
    private final String filename;
    private final boolean filterLinear;
    @Nonnull
    private final String key;
    @Nonnull
    private RenderImage renderImage;
    private int references;

    public ReferencedCountedImageBatch(
        @Nonnull final RenderDevice renderDevice,
        @Nonnull final Screen screen,
        @Nonnull final String filename,
        final boolean filterLinear,
        @Nonnull final RenderImage renderImage,
        @Nonnull final String key) {
      this.renderDevice = renderDevice;
      this.screen = screen;
      this.filename = filename;
      this.filterLinear = filterLinear;
      this.key = key;
      this.renderImage = renderImage;
      this.references = 1;
    }

    public void upload() {
      BatchRenderImage batchRenderImage = (BatchRenderImage) renderImage;
      batchRenderImage.upload();
    }

    public void unload() {
      BatchRenderImage batchRenderImage = (BatchRenderImage) renderImage;
      batchRenderImage.unload();
    }

    public void markAsUnloaded() {
      BatchRenderImage batchRenderImage = (BatchRenderImage) renderImage;
      batchRenderImage.markAsUnloaded();
    }

    @Nonnull
    @Override
    public RenderImage reload() {
      final RenderImage newImage = renderDevice.createImage(filename, filterLinear);
      if (newImage == null) {
        log.severe("Failed to reload image, reloading canceled.");
      } else {
        renderImage.dispose();
        renderImage = newImage;
      }
      return renderImage;
    }

    @Nonnull
    @Override
    public RenderImage addReference() {
      references++;
      return renderImage;
    }

    @Override
    public boolean removeReference() {
      references--;
      if (references == 0) {
        renderImage.dispose();
        return true;
      }
      return false;
    }

    @Override
    public int getReferences() {
      return references;
    }

    @Nonnull
    @Override
    public RenderImage getRenderImage() {
      return renderImage;
    }

    @Nonnull
    @Override
    public String getName() {
      return key;
    }

    @Nonnull
    @Override
    public Screen getScreen() {
      return screen;
    }

    public boolean isUploaded() {
      if (renderImage instanceof BatchRenderImage) {
        return ((BatchRenderImage) renderImage).isUploaded();
      }
      return false;
    }

    @Nonnull
    @Override
    public String toString() {
      return " - [" + getName() + "] reference count [" + getReferences() + "] uploaded [" + isUploaded() + "]\n";
    }
  }
}

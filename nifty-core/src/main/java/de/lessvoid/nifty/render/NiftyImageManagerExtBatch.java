package de.lessvoid.nifty.render;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.lessvoid.nifty.batch.BatchRenderDevice;
import de.lessvoid.nifty.batch.BatchRenderImage;
import de.lessvoid.nifty.render.NiftyImageManager.ReferencedCountedImage;
import de.lessvoid.nifty.render.NiftyImageManagerExtBatch.ReferencedCountedImageBatch;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;

public class NiftyImageManagerExtBatch<T extends ReferencedCountedImageBatch> implements NiftyImageManagerExt<T> {
  private static Logger log = Logger.getLogger(NiftyImageManagerExtBatch.class.getName());

  private Map<String, Set<T>> screenRef = new HashMap<String, Set<T>>();
  private Screen currentScreen;

  @Override
  public void registerImage(final Screen screen, final T image) {
    if (screen != null) {
      Set<T> screenList = screenRef.get(screen.getScreenId());
      if (screenList == null) {
        screenList = new HashSet<T>();
        screenRef.put(screen.getScreenId(), screenList);
      }
      if (screenList.add(image)) {
        if (log.isLoggable(Level.FINER)) {
          log.finer("[" + screen.getScreenId() + "] now with [" + screenList.size() + "] entries (" + image.getName() + ")");
        }
      }
      if (currentScreen != null && currentScreen.getScreenId().equals(screen.getScreenId())) {
        if (!image.isUploaded()) {
          image.upload();
        }
      }
    }
  }

  @Override
  public void unregisterImage(final T reference) {
    reference.unload();

    Set<T> screenList = screenRef.get(reference.getScreen().getScreenId());
    if (screenList != null) {
      screenList.remove(reference);
    }
  }

  @Override
  public void uploadScreenImages(final Screen screen) {
    currentScreen = screen;

    // find all ReferencedCountedImage and upload them into the texture atlas (for this screen).
    Set<T> imageList = screenRef.get(screen.getScreenId());
    if (imageList == null) {
      return;
    }

    for (T image : imageList) {
      image.upload();
    }
  }

  @Override
  public void unloadScreenImages(final Screen screen, final RenderDevice renderDevice, final Collection<T> imageSet) {
    ((BatchRenderDevice) renderDevice).resetTextureAtlas();

    // we need to mark all images as unloaded
    for (T i : imageSet) {
      i.markAsUnloaded();
    }

    currentScreen = null;
  }

  @Override
  public void screenAdded(final Screen screen) {
  }

  @Override
  public void screenRemoved(final Screen screen) {
    screenRef.remove(screen.getScreenId());
  }

  @Override
  public void addScreenInfo(final StringBuffer result) {
    if (screenRef.entrySet().isEmpty()) {
      return;
    }
    result.append("\n");
    for (Map.Entry<String, Set<T>> entry : screenRef.entrySet()) {
      result.append("\n[" + entry.getKey() + "]\n");
      for (T image : entry.getValue()) {
        result.append(image.toString());
      }
    }
  }

  @Override
  public T createReferencedCountedImage(
      final RenderDevice renderDevice,
      final Screen screen,
      final String filename,
      final boolean filterLinear,
      final RenderImage renderImage,
      final String key) {
    return (T) new ReferencedCountedImageBatch(renderDevice, screen, filename, filterLinear, renderImage, key);
  }

  /**
   * A standard implementation of a ReferencedCountedImage without Batch support.
   * @author void
   */
  public static class ReferencedCountedImageBatch implements ReferencedCountedImage {
    private final RenderDevice renderDevice;
    private final Screen screen;
    private final String filename;
    private final boolean filterLinear;
    private final String key;
    private RenderImage renderImage;
    private int references;

    public ReferencedCountedImageBatch(
        final RenderDevice renderDevice,
        final Screen screen,
        final String filename,
        final boolean filterLinear,
        final RenderImage renderImage,
        final String key) {
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

    @Override
    public RenderImage reload() {
      renderImage.dispose();
      renderImage = renderDevice.createImage(filename, filterLinear);
      return renderImage;
    }

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

    @Override
    public RenderImage getRenderImage() {
      return renderImage;
    }

    @Override
    public String getName() {
      return key;
    }

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

    @Override
    public String toString() {
      return " - [" + getName() + "] reference count [" + getReferences() + "] uploaded [" + isUploaded() + "]\n";
    }
  }
}

package de.lessvoid.nifty.render;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.lessvoid.nifty.NiftyStopwatch;
import de.lessvoid.nifty.batch.BatchRenderDevice;
import de.lessvoid.nifty.batch.BatchRenderImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;

public class NiftyImageManager {
  private static Logger log = Logger.getLogger(NiftyImageManager.class.getName());
  private RenderDevice renderDevice;
  private Map<String, ReferencedCountedImage> imageCache = new HashMap<String, ReferencedCountedImage>();
  private Map<RenderImage, ReferencedCountedImage> backReference = new HashMap<RenderImage, ReferencedCountedImage>();
  private Map<String, Set<ReferencedCountedImage>> screenRef = new HashMap<String, Set<ReferencedCountedImage>>();
  private Screen currentScreen;

  public NiftyImageManager(final RenderDevice renderDevice) {
    this.renderDevice = renderDevice;
  }

  public RenderImage registerImage(final String filename, final boolean filterLinear, final Screen screen) {
    ReferencedCountedImage image = addImage(filename, filterLinear, screen);

    if (screen != null) {
      Set<ReferencedCountedImage> screenList = screenRef.get(screen.getScreenId());
      if (screenList == null) {
        screenList = new HashSet<ReferencedCountedImage>();
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

    return image.getRenderImage();
  }

  public void unregisterImage(final RenderImage image) {
    if (backReference.containsKey(image)) {
      ReferencedCountedImage reference = backReference.get(image);
      if (removeImage(reference)) {
        if (isBatchRenderDevice()) {
          reference.unload();

          Set<ReferencedCountedImage> screenList = screenRef.get(reference.getScreen().getScreenId());
          if (screenList != null) {
            screenList.remove(reference);
          }
        }
      }
    }
  }

  public void uploadScreenImages(final Screen screen) {
    // Ok, I'll hate myself for this because it's really not clean to do such things BUT this is the only way - at least
    // the only one I've come up with - that will not require a change to the RenderDevice interface and therefore
    // allows us to be backward compatible with Nifty 1.3.2.
    //
    // This whole method will only be executed for a BatchRenderDevice.
    if (!isBatchRenderDevice()) {
      return;
    }

    currentScreen = screen;

    // find all ReferencedCountedImage and upload them into the texture atlas (for this screen).
    Set<ReferencedCountedImage> imageList = screenRef.get(screen.getScreenId());
    if (imageList == null) {
      return;
    }

    NiftyStopwatch.start();

    for (ReferencedCountedImage image : imageList) {
      image.upload();
    }

    long time = NiftyStopwatch.stop();
    if (log.isLoggable(Level.FINE)) {
      log.fine("{" + String.format("%d", time) + " ms} uploadScreenImages for [" + screen.getScreenId() + "]");
    }
    if (log.isLoggable(Level.FINER)) {
      log.finer("{" + String.format("%d", time) + " ms} uploadScreenImages for [" + screen.getScreenId() + "] " + getInfoString());
    }
  }

  public void unloadScreenImages(final Screen screen) {
    if (!isBatchRenderDevice()) {
      return;
    }

    NiftyStopwatch.start();

    ((BatchRenderDevice) renderDevice).resetTextureAtlas();

    Set<ReferencedCountedImage> imageList = screenRef.get(screen.getScreenId());
    if (imageList != null) {
      for (ReferencedCountedImage image : imageList) {
        image.markAsUnloaded();
      }
    }

    long time = NiftyStopwatch.stop();
    if (log.isLoggable(Level.FINE)) {
      log.fine("{" + String.format("%d", time) + " ms} unloadScreenImages for [" + screen.getScreenId() + "]");
    }
    if (log.isLoggable(Level.FINER)) {
      log.finer("{" + String.format("%d", time) + " ms} unloadScreenImages for [" + screen.getScreenId() + "] " + getInfoString());
    }

    currentScreen = null;
  }

  public RenderImage reload(final RenderImage image) {
    if (backReference.containsKey(image)) {
      return backReference.get(image).reload();
    }
    return image;
  }

  public String getInfoString() {
    StringBuffer result = new StringBuffer(); 
    result.append(imageCache.size() + " entries in cache and " + backReference.size() + " backreference entries.\n");
    for (Map.Entry<String, Set<ReferencedCountedImage>> entry : screenRef.entrySet()) {
      result.append("\n[" + entry.getKey() + "]\n");
      for (ReferencedCountedImage image : entry.getValue()) {
        result.append(" - [" + image.getName() + "] reference count [" + image.getReferences() + "] uploaded [" + image.isUploaded() + "]\n");
      }
    }
    return result.toString();
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
        log.finer("[" + screen.getScreenId() + "][" + key + "] refcount++ [" + existingImage.references + "]");
      }
      return existingImage;
    }
    NiftyStopwatch.start();

    RenderImage renderImage = renderDevice.createImage(filename, filterLinear);
    ReferencedCountedImage newImage = new ReferencedCountedImage(renderDevice, screen, filename, filterLinear, renderImage);
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

  private boolean isBatchRenderDevice() {
    return renderDevice instanceof BatchRenderDevice;
  }

  private static class ReferencedCountedImage {
    private final RenderDevice renderDevice;
    private final Screen screen;
    private final String filename;
    private final boolean filterLinear;
    private final String key;
    private RenderImage renderImage;
    private int references;

    public ReferencedCountedImage(
        final RenderDevice renderDevice,
        final Screen screen,
        final String filename,
        final boolean filterLinear,
        final RenderImage renderImage) {
      this.renderDevice = renderDevice;
      this.screen = screen;
      this.filename = filename;
      this.filterLinear = filterLinear;
      this.key = buildName(filename, filterLinear);
      this.renderImage = renderImage;
      this.references = 1;
    }

    public void upload() {
      BatchRenderImage batchRenderImage = (BatchRenderImage) renderImage;
      batchRenderImage.upload();
      if (log.isLoggable(Level.FINER)) {
        log.finer("[" + screen.getScreenId() + "][" + filename + "] uploaded (texture atlas)");
      }
    }

    public void unload() {
      BatchRenderImage batchRenderImage = (BatchRenderImage) renderImage;
      batchRenderImage.unload();
      if (log.isLoggable(Level.FINER)) {
        log.finer("[" + screen.getScreenId() + "][" + filename + "] unloaded (texture atlas)");
      }
    }

    public void markAsUnloaded() {
      BatchRenderImage batchRenderImage = (BatchRenderImage) renderImage;
      batchRenderImage.markAsUnloaded();
      if (log.isLoggable(Level.FINER)) {
        log.finer("[" + screen.getScreenId() + "][" + filename + "] marked unloaded (texture atlas)");
      }
    }

    public RenderImage reload() {
      renderImage.dispose();
      renderImage = renderDevice.createImage(filename, filterLinear);
      return renderImage;
    }

    public RenderImage addReference() {
      references++;
      return renderImage;
    }

    public boolean removeReference() {
      references--;
      if (references == 0) {
        renderImage.dispose();
        return true;
      }
      return false;
    }

    public int getReferences() {
      return references;
    }

    public RenderImage getRenderImage() {
      return renderImage;
    }

    public String getName() {
      return key;
    }

    public Screen getScreen() {
      return screen;
    }

    public boolean isUploaded() {
      return ((BatchRenderImage) renderImage).isUploaded();
    }
  }
}

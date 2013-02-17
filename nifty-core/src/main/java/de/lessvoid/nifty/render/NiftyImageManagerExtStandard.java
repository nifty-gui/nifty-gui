package de.lessvoid.nifty.render;

import java.util.Collection;

import de.lessvoid.nifty.render.NiftyImageManager.ReferencedCountedImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;

public class NiftyImageManagerExtStandard<T extends ReferencedCountedImage> implements NiftyImageManagerExt<T> {

  @Override
  public void registerImage(final Screen screen, final T image) {
  }

  @Override
  public void unregisterImage(final T reference) {
  }

  @Override
  public void uploadScreenImages(final Screen screen) {
  }

  @Override
  public void unloadScreenImages(final Screen screen, final RenderDevice renderDevice, final Collection<T> imageSet) {
  }

  @Override
  public void screenAdded(final Screen screen) {
  }

  @Override
  public void screenRemoved(final Screen screen) {
  }

  @Override
  public void addScreenInfo(final StringBuffer result) {
  }

  @Override
  public T createReferencedCountedImage(
      final RenderDevice renderDevice,
      final Screen screen,
      final String filename,
      final boolean filterLinear,
      final RenderImage renderImage,
      final String key) {
    return (T) new ReferencedCountedImageStandard(renderDevice, screen, filename, filterLinear, renderImage, key);
  }

  /**
   * A standard implementation of a ReferencedCountedImage without Batch support.
   * @author void
   */
  public static class ReferencedCountedImageStandard implements ReferencedCountedImage {
    private final RenderDevice renderDevice;
    private final Screen screen;
    private final String filename;
    private final boolean filterLinear;
    private final String key;
    private RenderImage renderImage;
    private int references;

    public ReferencedCountedImageStandard(
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

    @Override
    public String toString() {
      return " - [" + getName() + "] reference count [" + getReferences() + "]\n";
    }
  }
}

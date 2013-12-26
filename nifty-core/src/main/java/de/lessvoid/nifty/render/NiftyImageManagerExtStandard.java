package de.lessvoid.nifty.render;

import de.lessvoid.nifty.render.NiftyImageManager.ReferencedCountedImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.logging.Logger;

public class NiftyImageManagerExtStandard implements NiftyImageManagerExt<ReferencedCountedImage> {

  @Override
  public void registerImage(@Nonnull final Screen screen, @Nonnull final ReferencedCountedImage image) {
  }

  @Override
  public void unregisterImage(@Nonnull final ReferencedCountedImage reference) {
  }

  @Override
  public void uploadScreenImages(@Nonnull final Screen screen) {
  }

  @Override
  public void unloadScreenImages(
      @Nonnull final Screen screen,
      @Nonnull final RenderDevice renderDevice,
      @Nonnull final Collection<ReferencedCountedImage> imageSet) {
  }

  @Override
  public void screenAdded(@Nonnull final Screen screen) {
  }

  @Override
  public void screenRemoved(@Nonnull final Screen screen) {
  }

  @Override
  public void addScreenInfo(@Nonnull final StringBuffer result) {
  }

  @Nonnull
  @Override
  public ReferencedCountedImage createReferencedCountedImage(
      @Nonnull final RenderDevice renderDevice,
      @Nonnull final Screen screen,
      @Nonnull final String filename,
      final boolean filterLinear,
      @Nonnull final RenderImage renderImage,
      @Nonnull final String key) {
    return new ReferencedCountedImageStandard(renderDevice, screen, filename, filterLinear, renderImage, key);
  }

  /**
   * A standard implementation of a ReferencedCountedImage without Batch support.
   *
   * @author void
   */
  public static class ReferencedCountedImageStandard implements ReferencedCountedImage {
    private static final Logger log = Logger.getLogger(ReferencedCountedImageStandard.class.getName());
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

    public ReferencedCountedImageStandard(
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

    @Nonnull
    @Override
    public RenderImage reload() {
      RenderImage newImage = renderDevice.createImage(filename, filterLinear);
      if (newImage == null) {
        log.warning("Reloading of image failed! Keeping the old image alive.");
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

    @Nonnull
    @Override
    public String toString() {
      return " - [" + getName() + "] reference count [" + getReferences() + "]\n";
    }
  }
}

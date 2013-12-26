package de.lessvoid.nifty.elements.render;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Image Renderer.
 *
 * @author void
 */
public class ImageRenderer implements ElementRenderer {
  @Nullable
  private NiftyImage image;
  private int inset = 0;

  /**
   * Set Insert.
   */
  public void setInset(final int insetParam) {
    inset = insetParam;
  }

  /**
   * render it.
   *
   * @param element the element this ElementRenderer connects to
   * @param r       the RenderDevice
   */
  @Override
  public final void render(@Nonnull final Element element, @Nonnull final NiftyRenderEngine r) {
    if (image != null) {
      r.renderImage(
          image,
          element.getX() + inset,
          element.getY() + inset,
          element.getWidth() - inset * 2,
          element.getHeight() - inset * 2);
    }
  }

  /**
   * Get the contained Image.
   *
   * @return the Image
   */
  @Nullable
  public NiftyImage getImage() {
    return image;
  }

  /**
   * Set a new image.
   *
   * @param newImage new image
   */
  public void setImage(@Nullable final NiftyImage newImage) {
    image = newImage;
  }
}

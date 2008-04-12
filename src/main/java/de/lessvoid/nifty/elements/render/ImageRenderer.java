package de.lessvoid.nifty.elements.render;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderDevice;
import de.lessvoid.nifty.render.RenderImage;

/**
 * Image Renderer.
 * @author void
 */
public class ImageRenderer implements ElementRenderer {

  /**
   * the render image this ElementRenderer will render.
   */
  private de.lessvoid.nifty.render.RenderImage image;

  /**
   * create a new SingleImage instance using the given image.
   * @param newImage the image we should render
   */
  public ImageRenderer(final RenderImage newImage) {
    this.image = newImage;
  }

  /**
   * render it.
   * @param w the element this ElementRenderer connects to
   * @param r the RenderDevice
   */
  public final void render(final Element w, final RenderDevice r) {
    r.renderImage(image, w.getX(), w.getY(), w.getWidth(), w.getHeight());
  }

  /**
   * Get the contained Image.
   * @return the Image
   */
  public de.lessvoid.nifty.render.RenderImage getImage() {
    return image;
  }
}

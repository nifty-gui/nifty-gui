package de.lessvoid.nifty.elements.render;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;

/**
 * Image Renderer.
 * @author void
 */
public class ImageRenderer implements ElementRenderer {

  /**
   * the render image this ElementRenderer will render.
   */
  private NiftyImage image;

  /**
   * create a new SingleImage instance using the given image.
   * @param newImage the image we should render
   */
  public ImageRenderer(final NiftyImage newImage) {
    this.image = newImage;
  }

  /**
   * render it.
   * @param element the element this ElementRenderer connects to
   * @param r the RenderDevice
   */
  public final void render(final Element element, final NiftyRenderEngine r) {
    if (this.image != null) {
      r.renderImage(image, element.getX(), element.getY(), element.getWidth(), element.getHeight());
    }
  }

  /**
   * Get the contained Image.
   * @return the Image
   */
  public NiftyImage getImage() {
    return image;
  }

  /**
   * Set a new image.
   * @param newImage new image
   */
  public void setImage(final NiftyImage newImage) {
    image = newImage;
  }
}

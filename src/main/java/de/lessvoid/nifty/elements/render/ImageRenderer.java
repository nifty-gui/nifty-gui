package de.lessvoid.nifty.elements.render;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderEngine;
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
   * @param element the element this ElementRenderer connects to
   * @param r the RenderDevice
   */
  public final void render(final Element element, final RenderEngine r) {
    r.renderImage(image, element.getX(), element.getY(), element.getWidth(), element.getHeight());
  }

  /**
   * Get the contained Image.
   * @return the Image
   */
  public de.lessvoid.nifty.render.RenderImage getImage() {
    return image;
  }

  /**
   * Set a new image.
   * @param newImage new image
   */
  public void setImage(final RenderImage newImage) {
    image = newImage;
  }
}

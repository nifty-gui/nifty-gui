package de.lessvoid.nifty.effects.general;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderEngine;
import de.lessvoid.nifty.render.RenderImage;
import de.lessvoid.nifty.render.RenderImageSubImageMode;

/**
 * Image Overlay.
 * @author void
 */
public class ImageOverlay implements EffectImpl {

  /**
   * overlay image.
   */
  private RenderImage image;

  /**
   * initialize.
   * @param nifty Nifty
   * @param element Element
   * @param parameter Parameter
   */
  public void initialize(final Nifty nifty, final Element element, final Properties parameter) {
    image = nifty.getRenderDevice().createImage(parameter.getProperty("filename"), true);
    String subImageSizeMode = parameter.getProperty("subImageSizeMode", null);
    if (subImageSizeMode != null) {
      image.setSubImageMode(RenderImageSubImageMode.valueOf(subImageSizeMode));
    }

    String resizeHint = parameter.getProperty("resizeHint", null);
    if (resizeHint != null) {
      image.setResizeHint(resizeHint);
      image.setSubImageMode(RenderImageSubImageMode.RESIZE());
    }
  }

  /**
   * execute the effect.
   * @param element the Element
   * @param normalizedTime TimeInterpolator to use
   * @param r RenderDevice to use
   */
  public void execute(
      final Element element,
      final float normalizedTime,
      final RenderEngine r) {
    r.renderImage(image, element.getX(), element.getY(), element.getWidth(), element.getHeight());
  }
}

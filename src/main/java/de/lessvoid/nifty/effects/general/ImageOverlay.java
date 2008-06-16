package de.lessvoid.nifty.effects.general;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyImageMode;
import de.lessvoid.nifty.render.NiftyRenderEngine;

/**
 * Image Overlay.
 * @author void
 */
public class ImageOverlay implements EffectImpl {

  /**
   * overlay image.
   */
  private NiftyImage image;

  /**
   * initialize.
   * @param nifty Nifty
   * @param element Element
   * @param parameter Parameter
   */
  public void initialize(final Nifty nifty, final Element element, final Properties parameter) {
    image = nifty.getRenderDevice().createImage(parameter.getProperty("filename"), true);
    String imageMode = parameter.getProperty("imageMode", null);
    if (imageMode != null) {
      image.setImageMode(NiftyImageMode.valueOf(imageMode));
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
      final NiftyRenderEngine r) {
    r.renderImage(image, element.getX(), element.getY(), element.getWidth(), element.getHeight());
  }
}

package de.lessvoid.nifty.effects.general;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderEngine;
import de.lessvoid.nifty.render.RenderImage;
import de.lessvoid.nifty.render.RenderImageSubImageMode;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.TimeProvider;
import de.lessvoid.nifty.tools.pulsate.Pulsator;

/**
 * ImagePulsate - image color pulsate.
 * @author void
 */
public class ImageOverlayPulsate implements EffectImpl {

  /**
   * overlay image.
   */
  private RenderImage image;

  /**
   * the pulsater.
   */
  private Pulsator pulsater;

  /**
   * Initialize.
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

    this.pulsater = new Pulsator(parameter, new TimeProvider());
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
    float value = pulsater.update();
    r.setColor(new Color(1.0f, 1.0f, 1.0f, value));
    r.renderImage(image, element.getX(), element.getY(), element.getWidth(), element.getHeight());
  }

}

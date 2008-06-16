package de.lessvoid.nifty.effects.general;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

/**
 * ImageSize effect.
 * @author void
 */
public class ImageSize implements EffectImpl {

  /**
   * startSize.
   */
  private float startSize;

  /**
   * endSize.
   */
  private float endSize;

  /**
   * initialize.
   * @param nifty Nifty
   * @param element Element
   * @param parameter Properties
   */
  public final void initialize(final Nifty nifty, final Element element, final Properties parameter) {
    startSize = Float.parseFloat(parameter.getProperty("startSize", "1.0"));
    endSize = Float.parseFloat(parameter.getProperty("endSize", "2.0"));
  }

  /**
   * execute the effect.
   * @param element Element
   * @param normalizedTime TimeInterpolator
   * @param r RenderDevice
   */
  public void execute(final Element element, final float normalizedTime, final NiftyRenderEngine r) {
    float t = normalizedTime;
    r.setImageScale(startSize + t * (endSize - startSize));
  }
}

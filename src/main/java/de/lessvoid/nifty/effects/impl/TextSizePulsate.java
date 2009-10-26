package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * text size pulsate.
 * @author void
 */
public class TextSizePulsate implements EffectImpl {

  /**
   * start size.
   */
  private SizeValue startSize = new SizeValue("0%");

  /**
   * end size.
   */
  private SizeValue endSize = new SizeValue("100%");

  /**
   * initialize.
   * @param nifty Nifty
   * @param element Element
   * @param parameter Parameter
   */
  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    String startSizeString = parameter.getProperty("startSize");
    if (startSizeString != null) {
      startSize = new SizeValue(startSizeString);
    }

    String endSizeString = parameter.getProperty("endSize");
    if (endSizeString != null) {
      endSize = new SizeValue(endSizeString);
    }
  }

  /**
   * execute the effect.
   * @param element the Element
   * @param normalizedTime TimeInterpolator to use
   * @param normalizedFalloff falloff value
   * @param r RenderDevice to use
   */
  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    float value =
      startSize.getValue(1.0f)
      +
      (float) Math.pow(normalizedTime, 0.3) * (endSize.getValue(1.0f) - startSize.getValue(1.0f));
    r.setRenderTextSize(1.0f + value);
  }

  /**
   * deactivate the effect.
   */
  public void deactivate() {
  }
}

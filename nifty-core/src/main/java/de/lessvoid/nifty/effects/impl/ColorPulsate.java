package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.pulsate.Pulsator;

/**
 * ColorPulsate.
 * @author void
 */
public class ColorPulsate implements EffectImpl {
  private Color startColor;
  private Color endColor;
  private Pulsator pulsator;
  private Color currentColor = new Color("#000f");

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    startColor = new Color(parameter.getProperty("startColor", "#00000000"));
    endColor = new Color(parameter.getProperty("endColor", "#ffffffff"));
    pulsator = new Pulsator(parameter, nifty.getTimeProvider());
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    float value = pulsator.update();
    currentColor.linear(startColor, endColor, value);
    r.setColor(currentColor);
  }

  public void deactivate() {
  }
}

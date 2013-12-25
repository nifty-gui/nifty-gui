package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.pulsate.Pulsator;

import javax.annotation.Nonnull;

/**
 * ColorPulsate.
 *
 * @author void
 */
public class ColorPulsate implements EffectImpl {
  private Color startColor;
  private Color endColor;
  private Pulsator pulsator;
  @Nonnull
  private final Color currentColor = new Color("#000f");

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    startColor = new Color(parameter.getProperty("startColor", "#00000000"));
    endColor = new Color(parameter.getProperty("endColor", "#ffffffff"));
    pulsator = new Pulsator(parameter, nifty.getTimeProvider());
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    float value = pulsator.update();
    currentColor.linear(startColor, endColor, value);
    r.setColor(currentColor);
  }

  @Override
  public void deactivate() {
  }
}

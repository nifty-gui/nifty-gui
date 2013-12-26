package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

import javax.annotation.Nonnull;

public class AutoScroll implements EffectImpl {
  private float distance = 100;
  private float start = 0;

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    int startValue = Integer.parseInt(parameter.getProperty("start", "0"));
    int endValue = Integer.parseInt(parameter.getProperty("end", "0"));
    distance = endValue - startValue;
    start = startValue;
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {

    r.moveTo(0, start + normalizedTime * distance);
  }

  @Override
  public void deactivate() {
  }
}

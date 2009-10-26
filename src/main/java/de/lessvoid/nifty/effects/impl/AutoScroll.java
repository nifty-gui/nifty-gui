package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

public class AutoScroll implements EffectImpl {
  private float distance = 100;
  private float start = 0;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    int startValue = Integer.parseInt(parameter.getProperty("start", "0"));
    int endValue = Integer.parseInt(parameter.getProperty("end", "0"));
    distance = endValue - startValue;
    start = startValue;
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    
    r.moveTo(0, start + normalizedTime * distance);
  }

  public void deactivate() {
  }
}

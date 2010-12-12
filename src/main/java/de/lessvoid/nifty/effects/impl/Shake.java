package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

/**
 * Shake effect.
 * @author void
 */
public class Shake implements EffectImpl {

  private float distance;
  private boolean global;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    distance = Float.parseFloat(parameter.getProperty("distance", "10.0"));
    global = "true".equals(parameter.getProperty("global", "true").toLowerCase());
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    float t = normalizedTime;
    if (t > 0.0f) {
      float d = distance;
      float x = -d + (float) Math.random() * 2 * d;
      float y = -d + (float) Math.random() * 2 * d;

      if (t >= 0.99f) {
        x = 0;
        y = 0;
      }

      if (global) {
        r.setGlobalPosition(x, y);
      } else {
        r.moveTo(x, y);
      }
    }
  }

  public void deactivate() {
  }
}

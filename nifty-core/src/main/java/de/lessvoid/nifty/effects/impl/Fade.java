package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Alpha;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.LinearInterpolator;

/**
 * Fade effect - blend stuff in or out.
 * @author void
 */
public class Fade implements EffectImpl {
  private Alpha start = Alpha.ZERO;
  private Alpha end = Alpha.FULL;
  private LinearInterpolator interpolator;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    // startColor and endColor (only alpha component used) are the old version of this
    // and are kept here only for backward compatibility. The current attributes are "start" and "end" alpha values.
    if (parameter.getProperty("startColor") != null) {
      start = new Alpha(new Color(parameter.getProperty("startColor", "#000000ff")).getAlpha());
    }
    if (parameter.getProperty("endColor") != null) {
      end = new Alpha(new Color(parameter.getProperty("endColor", "#ffffffff")).getAlpha());
    }
    if (parameter.getProperty("start") != null) {
      start = new Alpha(parameter.getProperty("start"));
    }
    if (parameter.getProperty("end") != null) {
      end = new Alpha(parameter.getProperty("end"));
    }
    interpolator = parameter.getInterpolator();
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    if (interpolator != null) {
      r.setColorAlpha(interpolator.getValue(normalizedTime));
    } else {
      Alpha a = start.linear(end, normalizedTime);
      r.setColorAlpha(a.getAlpha());
    }
  }

  public void deactivate() {
  }
}


